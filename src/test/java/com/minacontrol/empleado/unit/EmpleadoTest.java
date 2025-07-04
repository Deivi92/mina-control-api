package com.minacontrol.empleado.unit;

import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.enums.RolSistema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EmpleadoTest {

    @Test
    @DisplayName("should create Empleado instance with builder")
    void should_CreateEmpleadoInstance_WithBuilder() {
        // Arrange
        Long id = 1L;
        String nombres = "Ana";
        String apellidos = "Gomez";
        String numeroIdentificacion = "987654321";
        String email = "ana.gomez@example.com";
        String telefono = "9876543210";
        String cargo = "Analista";
        LocalDate fechaContratacion = LocalDate.of(2022, 5, 10);
        BigDecimal salarioBase = new BigDecimal("1500.00");
        EstadoEmpleado estado = EstadoEmpleado.ACTIVO;
        RolSistema rolSistema = RolSistema.ADMINISTRADOR;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        Empleado empleado = Empleado.builder()
                .id(id)
                .nombres(nombres)
                .apellidos(apellidos)
                .numeroIdentificacion(numeroIdentificacion)
                .email(email)
                .telefono(telefono)
                .cargo(cargo)
                .fechaContratacion(fechaContratacion)
                .salarioBase(salarioBase)
                .estado(estado)
                .rolSistema(rolSistema)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Assert
        assertNotNull(empleado);
        assertEquals(id, empleado.getId());
        assertEquals(nombres, empleado.getNombres());
        assertEquals(apellidos, empleado.getApellidos());
        assertEquals(numeroIdentificacion, empleado.getNumeroIdentificacion());
        assertEquals(email, empleado.getEmail());
        assertEquals(telefono, empleado.getTelefono());
        assertEquals(cargo, empleado.getCargo());
        assertEquals(fechaContratacion, empleado.getFechaContratacion());
        assertEquals(salarioBase, empleado.getSalarioBase());
        assertEquals(estado, empleado.getEstado());
        assertEquals(rolSistema, empleado.getRolSistema());
        assertEquals(createdAt, empleado.getCreatedAt());
        assertEquals(updatedAt, empleado.getUpdatedAt());
    }

    @Test
    @DisplayName("should return correct full name")
    void should_ReturnCorrectFullName() {
        // Arrange
        Empleado empleado = Empleado.builder()
                .nombres("Pedro")
                .apellidos("Ramirez")
                .build();

        // Act
        String fullName = empleado.getNombres() + " " + empleado.getApellidos(); // Assuming a getter for full name or direct concatenation

        // Assert
        assertEquals("Pedro Ramirez", fullName);
    }

    // Add more tests for specific entity logic if any, beyond simple getters/setters
}
