package com.minacontrol.empleado.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.empleado.dto.request.EmpleadoRequest;
import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.enums.RolSistema;
import com.minacontrol.empleado.repository.EmpleadoRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EmpleadoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Empleado empleadoActivo;
    private Empleado empleadoInactivo;

    @BeforeEach
    void setUp() {
        empleadoRepository.deleteAll();

        empleadoActivo = Empleado.builder()
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
        empleadoRepository.save(empleadoActivo);

        empleadoInactivo = Empleado.builder()
                .nombres("Ana")
                .apellidos("Gomez")
                .numeroIdentificacion("987654321")
                .email("ana.gomez@example.com")
                .telefono("0987654321")
                .cargo("Supervisor")
                .fechaContratacion(LocalDate.of(2022, 5, 10))
                .salarioBase(new BigDecimal("2000.00"))
                .estado(EstadoEmpleado.INACTIVO)
                .rolSistema(RolSistema.ADMINISTRADOR)
                .tieneUsuario(false)
                .build();
        empleadoRepository.save(empleadoInactivo);
    }

    @Nested
    @DisplayName("CU-EMP-001: POST /api/v1/empleados")
    @WithMockUser(roles = "ADMINISTRADOR")
    class CrearEmpleadoIT {

        @Test
        @DisplayName("Debe crear un empleado y devolver 201 Created")
        void should_CreateEmpleado_And_Return201() throws Exception {
            EmpleadoRequest request = new EmpleadoRequest("Nuevo", "Empleado", "11111", "nuevo@example.com",
                    "111", "Cargo Nuevo", LocalDate.now(), BigDecimal.TEN, RolSistema.EMPLEADO);

            mockMvc.perform(post("/api/v1/empleados")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.email").value("nuevo@example.com"));
        }

        @Test
        @DisplayName("Debe devolver 400 Bad Request por datos inválidos")
        void should_Return400_When_InvalidData() throws Exception {
            EmpleadoRequest request = new EmpleadoRequest("", "", "", "", "", "", null, null, null);

            mockMvc.perform(post("/api/v1/empleados")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe devolver 409 Conflict si el email ya existe")
        void should_Return409_When_EmailAlreadyExists() throws Exception {
            EmpleadoRequest request = new EmpleadoRequest("Otro", "Empleado", "22222", empleadoActivo.getEmail(),
                    "111", "Cargo", LocalDate.now(), BigDecimal.TEN, RolSistema.EMPLEADO);

            mockMvc.perform(post("/api/v1/empleados")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Debe devolver 409 Conflict si el numeroIdentificacion ya existe")
        void should_Return409_When_NumeroIdentificacionAlreadyExists() throws Exception {
            EmpleadoRequest request = new EmpleadoRequest("Otro", "Empleado", empleadoActivo.getNumeroIdentificacion(), "otro@example.com",
                    "111", "Cargo", LocalDate.now(), BigDecimal.TEN, RolSistema.EMPLEADO);

            mockMvc.perform(post("/api/v1/empleados")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("CU-EMP-007: GET /api/v1/empleados/{id}")
    @WithMockUser(roles = {"ADMINISTRADOR", "GERENTE"})
    class ObtenerEmpleadoPorIdIT {

        @Test
        @DisplayName("Debe devolver un empleado y 200 OK")
        void should_ReturnEmpleado_And_200OK() throws Exception {
            mockMvc.perform(get("/api/v1/empleados/{id}", empleadoActivo.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(empleadoActivo.getId()));
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el empleado no existe")
        void should_Return404_When_NotFound() throws Exception {
            mockMvc.perform(get("/api/v1/empleados/{id}", 999L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("CU-EMP-002: GET /api/v1/empleados")
    @WithMockUser(roles = "ADMINISTRADOR")
    class ListarEmpleadosIT {

        @Test
        @DisplayName("Debe devolver todos los empleados y 200 OK")
        void should_ReturnAllEmpleados_And_200OK() throws Exception {
            mockMvc.perform(get("/api/v1/empleados"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));
        }

        @Test
        @DisplayName("Debe devolver empleados filtrados por estado y 200 OK")
        void should_ReturnFilteredEmpleadosByEstado_And_200OK() throws Exception {
            mockMvc.perform(get("/api/v1/empleados")
                    .param("estado", EstadoEmpleado.ACTIVO.name()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].estado").value(EstadoEmpleado.ACTIVO.name()));
        }

        @Test
        @DisplayName("Debe devolver empleados filtrados por cargo y 200 OK")
        void should_ReturnFilteredEmpleadosByCargo_And_200OK() throws Exception {
            mockMvc.perform(get("/api/v1/empleados")
                    .param("cargo", "Operador"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].cargo").value("Operador"));
        }
    }

    @Nested
    @DisplayName("CU-EMP-003: PUT /api/v1/empleados/{id}")
    @WithMockUser(roles = "ADMINISTRADOR")
    class ActualizarEmpleadoIT {

        @Test
        @DisplayName("Debe actualizar un empleado y devolver 200 OK")
        void should_UpdateEmpleado_And_Return200OK() throws Exception {
            EmpleadoRequest updateRequest = new EmpleadoRequest("Juan Updated", "Perez Updated", empleadoActivo.getNumeroIdentificacion(), "juan.updated@example.com",
                    "111222333", "Gerente", empleadoActivo.getFechaContratacion(), new BigDecimal("1500.00"), RolSistema.SUPERVISOR);

            mockMvc.perform(put("/api/v1/empleados/{id}", empleadoActivo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombres").value("Juan Updated"))
                    .andExpect(jsonPath("$.email").value("juan.updated@example.com"));
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el empleado a actualizar no existe")
        void should_Return404_When_EmpleadoToUpdateNotFound() throws Exception {
            EmpleadoRequest updateRequest = new EmpleadoRequest("Juan Updated", "Perez Updated", "999999", "juan.updated@example.com",
                    "111222333", "Gerente", LocalDate.now(), new BigDecimal("1500.00"), RolSistema.SUPERVISOR);

            mockMvc.perform(put("/api/v1/empleados/{id}", 999L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe devolver 409 Conflict si el email ya existe en otro empleado al actualizar")
        void should_Return409_When_EmailAlreadyExistsInAnotherEmpleadoOnUpdate() throws Exception {
            EmpleadoRequest updateRequest = new EmpleadoRequest(empleadoActivo.getNombres(), empleadoActivo.getApellidos(),
                    empleadoActivo.getNumeroIdentificacion(), empleadoInactivo.getEmail(), // Using email of another employee
                    empleadoActivo.getTelefono(), empleadoActivo.getCargo(), empleadoActivo.getFechaContratacion(),
                    empleadoActivo.getSalarioBase(), empleadoActivo.getRolSistema());

            mockMvc.perform(put("/api/v1/empleados/{id}", empleadoActivo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("CU-EMP-006: DELETE /api/v1/empleados/{id}")
    @WithMockUser(roles = "ADMINISTRADOR")
    class EliminarEmpleadoIT {

        @Test
        @DisplayName("Debe desactivar un empleado y devolver 204 No Content")
        void should_DeactivateEmpleado_And_Return204NoContent() throws Exception {
            mockMvc.perform(delete("/api/v1/empleados/{id}", empleadoActivo.getId()))
                    .andExpect(status().isNoContent());

            // Verify that the employee is now INACTIVO in the database
            Empleado deactivatedEmpleado = empleadoRepository.findById(empleadoActivo.getId()).orElseThrow();
            assertThat(deactivatedEmpleado.getEstado()).isEqualTo(EstadoEmpleado.INACTIVO);
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el empleado a desactivar no existe")
        void should_Return404_When_EmpleadoToDeactivateNotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/empleados/{id}", 999L))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe devolver 409 Conflict si el empleado ya está inactivo")
        void should_Return409_When_EmpleadoIsAlreadyInactive() throws Exception {
            mockMvc.perform(delete("/api/v1/empleados/{id}", empleadoInactivo.getId()))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("CU-EMP-004: PATCH /api/v1/empleados/{id}/estado")
    @WithMockUser(roles = "ADMINISTRADOR")
    class CambiarEstadoEmpleadoIT {

        @Test
        @DisplayName("Debe cambiar el estado del empleado a INACTIVO y devolver 200 OK")
        void should_ChangeEstadoToInactivo_And_Return200OK() throws Exception {
            mockMvc.perform(patch("/api/v1/empleados/{id}/estado", empleadoActivo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(EstadoEmpleado.INACTIVO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estado").value(EstadoEmpleado.INACTIVO.name()));

            // Verify in DB
            Empleado updatedEmpleado = empleadoRepository.findById(empleadoActivo.getId()).orElseThrow();
            assertThat(updatedEmpleado.getEstado()).isEqualTo(EstadoEmpleado.INACTIVO);
        }

        @Test
        @DisplayName("Debe cambiar el estado del empleado a ACTIVO y devolver 200 OK")
        void should_ChangeEstadoToActivo_And_Return200OK() throws Exception {
            mockMvc.perform(patch("/api/v1/empleados/{id}/estado", empleadoInactivo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(EstadoEmpleado.ACTIVO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estado").value(EstadoEmpleado.ACTIVO.name()));

            // Verify in DB
            Empleado updatedEmpleado = empleadoRepository.findById(empleadoInactivo.getId()).orElseThrow();
            assertThat(updatedEmpleado.getEstado()).isEqualTo(EstadoEmpleado.ACTIVO);
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el empleado no existe")
        void should_Return404_When_EmpleadoNotFound() throws Exception {
            mockMvc.perform(patch("/api/v1/empleados/{id}/estado", 999L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(EstadoEmpleado.ACTIVO)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe devolver 400 Bad Request si el estado es inválido")
        void should_Return400_When_InvalidEstado() throws Exception {
            mockMvc.perform(patch("/api/v1/empleados/{id}/estado", empleadoActivo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("\"INVALID_STATUS\""))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("CU-EMP-005: GET /api/v1/empleados/perfil")
    @WithMockUser(username = "juan.perez@example.com", roles = "EMPLEADO")
    class ObtenerPerfilPersonalIT {

        @Test
        @DisplayName("Debe devolver el perfil personal del empleado y 200 OK")
        void should_ReturnPersonalProfile_And_200OK() throws Exception {
            mockMvc.perform(get("/api/v1/empleados/perfil"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("juan.perez@example.com"));
        }

        @Test
        @DisplayName("Debe devolver 404 Not Found si el perfil no existe para el usuario autenticado")
        void should_Return404_When_ProfileNotFound() throws Exception {
            // Simulate a user that exists in Spring Security but not in our Empleado table
            empleadoRepository.deleteAll(); // Remove existing employees

            mockMvc.perform(get("/api/v1/empleados/perfil"))
                    .andExpect(status().isNotFound());
        }
    }
}