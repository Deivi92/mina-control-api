package com.minacontrol.produccion.service;

import com.minacontrol.produccion.dto.request.RegistroProduccionCreateDTO;
import com.minacontrol.produccion.dto.response.RegistroProduccionDTO;

import java.time.LocalDate;
import java.util.List;

public interface IProduccionService {

    RegistroProduccionDTO registrarProduccion(RegistroProduccionCreateDTO createDTO);

    List<RegistroProduccionDTO> listarRegistros(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin);

    RegistroProduccionDTO obtenerRegistroPorId(Long id);

    RegistroProduccionDTO actualizarRegistro(Long id, RegistroProduccionCreateDTO updateDTO);

    void eliminarRegistro(Long id);

    RegistroProduccionDTO validarRegistro(Long id);
}
