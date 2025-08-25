package com.minacontrol.turnos.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.enums.RolSistema;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.turnos.dto.request.ExcepcionAsistenciaDTO;
import com.minacontrol.turnos.dto.request.RegistrarAsistenciaDTO;
import com.minacontrol.turnos.entity.AsignacionTurno;
import com.minacontrol.turnos.entity.TipoTurno;
import com.minacontrol.turnos.enums.EstadoAsistencia;
import com.minacontrol.turnos.enums.TipoRegistro;
import com.minacontrol.turnos.repository.AsignacionTurnoRepository;
import com.minacontrol.turnos.repository.RegistroAsistenciaRepository;
import com.minacontrol.turnos.repository.TipoTurnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.minacontrol.MinaControlApiApplication;

@SpringBootTest(classes = MinaControlApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AsistenciaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private TipoTurnoRepository tipoTurnoRepository;

    @Autowired
    private RegistroAsistenciaRepository registroAsistenciaRepository;

    @Autowired
    private AsignacionTurnoRepository asignacionTurnoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Empleado empleado;
    private TipoTurno turno;

    @BeforeEach
    void setUp() {
        registroAsistenciaRepository.deleteAll();
        asignacionTurnoRepository.deleteAll();
        empleadoRepository.deleteAll();
        tipoTurnoRepository.deleteAll();

        empleado = Empleado.builder()
                .nombres("Juan")
                .apellidos("Perez")
                .numeroIdentificacion("123456789")
                .email("juan.perez@example.com")
                .telefono("1234567890")
                .cargo("Operador")
                .fechaContratacion(LocalDate.of(2023, 1, 1))
                .salarioBase(new BigDecimal("1000.00"))
                .estado(EstadoEmpleado.ACTIVO)
                .rolSistema(RolSistema.EMPLEADO)
                .tieneUsuario(false)
                .build();
        empleadoRepository.save(empleado);

        turno = TipoTurno.builder()
                .nombre("Turno Diurno")
                .horaInicio(LocalTime.of(8, 0))
                .horaFin(LocalTime.of(17, 0))
                .descripcion("Jornada de día")
                .activo(true)
                .build();
        tipoTurnoRepository.save(turno);

        AsignacionTurno asignacion = new AsignacionTurno();
        asignacion.setEmpleadoId(empleado.getId());
        asignacion.setTipoTurnoId(turno.getId());
        asignacion.setFechaInicio(LocalDate.now().minusDays(1));
        asignacion.setFechaFin(LocalDate.now().plusDays(1));
        asignacionTurnoRepository.save(asignacion);
    }

    @Nested
    @DisplayName("CU-TUR-007: POST /api/v1/asistencia/registrar")
    @WithMockUser(roles = "ADMINISTRADOR")
    class RegistrarAsistenciaIT {

        @Test
        @DisplayName("Debe registrar la asistencia de entrada y devolver 200 OK")
        void should_RegisterEntradaAsistencia_And_Return200OK() throws Exception {
            RegistrarAsistenciaDTO request = new RegistrarAsistenciaDTO(empleado.getId(), TipoRegistro.ENTRADA);

            mockMvc.perform(post("/api/v1/asistencia/registrar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.empleadoId").value(empleado.getId().intValue()));
        }

        @Test
        @DisplayName("Debe registrar la asistencia de salida y devolver 200 OK")
        void should_RegisterSalidaAsistencia_And_Return200OK() throws Exception {
            // Primero, registrar una entrada
            RegistrarAsistenciaDTO entradaRequest = new RegistrarAsistenciaDTO(empleado.getId(), TipoRegistro.ENTRADA);
            mockMvc.perform(post("/api/v1/asistencia/registrar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(entradaRequest)));

            // Luego, registrar la salida
            RegistrarAsistenciaDTO salidaRequest = new RegistrarAsistenciaDTO(empleado.getId(), TipoRegistro.SALIDA);
            mockMvc.perform(post("/api/v1/asistencia/registrar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(salidaRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.empleadoId").value(empleado.getId().intValue()));
        }

        @Test
        @DisplayName("Debe devolver 400 Bad Request si faltan datos")
        void should_Return400_When_MissingData() throws Exception {
            RegistrarAsistenciaDTO request = new RegistrarAsistenciaDTO(null, null);

            mockMvc.perform(post("/api/v1/asistencia/registrar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("CU-TUR-008: GET /api/v1/asistencia/consultar")
    @WithMockUser(roles = {"ADMINISTRADOR", "EMPLEADO"})
    class ConsultarAsistenciaIT {

        @Test
        @DisplayName("Debe devolver el historial de asistencia de un empleado y 200 OK")
        void should_ReturnAsistenciaHistory_And_200OK() throws Exception {
            // Registrar algunas asistencias para el empleado
            RegistrarAsistenciaDTO entrada = new RegistrarAsistenciaDTO(empleado.getId(), TipoRegistro.ENTRADA);
            mockMvc.perform(post("/api/v1/asistencia/registrar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(entrada)));

            mockMvc.perform(get("/api/v1/asistencia/consultar")
                    .param("empleadoId", empleado.getId().toString())
                    .param("fechaInicio", LocalDate.now().toString())
                    .param("fechaFin", LocalDate.now().toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el empleado no existe")
        void should_Return404_When_EmpleadoNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/asistencia/consultar")
                    .param("empleadoId", "999")
                    .param("fechaInicio", LocalDate.now().toString())
                    .param("fechaFin", LocalDate.now().toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    @DisplayName("CU-TUR-009: POST /api/v1/asistencia/excepciones")
    @WithMockUser(roles = "ADMINISTRADOR")
    class GestionarExcepcionAsistenciaIT {

        @Test
        @DisplayName("Debe gestionar una excepción de asistencia y devolver 200 OK")
        void should_ManageAsistenciaException_And_Return200OK() throws Exception {
            ExcepcionAsistenciaDTO request = new ExcepcionAsistenciaDTO(empleado.getId(), LocalDate.now(), EstadoAsistencia.PERMISO, "Cita médica");

            mockMvc.perform(post("/api/v1/asistencia/excepciones")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estado").value("PERMISO"));
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el empleado no existe")
        void should_Return404_When_EmpleadoNotFound() throws Exception {
            ExcepcionAsistenciaDTO request = new ExcepcionAsistenciaDTO(999L, LocalDate.now(), EstadoAsistencia.PERMISO, "Cita médica");

            mockMvc.perform(post("/api/v1/asistencia/excepciones")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }
    }
}
