package com.minacontrol.reportes.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.minacontrol.reportes.dto.request.DatosOperacionalesDTO;
import com.minacontrol.reportes.dto.request.ParametrosReporteDTO;
import com.minacontrol.reportes.dto.response.ReporteDTO;
import com.minacontrol.reportes.service.IReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reportes", description = "Generación de reportes de producción, asistencia, etc.")
@RestController
@RequestMapping("/api/reportes") // Añadido el RequestMapping
@RequiredArgsConstructor
public class ReporteController {

    private final IReporteService reporteService;

    @PostMapping("/produccion")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<ReporteDTO> generarReporteProduccion(@Valid @RequestBody ParametrosReporteDTO parametros) {
        ReporteDTO reporte = reporteService.generarReporteProduccion(parametros);
        return new ResponseEntity<>(reporte, HttpStatus.CREATED);
    }

    @PostMapping("/asistencia")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<ReporteDTO> generarReporteAsistencia(@Valid @RequestBody ParametrosReporteDTO parametros) {
        ReporteDTO reporte = reporteService.generarReporteAsistencia(parametros);
        return new ResponseEntity<>(reporte, HttpStatus.CREATED);
    }

    @PostMapping("/costos-laborales")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<ReporteDTO> generarReporteCostosLaborales(@Valid @RequestBody ParametrosReporteDTO parametros) {
        ReporteDTO reporte = reporteService.generarReporteCostosLaborales(parametros);
        return new ResponseEntity<>(reporte, HttpStatus.CREATED);
    }

    @PostMapping("/exportar-datos")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<ReporteDTO> exportarDatosOperacionales(@Valid @RequestBody DatosOperacionalesDTO datos) {
        ReporteDTO reporte = reporteService.exportarDatosOperacionales(datos);
        return new ResponseEntity<>(reporte, HttpStatus.CREATED);
    }
}
