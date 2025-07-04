package com.minacontrol.empleado.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmpleadoResponseDTO(
    Long id,
    String nombres,
    String apellidos,
    String numeroIdentificacion,
    String email,
    String telefono,
    String cargo,
    LocalDate fechaContratacion,
    BigDecimal salarioBase,
    String estado,
    String rolSistema,
    Boolean tieneUsuario,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    @Builder
    public EmpleadoResponseDTO {}
}
