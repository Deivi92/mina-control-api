package com.minacontrol.reportes.unit;

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
import com.minacontrol.reportes.service.impl.ReporteServiceImpl;
import com.minacontrol.shared.service.GeneradorReporteService;
import com.minacontrol.produccion.service.IProduccionService;
import com.minacontrol.turnos.service.IAsistenciaService;
import com.minacontrol.nomina.service.INominaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Añadido para manejar UnnecessaryStubbingException
@DisplayName("Pruebas Unitarias para ReporteService")
class ReporteServiceTest {

    @Mock private ReporteGeneradoRepository reporteRepository;
    @Mock private IProduccionService produccionService;
    @Mock private IAsistenciaService asistenciaService;
    @Mock private INominaService nominaService;
    @Mock private GeneradorReporteService generadorReporteService;
    @Mock private ReporteMapper reporteMapper;

    @InjectMocks
    private ReporteServiceImpl reporteService;

    private ParametrosReporteDTO parametrosReporte;

    @BeforeEach
    void setUp() {
        parametrosReporte = new ParametrosReporteDTO(
                TipoReporte.PRODUCCION,
                LocalDate.now().minusDays(7),
                LocalDate.now(),
                FormatoReporte.PDF,
                null, null, true, null
        );

        // Mockear el mapper para que devuelva un DTO válido con el tipo de reporte correcto
        when(reporteMapper.toDTO(any(ReporteGenerado.class))).thenAnswer(invocation -> {
            ReporteGenerado rg = invocation.getArgument(0);
            return new ReporteDTO(rg.getId(), rg.getTipoReporte().name(), rg.getNombreReporte(), rg.getFechaGeneracion(), rg.getFormato().name(), "");
        });
    }

    @Nested
    @DisplayName("CU-REP-001: Generar Reporte de Producción")
    class GenerarReporteProduccionTests {

