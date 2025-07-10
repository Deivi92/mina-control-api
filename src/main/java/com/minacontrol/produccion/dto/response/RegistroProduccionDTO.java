package com.minacontrol.produccion.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistroProduccionDTO(
    Long id,
    Long empleadoId,
    String nombreEmpleado, // Enriched field
    Long tipoTurnoId,
    String nombreTurno,    // Enriched field
    LocalDate fechaRegistro,
    BigDecimal cantidadExtraidaToneladas,
    String ubicacionExtraccion,
    String observaciones,
    boolean validado
) {}
