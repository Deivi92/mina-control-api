package com.minacontrol.logistica.service;

import com.minacontrol.logistica.domain.EstadoDespacho;
import com.minacontrol.logistica.dto.request.DespachoCreateDTO;
import com.minacontrol.logistica.dto.response.DespachoDTO;

import java.time.LocalDate;
import java.util.List;

public interface ILogisticaService {
    DespachoDTO registrarDespacho(DespachoCreateDTO createDTO);
    List<DespachoDTO> consultarDespachos(LocalDate fechaInicio, LocalDate fechaFin, EstadoDespacho estado, String destino);
    DespachoDTO actualizarEstadoDespacho(Long id, EstadoDespacho nuevoEstado);
}
