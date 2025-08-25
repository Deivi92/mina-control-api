package com.minacontrol.turnos.service;

import com.minacontrol.turnos.dto.request.ExcepcionAsistenciaDTO;
import com.minacontrol.turnos.dto.request.RegistrarAsistenciaDTO;
import com.minacontrol.turnos.dto.response.RegistroAsistenciaDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IAsistenciaService {
    RegistroAsistenciaDTO registrarAsistencia(RegistrarAsistenciaDTO registrarAsistenciaDTO);
    List<RegistroAsistenciaDTO> consultarAsistencia(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin);
    RegistroAsistenciaDTO gestionarExcepcionAsistencia(ExcepcionAsistenciaDTO excepcionAsistenciaDTO);
    Map<Long, BigDecimal> obtenerHorasTrabajadasPorPeriodo(Long periodoId, LocalDate fechaInicio, LocalDate fechaFin);
    List<Object> obtenerDatosAsistenciaParaReporte(LocalDate fechaInicio, LocalDate fechaFin);
}
