package com.minacontrol.produccion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistroProduccionCreateDTO(
    @NotNull(message = "El ID del empleado no puede ser nulo")
    Long empleadoId,

    @NotNull(message = "El ID del tipo de turno no puede ser nulo")
    Long tipoTurnoId,

    @NotNull(message = "La fecha de registro no puede ser nula")
    @PastOrPresent(message = "La fecha de registro no puede ser futura")
    LocalDate fechaRegistro,

    @NotNull(message = "La cantidad extraída no puede ser nula")
    @Positive(message = "La cantidad extraída debe ser positiva")
    BigDecimal cantidadExtraidaToneladas,

    @NotBlank(message = "La ubicación de extracción no puede estar vacía")
    @Size(max = 100, message = "La ubicación no puede exceder los 100 caracteres")
    String ubicacionExtraccion,

    String observaciones
) {}
