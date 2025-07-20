package com.minacontrol.logistica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespachoCreateDTO(
    @NotBlank String nombreConductor,
    @NotBlank String placaVehiculo,
    @NotNull @Positive BigDecimal cantidadDespachadaToneladas,
    @NotBlank String destino,
    @NotNull LocalDate fechaProgramada,
    String observaciones
) {}
