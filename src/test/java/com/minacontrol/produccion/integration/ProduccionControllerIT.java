package com.minacontrol.produccion.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.enums.RolSistema;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.produccion.dto.request.RegistroProduccionCreateDTO;
import com.minacontrol.produccion.entity.RegistroProduccion;
import com.minacontrol.produccion.repository.RegistroProduccionRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Pruebas de Integración para ProduccionController")
class ProduccionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegistroProduccionRepository registroProduccionRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private TipoTurnoRepository tipoTurnoRepository;

    private Empleado empleadoBase;
    private TipoTurno turnoBase;

    @BeforeEach
    void setUp() {
        registroProduccionRepository.deleteAll();
        empleadoRepository.deleteAll();
        tipoTurnoRepository.deleteAll();

        empleadoBase = Empleado.builder()
                .nombres("Test")
                .apellidos("Empleado")
                .numeroIdentificacion("EMP-PROD-IT")
                .email("prod-it@example.com")
                .cargo("Operador de Pruebas")
                .fechaContratacion(LocalDate.now().minusYears(1))
                .salarioBase(new BigDecimal("1200.00"))
                .estado(EstadoEmpleado.ACTIVO)
                .rolSistema(RolSistema.EMPLEADO)
                .build();
        empleadoRepository.save(empleadoBase);

        turnoBase = TipoTurno.builder()
                .nombre("Turno de Integración")
                .horaInicio(LocalTime.of(8, 0))
                .horaFin(LocalTime.of(16, 0))
                .activo(true)
                .build();
        tipoTurnoRepository.save(turnoBase);
    }

    @Nested
    @DisplayName("CU-PRO-001: Endpoint POST /api/v1/produccion")
    @WithMockUser(roles = {"ADMINISTRADOR", "SUPERVISOR"})
    class RegistrarProduccionIT {

        @Test
        @DisplayName("Debe crear un registro de producción y devolver 201 Created")
        void should_CrearRegistro_When_DatosValidos() throws Exception {
            RegistroProduccionCreateDTO createDTO = new RegistroProduccionCreateDTO(empleadoBase.getId(), turnoBase.getId(), LocalDate.now(), new BigDecimal("15.5"), "Veta Principal", "Observaciones de prueba");

            mockMvc.perform(post("/api/v1/produccion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.empleadoId").value(empleadoBase.getId()));

            List<RegistroProduccion> registros = registroProduccionRepository.findAll();
            assertThat(registros).hasSize(1);
            assertThat(registros.get(0).getUbicacionExtraccion()).isEqualTo("Veta Principal");
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el empleado no existe")
        void should_Return404_When_EmpleadoNoExiste() throws Exception {
            RegistroProduccionCreateDTO createDTO = new RegistroProduccionCreateDTO(999L, turnoBase.getId(), LocalDate.now(), new BigDecimal("10.0"), "Veta Secundaria", null);

            mockMvc.perform(post("/api/v1/produccion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe devolver 409 Conflict si ya existe un registro para el mismo empleado, turno y fecha")
        void should_Return409_When_RegistroDuplicado() throws Exception {
            RegistroProduccion registroExistente = RegistroProduccion.builder().empleadoId(empleadoBase.getId()).tipoTurnoId(turnoBase.getId()).fechaRegistro(LocalDate.now()).cantidadExtraidaToneladas(new BigDecimal("5.0")).ubicacionExtraccion("Veta A").build();
            registroProduccionRepository.save(registroExistente);

            RegistroProduccionCreateDTO createDTO = new RegistroProduccionCreateDTO(empleadoBase.getId(), turnoBase.getId(), LocalDate.now(), new BigDecimal("12.0"), "Veta B", null);

            mockMvc.perform(post("/api/v1/produccion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO)))
                    .andExpect(status().isConflict());
        }
    }
    
    @Nested
    @DisplayName("CU-PRO-002: Endpoint GET /api/v1/produccion")
    @WithMockUser(roles = {"ADMINISTRADOR", "SUPERVISOR", "GERENTE"})
    class ListarRegistrosIT {

        @BeforeEach
        void nestedSetUp() {
            RegistroProduccion r1 = RegistroProduccion.builder().empleadoId(empleadoBase.getId()).tipoTurnoId(turnoBase.getId()).fechaRegistro(LocalDate.now()).cantidadExtraidaToneladas(new BigDecimal("10")).ubicacionExtraccion("A").build();
            RegistroProduccion r2 = RegistroProduccion.builder().empleadoId(empleadoBase.getId()).tipoTurnoId(turnoBase.getId()).fechaRegistro(LocalDate.now().minusDays(1)).cantidadExtraidaToneladas(new BigDecimal("20")).ubicacionExtraccion("B").build();
            registroProduccionRepository.saveAll(List.of(r1, r2));
        }

        @Test
        @DisplayName("Debe devolver todos los registros cuando no hay filtros")
        void should_ReturnAllRegistros_When_NoFilter() throws Exception {
            mockMvc.perform(get("/api/v1/produccion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
        }

        @Test
        @DisplayName("Debe devolver registros filtrados por fecha")
        void should_ReturnFilteredRegistros_When_FilterByDate() throws Exception {
            mockMvc.perform(get("/api/v1/produccion")
                .param("fechaInicio", LocalDate.now().minusDays(1).toString())
                .param("fechaFin", LocalDate.now().minusDays(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].ubicacionExtraccion").value("B"));
        }
    }

    @Nested
    @DisplayName("CU-PRO-003: Endpoint GET /api/v1/produccion/{id}")
    @WithMockUser(roles = {"ADMINISTRADOR", "SUPERVISOR", "GERENTE"})
    class ObtenerRegistroPorIdIT {

        private RegistroProduccion registroDePrueba;

        @BeforeEach
        void nestedSetUp() {
            registroDePrueba = RegistroProduccion.builder().empleadoId(empleadoBase.getId()).tipoTurnoId(turnoBase.getId()).fechaRegistro(LocalDate.now()).cantidadExtraidaToneladas(new BigDecimal("22.2")).ubicacionExtraccion("Veta de Prueba").validado(false).build();
            registroProduccionRepository.save(registroDePrueba);
        }

        @Test
        @DisplayName("Debe devolver un registro de producción y 200 OK si el ID existe")
        void should_ReturnRegistro_When_IdExiste() throws Exception {
            mockMvc.perform(get("/api/v1/produccion/{id}", registroDePrueba.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(registroDePrueba.getId()));
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el ID no existe")
        void should_Return404_When_IdNoExiste() throws Exception {
            mockMvc.perform(get("/api/v1/produccion/{id}", 999L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("CU-PRO-004: Endpoint PUT /api/v1/produccion/{id}")
    @WithMockUser(roles = {"ADMINISTRADOR", "SUPERVISOR"})
    class ActualizarRegistroIT {
        
        private RegistroProduccion registroParaActualizar;

        @BeforeEach
        void nestedSetUp() {
            registroParaActualizar = RegistroProduccion.builder().empleadoId(empleadoBase.getId()).tipoTurnoId(turnoBase.getId()).fechaRegistro(LocalDate.now()).cantidadExtraidaToneladas(new BigDecimal("33.3")).ubicacionExtraccion("Ubicacion Original").validado(false).build();
            registroProduccionRepository.save(registroParaActualizar);
        }

        @Test
        @DisplayName("Debe actualizar un registro y devolver 200 OK")
        void should_UpdateRegistro_And_Return200() throws Exception {
            RegistroProduccionCreateDTO updateDTO = new RegistroProduccionCreateDTO(empleadoBase.getId(), turnoBase.getId(), LocalDate.now(), new BigDecimal("44.4"), "Ubicacion Actualizada", null);

            mockMvc.perform(put("/api/v1/produccion/{id}", registroParaActualizar.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidadExtraidaToneladas").value(44.4));

            RegistroProduccion registroActualizado = registroProduccionRepository.findById(registroParaActualizar.getId()).get();
            assertThat(registroActualizado.getUbicacionExtraccion()).isEqualTo("Ubicacion Actualizada");
        }
    }

    @Nested
    @DisplayName("CU-PRO-005: Endpoint DELETE /api/v1/produccion/{id}")
    @WithMockUser(roles = "ADMINISTRADOR")
    class EliminarRegistroIT {

        private RegistroProduccion registroParaEliminar;

        @BeforeEach
        void nestedSetUp() {
            registroParaEliminar = RegistroProduccion.builder().empleadoId(empleadoBase.getId()).tipoTurnoId(turnoBase.getId()).fechaRegistro(LocalDate.now()).cantidadExtraidaToneladas(new BigDecimal("55.5")).ubicacionExtraccion("Para Eliminar").validado(false).build();
            registroProduccionRepository.save(registroParaEliminar);
        }

        @Test
        @DisplayName("Debe eliminar un registro y devolver 204 No Content")
        void should_DeleteRegistro_And_Return204() throws Exception {
            mockMvc.perform(delete("/api/v1/produccion/{id}", registroParaEliminar.getId()))
                .andExpect(status().isNoContent());
            
            assertThat(registroProduccionRepository.findById(registroParaEliminar.getId())).isEmpty();
        }
    }

    @Nested
    @DisplayName("CU-PRO-006: Endpoint PATCH /api/v1/produccion/{id}/validar")
    @WithMockUser(roles = {"ADMINISTRADOR", "GERENTE"})
    class ValidarRegistroIT {

        private RegistroProduccion registroParaValidar;

        @BeforeEach
        void nestedSetUp() {
            registroParaValidar = RegistroProduccion.builder().empleadoId(empleadoBase.getId()).tipoTurnoId(turnoBase.getId()).fechaRegistro(LocalDate.now()).cantidadExtraidaToneladas(new BigDecimal("66.6")).ubicacionExtraccion("Para Validar").validado(false).build();
            registroProduccionRepository.save(registroParaValidar);
        }

        @Test
        @DisplayName("Debe validar un registro y devolver 200 OK")
        void should_ValidateRegistro_And_Return200() throws Exception {
            mockMvc.perform(patch("/api/v1/produccion/{id}/validar", registroParaValidar.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validado").value(true));

            RegistroProduccion registroValidado = registroProduccionRepository.findById(registroParaValidar.getId()).get();
            assertThat(registroValidado.isValidado()).isTrue();
        }
    }
}
