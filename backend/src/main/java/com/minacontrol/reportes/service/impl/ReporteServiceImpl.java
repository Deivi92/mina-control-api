package com.minacontrol.reportes.service.impl;

import com.minacontrol.reportes.dto.request.DatosOperacionalesDTO;
import com.minacontrol.reportes.dto.request.ParametrosReporteDTO;
import com.minacontrol.reportes.dto.response.ReporteDTO;
import com.minacontrol.reportes.entity.ReporteGenerado;
import com.minacontrol.reportes.enums.FormatoReporte;
import com.minacontrol.reportes.enums.TipoReporte;
import com.minacontrol.reportes.exception.DatosInsuficientesParaReporteException;
import com.minacontrol.reportes.exception.ErrorGeneracionReporteException;
import com.minacontrol.reportes.exception.ParametrosReporteInvalidosException;
import com.minacontrol.reportes.mapper.ReporteMapper;
import com.minacontrol.reportes.repository.ReporteGeneradoRepository;
import com.minacontrol.reportes.service.IReporteService;
import com.minacontrol.shared.service.GeneradorReporteService;
import com.minacontrol.produccion.service.IProduccionService;
import com.minacontrol.turnos.service.IAsistenciaService;
import com.minacontrol.nomina.service.INominaService;
import com.minacontrol.nomina.service.IConfiguracionTarifasService;
import com.minacontrol.nomina.entity.ConfiguracionTarifas; // Importar la entidad de configuración de tarifas
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList; // Importar ArrayList
import java.util.List;
import java.util.Optional; // Importar Optional
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements IReporteService {

    private final ReporteGeneradoRepository reporteRepository;
    private final IProduccionService produccionService;
    private final IAsistenciaService asistenciaService;
    private final INominaService nominaService;
    private final IConfiguracionTarifasService configuracionTarifasService; // Inyectar el servicio de configuración de tarifas
    private final GeneradorReporteService generadorReporteService;
    private final ReporteMapper reporteMapper;

    @Override
    @Transactional
    public ReporteDTO generarReporteProduccion(ParametrosReporteDTO parametros) {
        validarParametrosComunes(parametros);

        // 1. Obtener datos de producción
        List<Object> datosProduccion = produccionService.obtenerDatosProduccionParaReporte(parametros.fechaInicio(), parametros.fechaFin());
        if (datosProduccion.isEmpty()) {
            throw new DatosInsuficientesParaReporteException("No se encontraron datos de producción para el período especificado.");
        }

        // 2. Obtener configuración de tarifas vigente
        Optional<ConfiguracionTarifas> configuracionOpt = configuracionTarifasService.obtenerConfiguracionVigente("COP");
        ConfiguracionTarifas configuracion = configuracionOpt.orElse(null);
        
        // 3. Agregar información de tarifas a los datos del reporte
        if (configuracion != null) {
            // Crear una nueva lista mutable para evitar UnsupportedOperationException
            datosProduccion = new ArrayList<>(datosProduccion);
            datosProduccion.add(configuracion); // Agregar la configuración de tarifas a los datos
        }

        // 4. Generar archivo
        String rutaArchivo;
        try {
            if (parametros.formato() == FormatoReporte.PDF) {
                rutaArchivo = generadorReporteService.generarArchivoPDF(datosProduccion, "produccion_template");
            } else if (parametros.formato() == FormatoReporte.EXCEL) {
                rutaArchivo = generadorReporteService.generarArchivoExcel(datosProduccion);
            } else {
                rutaArchivo = generadorReporteService.generarArchivoCSV(datosProduccion);
            }
        } catch (Exception e) {
            throw new ErrorGeneracionReporteException("Error al generar el archivo del reporte de producción: " + e.getMessage(), e);
        }

        // 5. Guardar metadatos del reporte
        ReporteGenerado reporteGenerado = ReporteGenerado.builder()
                .tipoReporte(TipoReporte.PRODUCCION)
                .nombreReporte("Reporte de Producción")
                .parametrosJson(parametros.toString()) // Convertir a JSON real si es necesario
                .fechaGeneracion(LocalDateTime.now())
                .fechaInicioDatos(parametros.fechaInicio())
                .fechaFinDatos(parametros.fechaFin())
                .rutaArchivo(rutaArchivo)
                .formato(parametros.formato())
                .generadoPor(1L) // TODO: Obtener el ID del usuario autenticado
                .build();
        ReporteGenerado savedReporte = reporteRepository.save(reporteGenerado);

        ReporteDTO reporteDTO = reporteMapper.toDTO(savedReporte);
        // Construir la URL de descarga (ej. /api/reportes/{id}/download)
        reporteDTO = new ReporteDTO(reporteDTO.id(), reporteDTO.tipoReporte(), reporteDTO.nombreReporte(), reporteDTO.fechaGeneracion(), reporteDTO.formato(), "/api/reportes/" + savedReporte.getId() + "/download");
        return reporteDTO;
    }

    @Override
    @Transactional
    public ReporteDTO generarReporteAsistencia(ParametrosReporteDTO parametros) {
        validarParametrosComunes(parametros);

        // 1. Obtener datos de asistencia
        List<Object> datosAsistencia = asistenciaService.obtenerDatosAsistenciaParaReporte(parametros.fechaInicio(), parametros.fechaFin());
        if (datosAsistencia.isEmpty()) {
            throw new DatosInsuficientesParaReporteException("No se encontraron datos de asistencia para el período especificado.");
        }

        // 2. Obtener configuración de tarifas vigente
        Optional<ConfiguracionTarifas> configuracionOpt = configuracionTarifasService.obtenerConfiguracionVigente("COP");
        ConfiguracionTarifas configuracion = configuracionOpt.orElse(null);
        
        // 3. Agregar información de tarifas a los datos del reporte
        if (configuracion != null) {
            // Crear una nueva lista mutable para evitar UnsupportedOperationException
            datosAsistencia = new ArrayList<>(datosAsistencia);
            datosAsistencia.add(configuracion); // Agregar la configuración de tarifas a los datos
        }

        // 4. Generar archivo
        String rutaArchivo;
        try {
            if (parametros.formato() == FormatoReporte.PDF) {
                rutaArchivo = generadorReporteService.generarArchivoPDF(datosAsistencia, "asistencia_template");
            } else if (parametros.formato() == FormatoReporte.EXCEL) {
                rutaArchivo = generadorReporteService.generarArchivoExcel(datosAsistencia);
            } else {
                rutaArchivo = generadorReporteService.generarArchivoCSV(datosAsistencia);
            }
        } catch (Exception e) {
            throw new ErrorGeneracionReporteException("Error al generar el archivo del reporte de asistencia: " + e.getMessage(), e);
        }

        // 5. Guardar metadatos del reporte
        ReporteGenerado reporteGenerado = ReporteGenerado.builder()
                .tipoReporte(TipoReporte.ASISTENCIA)
                .nombreReporte("Reporte de Asistencia")
                .parametrosJson(parametros.toString())
                .fechaGeneracion(LocalDateTime.now())
                .fechaInicioDatos(parametros.fechaInicio())
                .fechaFinDatos(parametros.fechaFin())
                .rutaArchivo(rutaArchivo)
                .formato(parametros.formato())
                .generadoPor(1L) // TODO: Obtener el ID del usuario autenticado
                .build();
        ReporteGenerado savedReporte = reporteRepository.save(reporteGenerado);

        ReporteDTO reporteDTO = reporteMapper.toDTO(savedReporte);
        reporteDTO = new ReporteDTO(reporteDTO.id(), reporteDTO.tipoReporte(), reporteDTO.nombreReporte(), reporteDTO.fechaGeneracion(), reporteDTO.formato(), "/api/reportes/" + savedReporte.getId() + "/download");
        return reporteDTO;
    }

    @Override
    @Transactional
    public ReporteDTO generarReporteCostosLaborales(ParametrosReporteDTO parametros) {
        validarParametrosComunes(parametros);

        // 1. Obtener datos de nómina y producción
        List<Object> datosNomina = nominaService.obtenerDatosCostosParaReporte(parametros.fechaInicio(), parametros.fechaFin());
        List<Object> datosProduccion = produccionService.obtenerDatosProduccionParaReporte(parametros.fechaInicio(), parametros.fechaFin());

        if (datosNomina.isEmpty() || datosProduccion.isEmpty()) {
            throw new DatosInsuficientesParaReporteException("No se encontraron suficientes datos de nómina o producción para generar el reporte de costos laborales.");
        }

        // 2. Obtener configuración de tarifas vigente
        Optional<ConfiguracionTarifas> configuracionOpt = configuracionTarifasService.obtenerConfiguracionVigente("COP");
        ConfiguracionTarifas configuracion = configuracionOpt.orElse(null);
        
        // 3. Agregar información de tarifas a los datos del reporte
        if (configuracion != null) {
            // Crear nuevas listas mutables para evitar UnsupportedOperationException
            datosNomina = new ArrayList<>(datosNomina);
            datosProduccion = new ArrayList<>(datosProduccion);
            // Agregar la configuración de tarifas a ambos conjuntos de datos
            datosNomina.add(configuracion);
            datosProduccion.add(configuracion);
        }

        // TODO: Consolidar y procesar datos para KPIs de costos laborales

        // 4. Generar archivo
        String rutaArchivo;
        try {
            if (parametros.formato() == FormatoReporte.PDF) {
                rutaArchivo = generadorReporteService.generarArchivoPDF(List.of(datosNomina, datosProduccion), "costos_laborales_template");
            } else if (parametros.formato() == FormatoReporte.EXCEL) {
                rutaArchivo = generadorReporteService.generarArchivoExcel(List.of(datosNomina, datosProduccion));
            } else {
                rutaArchivo = generadorReporteService.generarArchivoCSV(List.of(datosNomina, datosProduccion));
            }
        } catch (Exception e) {
            throw new ErrorGeneracionReporteException("Error al generar el archivo del reporte de costos laborales: " + e.getMessage(), e);
        }

        // 5. Guardar metadatos del reporte
        ReporteGenerado reporteGenerado = ReporteGenerado.builder()
                .tipoReporte(TipoReporte.COSTOS_LABORALES)
                .nombreReporte("Reporte de Costos Laborales")
                .parametrosJson(parametros.toString())
                .fechaGeneracion(LocalDateTime.now())
                .fechaInicioDatos(parametros.fechaInicio())
                .fechaFinDatos(parametros.fechaFin())
                .rutaArchivo(rutaArchivo)
                .formato(parametros.formato())
                .generadoPor(1L) // TODO: Obtener el ID del usuario autenticado
                .build();
        ReporteGenerado savedReporte = reporteRepository.save(reporteGenerado);

        ReporteDTO reporteDTO = reporteMapper.toDTO(savedReporte);
        reporteDTO = new ReporteDTO(reporteDTO.id(), reporteDTO.tipoReporte(), reporteDTO.nombreReporte(), reporteDTO.fechaGeneracion(), reporteDTO.formato(), "/api/reportes/" + savedReporte.getId() + "/download");
        return reporteDTO;
    }

    @Override
    @Transactional
    public ReporteDTO exportarDatosOperacionales(DatosOperacionalesDTO datos) {
        if (datos.fechaInicio().isAfter(datos.fechaFin())) {
            throw new ParametrosReporteInvalidosException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        if (datos.datasets().isEmpty()) {
            throw new ParametrosReporteInvalidosException("Debe especificar al menos un dataset para exportar.");
        }

        // 1. Recopilar datos de los datasets solicitados
        // Esta es una implementación simplificada. En un caso real, se iteraría sobre datos.datasets()
        // y se llamarían a los servicios correspondientes (empleados, turnos, etc.)
        List<Object> datosAExportar = null;
        if (datos.datasets().contains("produccion")) {
            datosAExportar = produccionService.obtenerDatosProduccionParaReporte(datos.fechaInicio(), datos.fechaFin());
        } else if (datos.datasets().contains("asistencia")) {
            datosAExportar = asistenciaService.obtenerDatosAsistenciaParaReporte(datos.fechaInicio(), datos.fechaFin());
        }
        // ... más lógica para otros datasets

        if (datosAExportar == null || datosAExportar.isEmpty()) {
            throw new DatosInsuficientesParaReporteException("No se encontraron datos para los datasets y período especificados.");
        }

        // 2. Obtener configuración de tarifas vigente
        Optional<ConfiguracionTarifas> configuracionOpt = configuracionTarifasService.obtenerConfiguracionVigente("COP");
        ConfiguracionTarifas configuracion = configuracionOpt.orElse(null);
        
        // 3. Agregar información de tarifas a los datos del reporte
        if (configuracion != null) {
            // Crear una nueva lista mutable para evitar UnsupportedOperationException
            datosAExportar = new ArrayList<>(datosAExportar);
            datosAExportar.add(configuracion); // Agregar la configuración de tarifas a los datos
        }

        // 4. Generar archivo
        String rutaArchivo;
        try {
            if (datos.formato() == FormatoReporte.CSV) {
                rutaArchivo = generadorReporteService.generarArchivoCSV(datosAExportar);
            } else if (datos.formato() == FormatoReporte.EXCEL) {
                rutaArchivo = generadorReporteService.generarArchivoExcel(datosAExportar);
            } else { // JSON
                rutaArchivo = generadorReporteService.generarArchivoPDF(datosAExportar, "json_export_template"); // Usar PDF como placeholder para JSON
            }
        } catch (Exception e) {
            throw new ErrorGeneracionReporteException("Error al generar el archivo de exportación de datos operacionales: " + e.getMessage(), e);
        }

        // 5. Guardar metadatos del reporte
        ReporteGenerado reporteGenerado = ReporteGenerado.builder()
                .tipoReporte(TipoReporte.OPERACIONAL)
                .nombreReporte("Exportación de Datos Operacionales")
                .parametrosJson(datos.toString())
                .fechaGeneracion(LocalDateTime.now())
                .fechaInicioDatos(datos.fechaInicio())
                .fechaFinDatos(datos.fechaFin())
                .rutaArchivo(rutaArchivo)
                .formato(datos.formato())
                .generadoPor(1L) // TODO: Obtener el ID del usuario autenticado
                .build();
        ReporteGenerado savedReporte = reporteRepository.save(reporteGenerado);

        ReporteDTO reporteDTO = reporteMapper.toDTO(savedReporte);
        reporteDTO = new ReporteDTO(reporteDTO.id(), reporteDTO.tipoReporte(), reporteDTO.nombreReporte(), reporteDTO.fechaGeneracion(), reporteDTO.formato(), "/api/reportes/" + savedReporte.getId() + "/download");
        return reporteDTO;
    }

    private void validarParametrosComunes(ParametrosReporteDTO parametros) {
        if (parametros.fechaInicio().isAfter(parametros.fechaFin())) {
            throw new ParametrosReporteInvalidosException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        // Aquí se podrían añadir más validaciones comunes, como el rango máximo de fechas
    }
}