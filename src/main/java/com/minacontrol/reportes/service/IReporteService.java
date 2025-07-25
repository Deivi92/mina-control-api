package com.minacontrol.reportes.service;

import com.minacontrol.reportes.dto.request.DatosOperacionalesDTO;
import com.minacontrol.reportes.dto.request.ParametrosReporteDTO;
import com.minacontrol.reportes.dto.response.ReporteDTO;

public interface IReporteService {
    ReporteDTO generarReporteProduccion(ParametrosReporteDTO parametros);
    ReporteDTO generarReporteAsistencia(ParametrosReporteDTO parametros);
    ReporteDTO generarReporteCostosLaborales(ParametrosReporteDTO parametros);
    ReporteDTO exportarDatosOperacionales(DatosOperacionalesDTO datos);
}
