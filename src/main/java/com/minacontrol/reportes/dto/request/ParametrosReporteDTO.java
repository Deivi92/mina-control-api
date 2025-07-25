package com.minacontrol.reportes.dto.request;

import com.minacontrol.reportes.enums.FormatoReporte;
import com.minacontrol.reportes.enums.TipoReporte;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ParametrosReporteDTO(
        @NotNull TipoReporte tipoReporte,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        @NotNull FormatoReporte formato,
        List<Long> empleadosIds,
        List<String> areas,
        Boolean incluirDetalle,
        Map<String, Object> parametrosAdicionales
) {}
