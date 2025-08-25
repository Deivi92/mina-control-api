package com.minacontrol.reportes.dto.request;

import com.minacontrol.reportes.enums.FormatoReporte;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record DatosOperacionalesDTO(
        @NotEmpty List<String> datasets,
        @NotNull FormatoReporte formato,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        Map<String, Object> filtros
) {}
