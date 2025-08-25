
package com.minacontrol.turnos.dto.response;

import java.time.LocalDate;

public record AsignacionTurnoDTO(
        Long id,
        Long empleadoId,
        Long tipoTurnoId,
        LocalDate fechaInicio,
        LocalDate fechaFin
) {}
