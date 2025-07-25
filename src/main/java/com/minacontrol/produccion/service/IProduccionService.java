package com.minacontrol.produccion.service;

import com.minacontrol.produccion.dto.request.RegistroProduccionCreateDTO;
import com.minacontrol.produccion.dto.response.RegistroProduccionDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IProduccionService {
    RegistroProduccionDTO registrarProduccion(RegistroProduccionCreateDTO createDTO);
    List<RegistroProduccionDTO> listarRegistros(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin);
    RegistroProduccionDTO obtenerRegistroPorId(Long id);
    RegistroProduccionDTO actualizarRegistro(Long id, RegistroProduccionCreateDTO updateDTO);
    void eliminarRegistro(Long id);
    RegistroProduccionDTO validarRegistro(Long id);
    Map<Long, BigDecimal> obtenerProduccionPorPeriodo(Long periodoId, LocalDate fechaInicio, LocalDate fechaFin);
    List<Object> obtenerDatosProduccionParaReporte(LocalDate fechaInicio, LocalDate fechaFin);
}
