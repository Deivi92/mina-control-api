package com.minacontrol.logistica.dto.response;

import com.minacontrol.logistica.domain.EstadoDespacho;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DespachoDTO(
    Long id,
    String numeroDespacho,
    String nombreConductor,
    String placaVehiculo,
    BigDecimal cantidadDespachadaToneladas,
    String destino,
    LocalDate fechaProgramada,
    LocalDateTime fechaSalida,
    LocalDateTime fechaEntrega,
    EstadoDespacho estado,
    String observaciones
) {}
