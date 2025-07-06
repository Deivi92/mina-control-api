
package com.minacontrol.turnos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record TipoTurnoCreateDTO(
        @NotBlank @Size(max = 50) String nombre,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFin,
        @Size(max = 255) String descripcion
) {}
