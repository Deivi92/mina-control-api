package com.minacontrol.turnos.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.turnos.dto.request.AsignacionTurnoCreateDTO;
import com.minacontrol.turnos.dto.request.TipoTurnoCreateDTO;
import com.minacontrol.turnos.entity.TipoTurno;
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

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TurnoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TipoTurnoRepository tipoTurnoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private TipoTurno turnoDia;
    private TipoTurno turnoNoche;

    @BeforeEach
    void setUp() {
        tipoTurnoRepository.deleteAll();

        turnoDia = TipoTurno.builder()
                .nombre("Turno Diurno")
                .horaInicio(LocalTime.of(8, 0))
                .horaFin(LocalTime.of(17, 0))
                .descripcion("Jornada de día")
                .activo(true)
                .build();
        tipoTurnoRepository.save(turnoDia);

        turnoNoche = TipoTurno.builder()
                .nombre("Turno Nocturno")
                .horaInicio(LocalTime.of(20, 0))
                .horaFin(LocalTime.of(5, 0))
                .descripcion("Jornada de noche")
                .activo(true)
                .build();
        tipoTurnoRepository.save(turnoNoche);
    }

    // Aquí se añadirán los @Nested para cada caso de uso del TurnoController

    @Nested
    @DisplayName("CU-TUR-001: POST /api/v1/turnos")
    @WithMockUser(roles = "ADMINISTRADOR")
    class CrearTurnoIT {

        @Test
        @DisplayName("Debe crear un tipo de turno y devolver 201 Created")
        void should_CreateTipoTurno_And_Return201() throws Exception {
            TipoTurnoCreateDTO request = new TipoTurnoCreateDTO("Turno Tarde", LocalTime.of(14, 0), LocalTime.of(22, 0), "Jornada de tarde");

            mockMvc.perform(post("/api/v1/turnos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nombre").value("Turno Tarde"));
        }

        @Test
        @DisplayName("Debe devolver 400 Bad Request por datos inválidos")
        void should_Return400_When_InvalidData() throws Exception {
            TipoTurnoCreateDTO request = new TipoTurnoCreateDTO("", null, null, "");

            mockMvc.perform(post("/api/v1/turnos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe devolver 409 Conflict si el nombre del turno ya existe")
        void should_Return409_When_TurnoNameAlreadyExists() throws Exception {
            TipoTurnoCreateDTO request = new TipoTurnoCreateDTO(turnoDia.getNombre(), LocalTime.of(14, 0), LocalTime.of(22, 0), "Jornada de tarde");

            mockMvc.perform(post("/api/v1/turnos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("CU-TUR-002: GET /api/v1/turnos")
    @WithMockUser(roles = {"ADMINISTRADOR", "GERENTE"})
    class ListarTurnosIT {

        @Test
        @DisplayName("Debe devolver todos los tipos de turno y 200 OK")
        void should_ReturnAllTiposTurno_And_200OK() throws Exception {
            mockMvc.perform(get("/api/v1/turnos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));
        }
    }

    @Nested
    @DisplayName("CU-TUR-003: GET /api/v1/turnos/{id}")
    @WithMockUser(roles = {"ADMINISTRADOR", "GERENTE"})
    class ObtenerTurnoPorIdIT {

        @Test
        @DisplayName("Debe devolver un tipo de turno y 200 OK")
        void should_ReturnTipoTurno_And_200OK() throws Exception {
            mockMvc.perform(get("/api/v1/turnos/{id}", turnoDia.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(turnoDia.getId()));
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el turno no existe")
        void should_Return404_When_NotFound() throws Exception {
            mockMvc.perform(get("/api/v1/turnos/{id}", 999L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("CU-TUR-004: PUT /api/v1/turnos/{id}")
    @WithMockUser(roles = "ADMINISTRADOR")
    class ActualizarTurnoIT {

        @Test
        @DisplayName("Debe actualizar un tipo de turno y devolver 200 OK")
        void should_UpdateTipoTurno_And_Return200OK() throws Exception {
            TipoTurnoCreateDTO request = new TipoTurnoCreateDTO("Turno Diurno Modificado", LocalTime.of(9, 0), LocalTime.of(18, 0), "Jornada de día modificada");

            mockMvc.perform(put("/api/v1/turnos/{id}", turnoDia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Turno Diurno Modificado"));
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el turno a actualizar no existe")
        void should_Return404_When_TurnoToUpdateNotFound() throws Exception {
            TipoTurnoCreateDTO request = new TipoTurnoCreateDTO("Test", LocalTime.now(), LocalTime.now(), "");
            mockMvc.perform(put("/api/v1/turnos/{id}", 999L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("CU-TUR-005: DELETE /api/v1/turnos/{id}")
    @WithMockUser(roles = "ADMINISTRADOR")
    class EliminarTurnoIT {

        @Test
        @DisplayName("Debe desactivar un tipo de turno y devolver 204 No Content")
        void should_DeactivateTipoTurno_And_Return204NoContent() throws Exception {
            mockMvc.perform(delete("/api/v1/turnos/{id}", turnoDia.getId()))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el turno a desactivar no existe")
        void should_Return404_When_TurnoToDeactivateNotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/turnos/{id}", 999L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("CU-TUR-006: POST /api/v1/turnos/asignaciones")
    @WithMockUser(roles = "ADMINISTRADOR")
    class AsignarEmpleadoIT {

        private Empleado empleado;

        @BeforeEach
        void setUp() {
            empleadoRepository.deleteAll();
            empleado = new Empleado();
            empleado.setNombres("Test");
            empleado.setApellidos("User");
            empleado.setNumeroIdentificacion("12345");
            empleado.setEmail("test.user@example.com");
            empleado.setCargo("Tester");
            empleado.setFechaContratacion(LocalDate.now());
            empleado.setSalarioBase(new java.math.BigDecimal("1000"));
            empleado.setEstado(com.minacontrol.empleado.enums.EstadoEmpleado.ACTIVO);
            empleado.setRolSistema(com.minacontrol.empleado.enums.RolSistema.EMPLEADO);
            empleadoRepository.save(empleado);
        }

        @Test
        @DisplayName("Debe asignar un empleado a un turno y devolver 201 Created")
        void should_AssignEmpleadoToTurno_And_Return201() throws Exception {
            AsignacionTurnoCreateDTO request = new AsignacionTurnoCreateDTO(empleado.getId(), turnoDia.getId(), LocalDate.now(), LocalDate.now().plusDays(7));

            mockMvc.perform(post("/api/v1/turnos/asignaciones")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.empleadoId").value(empleado.getId()));
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el empleado no existe")
        void should_Return404_When_EmpleadoToAssignNotFound() throws Exception {
            AsignacionTurnoCreateDTO request = new AsignacionTurnoCreateDTO(999L, turnoDia.getId(), LocalDate.now(), LocalDate.now().plusDays(7));

            mockMvc.perform(post("/api/v1/turnos/asignaciones")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }
    }
}