        @Test
        @DisplayName("Debe generar un reporte de producción exitosamente")
        void should_GenerarReporteProduccion_When_ValidData() {
            // Arrange
            when(produccionService.obtenerDatosProduccionParaReporte(any(), any())).thenReturn(List.of("dato1", "dato2"));
            when(generadorReporteService.generarArchivoPDF(any(), anyString())).thenReturn("/path/to/report.pdf");
            when(reporteRepository.save(any(ReporteGenerado.class))).thenAnswer(invocation -> {
                ReporteGenerado rg = invocation.getArgument(0);
                rg.setId(1L);
                return rg;
            });

            // Act
            ReporteDTO result = reporteService.generarReporteProduccion(parametrosReporte);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.tipoReporte()).isEqualTo(TipoReporte.PRODUCCION.name());
            assertThat(result.urlDescarga()).isEqualTo("/api/reportes/1/download");
            verify(produccionService).obtenerDatosProduccionParaReporte(any(), any());
            verify(generadorReporteService).generarArchivoPDF(any(), anyString());
            verify(reporteRepository).save(any(ReporteGenerado.class));
        }
    }

    @Nested
    @DisplayName("CU-REP-002: Generar Reporte de Asistencia")
    class GenerarReporteAsistenciaTests {

        @Test
        @DisplayName("Debe generar un reporte de asistencia exitosamente")
        void should_GenerarReporteAsistencia_When_ValidData() {
            // Arrange
            parametrosReporte = new ParametrosReporteDTO(TipoReporte.ASISTENCIA, LocalDate.now().minusDays(7), LocalDate.now(), FormatoReporte.EXCEL, null, null, true, null);
            when(asistenciaService.obtenerDatosAsistenciaParaReporte(any(), any())).thenReturn(List.of("dato1", "dato2"));
            when(generadorReporteService.generarArchivoExcel(any())).thenReturn("/path/to/report.xlsx");
            when(reporteRepository.save(any(ReporteGenerado.class))).thenAnswer(invocation -> {
                ReporteGenerado rg = invocation.getArgument(0);
                rg.setId(1L);
                return rg;
            });

            // Act
            ReporteDTO result = reporteService.generarReporteAsistencia(parametrosReporte);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.tipoReporte()).isEqualTo(TipoReporte.ASISTENCIA.name());
            assertThat(result.formato()).isEqualTo(FormatoReporte.EXCEL.name());
            assertThat(result.urlDescarga()).isEqualTo("/api/reportes/1/download");
            verify(asistenciaService).obtenerDatosAsistenciaParaReporte(any(), any());
            verify(generadorReporteService).generarArchivoExcel(any());
            verify(reporteRepository).save(any(ReporteGenerado.class));
        }
    }

    @Nested
    @DisplayName("CU-REP-003: Generar Reporte de Costos Laborales")
    class GenerarReporteCostosLaboralesTests {
        @Test
        @DisplayName("Debe generar un reporte de costos laborales exitosamente")
        void should_GenerarReporteCostos_When_ValidData() {
            // Arrange
            parametrosReporte = new ParametrosReporteDTO(TipoReporte.COSTOS_LABORALES, LocalDate.now().minusDays(7), LocalDate.now(), FormatoReporte.PDF, null, null, true, null);
            when(nominaService.obtenerDatosCostosParaReporte(any(), any())).thenReturn(List.of("dato1"));
            when(produccionService.obtenerDatosProduccionParaReporte(any(), any())).thenReturn(List.of("dato2"));
            when(generadorReporteService.generarArchivoPDF(any(), anyString())).thenReturn("/path/to/costos.pdf");
            when(reporteRepository.save(any(ReporteGenerado.class))).thenAnswer(invocation -> {
                ReporteGenerado rg = invocation.getArgument(0);
                rg.setId(1L);
                return rg;
            });

            // Act
            ReporteDTO result = reporteService.generarReporteCostosLaborales(parametrosReporte);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.tipoReporte()).isEqualTo(TipoReporte.COSTOS_LABORALES.name());
            assertThat(result.urlDescarga()).isEqualTo("/api/reportes/1/download");
            verify(nominaService).obtenerDatosCostosParaReporte(any(), any());
            verify(produccionService).obtenerDatosProduccionParaReporte(any(), any());
            verify(generadorReporteService).generarArchivoPDF(any(), anyString());
            verify(reporteRepository).save(any(ReporteGenerado.class));
        }
    }

    @Nested
    @DisplayName("CU-REP-004: Exportar Datos Operacionales")
    class ExportarDatosOperacionalesTests {
        @Test
        @DisplayName("Debe exportar datos operacionales en formato CSV exitosamente")
        void should_ExportarDatos_When_ValidData() {
            // Arrange
            DatosOperacionalesDTO exportDTO = new DatosOperacionalesDTO(List.of("produccion"), FormatoReporte.CSV, LocalDate.now().minusDays(7), LocalDate.now(), null);
            when(produccionService.obtenerDatosProduccionParaReporte(any(), any())).thenReturn(List.of("data"));
            when(generadorReporteService.generarArchivoCSV(any())).thenReturn("/path/to/export.csv");
            when(reporteRepository.save(any(ReporteGenerado.class))).thenAnswer(invocation -> {
                ReporteGenerado rg = invocation.getArgument(0);
                rg.setId(1L);
                return rg;
            });

            // Act
            ReporteDTO result = reporteService.exportarDatosOperacionales(exportDTO);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.tipoReporte()).isEqualTo(TipoReporte.OPERACIONAL.name());
            assertThat(result.formato()).isEqualTo(FormatoReporte.CSV.name());
            assertThat(result.urlDescarga()).isEqualTo("/api/reportes/1/download");
            verify(reporteRepository).save(any(ReporteGenerado.class));
        }
    }

    @Nested
    @DisplayName("Validaciones y Excepciones")
    class ValidacionesYExcepcionesTests {

        @Test
        @DisplayName("Debe lanzar ParametrosReporteInvalidosException si fechaInicio es posterior a fechaFin")
        void should_ThrowException_When_StartDateIsAfterEndDate() {
            // Arrange
            parametrosReporte = new ParametrosReporteDTO(TipoReporte.PRODUCCION, LocalDate.now(), LocalDate.now().minusDays(1), FormatoReporte.PDF, null, null, false, null);

            // Act & Assert
            assertThatThrownBy(() -> reporteService.generarReporteProduccion(parametrosReporte))
                    .isInstanceOf(ParametrosReporteInvalidosException.class)
                    .hasMessageContaining("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        @Test
        @DisplayName("Debe lanzar ErrorGeneracionReporteException si el generador de archivos falla")
        void should_ThrowException_When_FileGeneratorFails() {
            // Arrange
            when(produccionService.obtenerDatosProduccionParaReporte(any(), any())).thenReturn(List.of("dato"));
            when(generadorReporteService.generarArchivoPDF(any(), anyString())).thenThrow(new RuntimeException("Error de disco"));

            // Act & Assert
            assertThatThrownBy(() -> reporteService.generarReporteProduccion(parametrosReporte))
                    .isInstanceOf(ErrorGeneracionReporteException.class)
                    .hasMessageContaining("Error al generar el archivo del reporte de producción");
        }

        @Test
        @DisplayName("Debe lanzar DatosInsuficientesParaReporteException si no hay datos de nómina para reporte de costos")
        void should_ThrowException_When_NoNominaDataForCostosReport() {
            // Arrange
            parametrosReporte = new ParametrosReporteDTO(TipoReporte.COSTOS_LABORALES, LocalDate.now().minusDays(7), LocalDate.now(), FormatoReporte.PDF, null, null, true, null);
            when(nominaService.obtenerDatosCostosParaReporte(any(), any())).thenReturn(Collections.emptyList());
            when(produccionService.obtenerDatosProduccionParaReporte(any(), any())).thenReturn(List.of("dato"));

            // Act & Assert
            assertThatThrownBy(() -> reporteService.generarReporteCostosLaborales(parametrosReporte))
                    .isInstanceOf(DatosInsuficientesParaReporteException.class)
                    .hasMessageContaining("No se encontraron suficientes datos de nómina o producción para generar el reporte de costos laborales.");
        }

        @Test
        @DisplayName("Debe lanzar ParametrosReporteInvalidosException si no se especifica ningún dataset para exportar")
        void should_ThrowException_When_NoDatasetForExport() {
            // Arrange
            DatosOperacionalesDTO exportDTO = new DatosOperacionalesDTO(Collections.emptyList(), FormatoReporte.CSV, LocalDate.now().minusDays(7), LocalDate.now(), null);

            // Act & Assert
            assertThatThrownBy(() -> reporteService.exportarDatosOperacionales(exportDTO))
                    .isInstanceOf(ParametrosReporteInvalidosException.class)
                    .hasMessageContaining("Debe especificar al menos un dataset para exportar.");
        }

        @Test
        @DisplayName("Debe lanzar DatosInsuficientesParaReporteException si no hay datos para el dataset especificado en la exportación")
        void should_ThrowException_When_NoDataForSpecifiedDatasetInExport() {
            // Arrange
            DatosOperacionalesDTO exportDTO = new DatosOperacionalesDTO(List.of("produccion"), FormatoReporte.CSV, LocalDate.now().minusDays(7), LocalDate.now(), null);
            when(produccionService.obtenerDatosProduccionParaReporte(any(), any())).thenReturn(Collections.emptyList());

            // Act & Assert
            assertThatThrownBy(() -> reporteService.exportarDatosOperacionales(exportDTO))
                    .isInstanceOf(DatosInsuficientesParaReporteException.class)
                    .hasMessageContaining("No se encontraron datos para los datasets y período especificados.");
        }
    }
}
