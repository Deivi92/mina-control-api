
package com.minacontrol.turnos.dto.request;

import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record TipoTurnoUpdateDTO(
        @Size(max = 50) String nombre,
        LocalTime horaInicio,
        LocalTime horaFin,
        @Size(max = 255) String descripcion,
        Boolean activo
) {}
