package com.minacontrol.turnos.service;

import com.minacontrol.turnos.dto.request.ExcepcionAsistenciaDTO;
import com.minacontrol.turnos.dto.request.RegistrarAsistenciaDTO;
import com.minacontrol.turnos.dto.response.RegistroAsistenciaDTO;

import java.time.LocalDate;
import java.util.List;

public interface IAsistenciaService {
    RegistroAsistenciaDTO registrarAsistencia(RegistrarAsistenciaDTO registrarAsistenciaDTO);
    List<RegistroAsistenciaDTO> consultarAsistencia(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin);
    RegistroAsistenciaDTO gestionarExcepcionAsistencia(ExcepcionAsistenciaDTO excepcionAsistenciaDTO);
}
