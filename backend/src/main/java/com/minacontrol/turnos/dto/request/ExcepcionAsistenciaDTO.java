package com.minacontrol.turnos.dto.request;

import com.minacontrol.turnos.enums.EstadoAsistencia;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ExcepcionAsistenciaDTO(
        @NotNull Long empleadoId,
        @NotNull LocalDate fecha,
        @NotNull EstadoAsistencia tipoExcepcion,
        String motivo
) {
}
