package com.minacontrol.logistica.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.enums.RolSistema;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.logistica.domain.EstadoDespacho;
import com.minacontrol.logistica.dto.request.DespachoCreateDTO;
import com.minacontrol.logistica.entity.Despacho;
import com.minacontrol.logistica.repository.DespachoRepository;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Pruebas de Integración para LogisticaController")
class LogisticaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DespachoRepository despachoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @BeforeEach
    void setUp() {
        despachoRepository.deleteAll();
        empleadoRepository.deleteAll();

        Empleado adminUser = Empleado.builder()
                .nombres("Admin")
                .apellidos("User")
                .numeroIdentificacion("ADMIN-001")
                .email("admin@example.com")
                .cargo("Administrador")
                .fechaContratacion(LocalDate.now())
                .salarioBase(BigDecimal.valueOf(5000))
                .estado(EstadoEmpleado.ACTIVO)
                .rolSistema(RolSistema.ADMINISTRADOR)
                .tieneUsuario(true)
                .build();
        empleadoRepository.save(adminUser);
    }

    @Nested
    @DisplayName("CU-LOG-001: POST /api/v1/logistica/despachos")
    @WithMockUser(username = "admin@example.com", roles = {"ADMINISTRADOR", "SUPERVISOR"})
    class RegistrarDespachoIT {

        @Test
        @DisplayName("Debe registrar un despacho y devolver 201 Created")
        void should_RegistrarDespacho_And_Return201() throws Exception {
            DespachoCreateDTO createDTO = new DespachoCreateDTO(
                    "Conductor Test",
                    "ABC-123",
                    new BigDecimal("100.50"),
                    "Destino A",
                    LocalDate.now().plusDays(1),
                    "Observaciones de prueba"
            );

            mockMvc.perform(post("/api/v1/logistica/despachos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.numeroDespacho").isNotEmpty())
                    .andExpect(jsonPath("$.estado").value(EstadoDespacho.PROGRAMADO.name()));

            assertThat(despachoRepository.findAll()).hasSize(1);
        }

        @Test
        @DisplayName("Debe devolver 400 Bad Request por datos inválidos y verificar ErrorResponseDTO")
        void should_Return400_When_InvalidData() throws Exception {
            DespachoCreateDTO createDTO = new DespachoCreateDTO(
                    "", null, new BigDecimal("-10.0"), "", null, null
            );

            mockMvc.perform(post("/api/v1/logistica/despachos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.error", is("Bad Request")))
                    .andExpect(jsonPath("$.path", is("/api/v1/logistica/despachos")));
        }
    }

    @Nested
    @DisplayName("CU-LOG-002: GET /api/v1/logistica/despachos")
    @WithMockUser(username = "admin@example.com", roles = {"ADMINISTRADOR", "SUPERVISOR", "GERENTE"})
    class ConsultarDespachosIT {

        @BeforeEach
        void nestedSetUp() {
            Despacho d1 = Despacho.builder().numeroDespacho("DES-001").nombreConductor("Conductor Uno").placaVehiculo("ABC-111").cantidadDespachadaToneladas(new BigDecimal("50.0")).destino("Destino X").fechaProgramada(LocalDate.now().minusDays(2)).estado(EstadoDespacho.ENTREGADO).build();
            Despacho d2 = Despacho.builder().numeroDespacho("DES-002").nombreConductor("Conductor Dos").placaVehiculo("DEF-222").cantidadDespachadaToneladas(new BigDecimal("75.0")).destino("Destino Y").fechaProgramada(LocalDate.now().plusDays(1)).estado(EstadoDespacho.PROGRAMADO).build();
            despachoRepository.saveAll(List.of(d1, d2));
        }

        @Test
        @DisplayName("Debe devolver todos los despachos cuando no hay filtros")
        void should_ReturnAllDespachos_When_NoFilter() throws Exception {
            mockMvc.perform(get("/api/v1/logistica/despachos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));
        }

        @Test
        @DisplayName("Debe devolver despachos filtrados por rango de fechas")
        void should_ReturnFilteredDespachos_When_FilterByDate() throws Exception {
            mockMvc.perform(get("/api/v1/logistica/despachos")
                    .param("fechaInicio", LocalDate.now().minusDays(3).toString())
                    .param("fechaFin", LocalDate.now().minusDays(1).toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].numeroDespacho").value("DES-001"));
        }

        @Test
        @DisplayName("Debe devolver despachos filtrados por estado")
        void should_ReturnFilteredDespachos_When_FilterByEstado() throws Exception {
            mockMvc.perform(get("/api/v1/logistica/despachos")
                    .param("estado", EstadoDespacho.PROGRAMADO.name()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].numeroDespacho").value("DES-002"));
        }

        @Test
        @DisplayName("Debe devolver despachos filtrados por destino")
        void should_ReturnFilteredDespachos_When_FilterByDestino() throws Exception {
            mockMvc.perform(get("/api/v1/logistica/despachos")
                    .param("destino", "Destino Y"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].numeroDespacho").value("DES-002"));
        }

        @Test
        @DisplayName("Debe devolver 400 Bad Request si el rango de fechas es inválido")
        void should_Return400_When_InvalidDateRange() throws Exception {
            mockMvc.perform(get("/api/v1/logistica/despachos")
                    .param("fechaInicio", LocalDate.now().toString())
                    .param("fechaFin", LocalDate.now().minusDays(1).toString()))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.error", is("Bad Request")))
                    .andExpect(jsonPath("$.message", is("La fecha de inicio no puede ser posterior a la fecha de fin.")))
                    .andExpect(jsonPath("$.path", is("/api/v1/logistica/despachos")));
        }
    }

    @Nested
    @DisplayName("CU-LOG-003: PATCH /api/v1/logistica/despachos/{id}/estado")
    @WithMockUser(username = "admin@example.com", roles = {"ADMINISTRADOR", "SUPERVISOR"})
    class ActualizarEstadoDespachoIT {

        private Despacho despachoToUpdate;

        @BeforeEach
        void nestedSetUp() {
            despachoToUpdate = Despacho.builder().numeroDespacho("DES-003").nombreConductor("Conductor Tres").placaVehiculo("GHI-333").cantidadDespachadaToneladas(new BigDecimal("120.0")).destino("Destino Z").fechaProgramada(LocalDate.now()).estado(EstadoDespacho.PROGRAMADO).build();
            despachoRepository.save(despachoToUpdate);
        }

        @Test
        @DisplayName("Debe actualizar el estado a EN_TRANSITO y devolver 200 OK")
        void should_UpdateEstadoToEnTransito_And_Return200() throws Exception {
            mockMvc.perform(patch("/api/v1/logistica/despachos/{id}/estado", despachoToUpdate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(EstadoDespacho.EN_TRANSITO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estado").value(EstadoDespacho.EN_TRANSITO.name()))
                    .andExpect(jsonPath("$.fechaSalida").isNotEmpty());

            Despacho updated = despachoRepository.findById(despachoToUpdate.getId()).get();
            assertThat(updated.getEstado()).isEqualTo(EstadoDespacho.EN_TRANSITO);
            assertThat(updated.getFechaSalida()).isNotNull();
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el despacho no existe")
        void should_Return404_When_DespachoNotFound() throws Exception {
            mockMvc.perform(patch("/api/v1/logistica/despachos/{id}/estado", 999L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(EstadoDespacho.EN_TRANSITO)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.error", is("Not Found")))
                    .andExpect(jsonPath("$.message", is("Despacho no encontrado con ID: 999")))
                    .andExpect(jsonPath("$.path", is("/api/v1/logistica/despachos/999/estado")));
        }

        @Test
        @DisplayName("Debe devolver 409 Conflict si la transición de estado es inválida")
        void should_Return409_When_InvalidStateTransition() throws Exception {
            despachoToUpdate.setEstado(EstadoDespacho.ENTREGADO);
            despachoRepository.save(despachoToUpdate);

            mockMvc.perform(patch("/api/v1/logistica/despachos/{id}/estado", despachoToUpdate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(EstadoDespacho.PROGRAMADO)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status", is(409)))
                    .andExpect(jsonPath("$.error", is("Conflict")))
                    .andExpect(jsonPath("$.message", is("No se puede cambiar el estado de ENTREGADO a PROGRAMADO")))
                    .andExpect(jsonPath("$.path", is("/api/v1/logistica/despachos/" + despachoToUpdate.getId() + "/estado")));
        }

        @Test
        @DisplayName("Debe devolver 400 Bad Request si el estado es un enum no válido")
        void should_Return400_When_InvalidEstadoEnum() throws Exception {
            mockMvc.perform(patch("/api/v1/logistica/despachos/{id}/estado", despachoToUpdate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("\"INVALID_STATE\""))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.error", is("Bad Request")));
        }
    }
}
