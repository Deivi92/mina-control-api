package com.minacontrol.reportes.dto.response;

import java.time.LocalDateTime;

public record ReporteDTO(
        Long id,
        String tipoReporte,
        String nombreReporte,
        LocalDateTime fechaGeneracion,
        String formato,
        String urlDescarga
) {}
