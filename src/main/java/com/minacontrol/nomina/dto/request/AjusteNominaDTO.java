package com.minacontrol.nomina.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AjusteNominaDTO(
        @NotBlank
        String concepto,
        @NotNull
        @Positive
        BigDecimal monto,
        @NotNull
        Boolean esDeduccion,
        @NotBlank
        String justificacion
) {
}
