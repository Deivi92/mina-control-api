package com.minacontrol.empleado.unit;

import com.minacontrol.empleado.dto.request.EmpleadoRequest;
import com.minacontrol.empleado.dto.response.EmpleadoResponse;
import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.enums.RolSistema;
import com.minacontrol.empleado.mapper.EmpleadoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class EmpleadoMapperTest {

    private EmpleadoMapper empleadoMapper;

    @BeforeEach
    void setUp() {
        empleadoMapper = new EmpleadoMapper();
    }

    @Test
    @DisplayName("Debe mapear Empleado a EmpleadoResponse correctamente")
    void should_MapEmpleadoToEmpleadoResponse_Correctly() {
        // Arrange
        Empleado empleado = Empleado.builder()
                .id(1L)
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
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // Act
        EmpleadoResponse response = empleadoMapper.toResponse(empleado);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(empleado.getId());
        assertThat(response.nombres()).isEqualTo(empleado.getNombres());
        assertThat(response.apellidos()).isEqualTo(empleado.getApellidos());
        assertThat(response.numeroIdentificacion()).isEqualTo(empleado.getNumeroIdentificacion());
        assertThat(response.email()).isEqualTo(empleado.getEmail());
        assertThat(response.telefono()).isEqualTo(empleado.getTelefono());
        assertThat(response.cargo()).isEqualTo(empleado.getCargo());
        assertThat(response.fechaContratacion()).isEqualTo(empleado.getFechaContratacion());
        assertThat(response.salarioBase()).isEqualTo(empleado.getSalarioBase());
        assertThat(response.estado()).isEqualTo(empleado.getEstado());
        assertThat(response.rolSistema()).isEqualTo(empleado.getRolSistema());
        assertThat(response.tieneUsuario()).isEqualTo(empleado.isTieneUsuario());
        assertThat(response.createdAt()).isEqualTo(LocalDateTime.ofInstant(empleado.getCreatedAt(), ZoneId.systemDefault()));
        assertThat(response.updatedAt()).isEqualTo(LocalDateTime.ofInstant(empleado.getUpdatedAt(), ZoneId.systemDefault()));
    }

    @Test
    @DisplayName("Debe mapear EmpleadoRequest a Empleado correctamente")
    void should_MapEmpleadoRequestToEmpleado_Correctly() {
        // Arrange
        EmpleadoRequest request = new EmpleadoRequest(
                "Ana", "Gomez", "987654321", "ana.gomez@example.com",
                "9876543210", "Analista", LocalDate.of(2022, 5, 10),
                new BigDecimal("1500.00"), RolSistema.ADMINISTRADOR
        );

        // Act
        Empleado empleado = empleadoMapper.toEntity(request);

        // Assert
        assertThat(empleado).isNotNull();
        assertThat(empleado.getId()).isNull(); // ID debe ser nulo para una nueva entidad
        assertThat(empleado.getNombres()).isEqualTo(request.nombres());
        assertThat(empleado.getApellidos()).isEqualTo(request.apellidos());
        assertThat(empleado.getNumeroIdentificacion()).isEqualTo(request.numeroIdentificacion());
        assertThat(empleado.getEmail()).isEqualTo(request.email());
        assertThat(empleado.getTelefono()).isEqualTo(request.telefono());
        assertThat(empleado.getCargo()).isEqualTo(request.cargo());
        assertThat(empleado.getFechaContratacion()).isEqualTo(request.fechaContratacion());
        assertThat(empleado.getSalarioBase()).isEqualTo(request.salarioBase());
        assertThat(empleado.getRolSistema()).isEqualTo(request.rolSistema());
    }

    @Test
    @DisplayName("Debe actualizar Empleado desde EmpleadoRequest correctamente")
    void should_UpdateEntityFromRequest_Correctly() {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setNombres("Original");

        EmpleadoRequest request = new EmpleadoRequest(
                "Nuevo", "Apellido", "123", "test@test.com", "123", "Cargo",
                LocalDate.now(), BigDecimal.ONE, RolSistema.EMPLEADO
        );

        // Act
        empleadoMapper.updateEntityFromRequest(request, empleado);

        // Assert
        assertThat(empleado.getNombres()).isEqualTo(request.nombres());
        assertThat(empleado.getApellidos()).isEqualTo(request.apellidos());
    }
}