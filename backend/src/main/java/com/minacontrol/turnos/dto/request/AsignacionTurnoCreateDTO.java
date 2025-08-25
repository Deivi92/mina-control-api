
package com.minacontrol.turnos.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AsignacionTurnoCreateDTO(
        @NotNull Long empleadoId,
        @NotNull Long tipoTurnoId,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin
) {}
