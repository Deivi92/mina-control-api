package com.minacontrol.nomina.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConfiguracionTarifasDTO(
        Long id,
        BigDecimal tarifaPorHora,
        BigDecimal bonoPorTonelada,
        String moneda,
        LocalDate fechaVigencia
) {
}