
package com.minacontrol.turnos.dto.response;

import java.time.LocalTime;

public record TipoTurnoDTO(
        Long id,
        String nombre,
        LocalTime horaInicio,
        LocalTime horaFin,
        String descripcion,
        boolean activo
) {}
