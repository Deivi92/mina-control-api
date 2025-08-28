package com.minacontrol.nomina.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConfiguracionTarifasCreateDTO(
        BigDecimal tarifaPorHora,
        BigDecimal bonoPorTonelada,
        String moneda,
        LocalDate fechaVigencia
) {
}