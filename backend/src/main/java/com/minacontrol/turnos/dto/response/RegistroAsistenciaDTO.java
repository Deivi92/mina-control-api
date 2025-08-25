
package com.minacontrol.turnos.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record RegistroAsistenciaDTO(
        Long id,
        Long empleadoId,
        LocalDate fecha,
        LocalTime horaEntrada,
        LocalTime horaSalida,
        double horasTrabajadas,
        String estado,
        String motivo
) {}
