package com.minacontrol.nomina.dto.response;

import java.math.BigDecimal;

public record CalculoNominaDTO(
        Long id,
        Long empleadoId,
        BigDecimal totalNeto
) {
}
