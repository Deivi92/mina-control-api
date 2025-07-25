package com.minacontrol.nomina.service;

import com.minacontrol.nomina.dto.request.AjusteNominaDTO;
import com.minacontrol.nomina.dto.request.CalcularNominaRequestDTO;
import com.minacontrol.nomina.dto.response.CalculoNominaDTO;
import com.minacontrol.nomina.dto.response.CalculoNominaResumenDTO;
import com.minacontrol.nomina.dto.response.ComprobantePagoDTO;

import java.time.LocalDate;
import java.util.List;

public interface INominaService {
    CalculoNominaResumenDTO calcularNominaSemanal(CalcularNominaRequestDTO request);

    CalculoNominaDTO ajustarCalculoNomina(Long id, AjusteNominaDTO ajusteDTO);

    List<ComprobantePagoDTO> generarComprobantesPago(Long periodoId);

    List<CalculoNominaDTO> consultarHistorialPagos(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin);
    List<Object> obtenerDatosCostosParaReporte(LocalDate fechaInicio, LocalDate fechaFin);
}
