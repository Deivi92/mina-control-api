package com.minacontrol.nomina.unit;

import com.minacontrol.nomina.dto.request.AjusteNominaDTO;
import com.minacontrol.nomina.dto.request.CalcularNominaRequestDTO;
import com.minacontrol.nomina.dto.response.CalculoNominaDTO;
import com.minacontrol.nomina.dto.response.ComprobantePagoDTO;
import com.minacontrol.nomina.entity.CalculoNomina;
import com.minacontrol.nomina.entity.ComprobantePago;
import com.minacontrol.nomina.entity.PeriodoNomina;
import com.minacontrol.nomina.enums.EstadoPeriodo;
import com.minacontrol.nomina.exception.AjusteNominaNoPermitidoException;
import com.minacontrol.nomina.exception.CalculoNominaNotFoundException;
import com.minacontrol.nomina.exception.PeriodoNominaInvalidoException;
import com.minacontrol.nomina.mapper.CalculoNominaMapper;
import com.minacontrol.nomina.mapper.ComprobantePagoMapper;
import com.minacontrol.nomina.repository.CalculoNominaRepository;
import com.minacontrol.nomina.repository.ComprobantePagoRepository;
import com.minacontrol.nomina.repository.PeriodoNominaRepository;
import com.minacontrol.nomina.service.impl.NominaServiceImpl;
import com.minacontrol.produccion.service.IProduccionService;
import com.minacontrol.turnos.service.IAsistenciaService;
import com.minacontrol.empleado.service.IEmpleadoService; // Added this import
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para NominaService")
class NominaServiceTest {

    @Mock
    private PeriodoNominaRepository periodoNominaRepository;
    @Mock
    private CalculoNominaRepository calculoNominaRepository;
    @Mock
    private ComprobantePagoRepository comprobantePagoRepository;
    @Mock
    private IAsistenciaService asistenciaService;
    @Mock
    private IProduccionService produccionService;
    @Mock
    private IEmpleadoService empleadoService; // Added this mock
    @Mock
    private CalculoNominaMapper calculoNominaMapper;
    @Mock
    private ComprobantePagoMapper comprobantePagoMapper;

    @InjectMocks
    private NominaServiceImpl nominaService;

    private PeriodoNomina periodoNomina;
    private CalculoNomina calculoNomina;

    @BeforeEach
    void setUp() {
        periodoNomina = new PeriodoNomina();
        periodoNomina.setId(1L);
        periodoNomina.setFechaInicio(LocalDate.now().minusDays(7));
        periodoNomina.setFechaFin(LocalDate.now());
        periodoNomina.setEstado(EstadoPeriodo.ABIERTO);

        calculoNomina = new CalculoNomina();
        calculoNomina.setId(1L);
        calculoNomina.setPeriodo(periodoNomina);
        calculoNomina.setEmpleadoId(1L);
        calculoNomina.setSalarioBase(BigDecimal.ZERO);
        calculoNomina.setBonificaciones(BigDecimal.ZERO);
        calculoNomina.setDeducciones(BigDecimal.ZERO);
        calculoNomina.setTotalBruto(new BigDecimal("1000"));
        calculoNomina.setTotalNeto(new BigDecimal("1000"));
    }

    @Nested
    @DisplayName("CU-NOM-001: Calcular Nómina Semanal")
    class CalcularNominaSemanalTests {

        @Test
        @DisplayName("Debe calcular la nómina para un período válido y abierto")
        void should_CalcularNomina_When_PeriodoValido() {
            // Arrange
            CalcularNominaRequestDTO request = new CalcularNominaRequestDTO(1L);
            when(periodoNominaRepository.findById(1L)).thenReturn(Optional.of(periodoNomina));
            // Simular servicios de asistencia y producción
            when(asistenciaService.obtenerHorasTrabajadasPorPeriodo(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(Collections.singletonMap(1L, new BigDecimal("40")));
            when(produccionService.obtenerProduccionPorPeriodo(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(Collections.singletonMap(1L, new BigDecimal("5")));
            when(empleadoService.obtenerIdsEmpleadosActivos()).thenReturn(List.of(1L)); // Mock para el nuevo método
            when(calculoNominaRepository.save(any(CalculoNomina.class))).thenReturn(calculoNomina);

            // Act
            nominaService.calcularNominaSemanal(request);

            // Assert
            verify(calculoNominaRepository, atLeastOnce()).save(any(CalculoNomina.class));
            assertThat(periodoNomina.getEstado()).isEqualTo(EstadoPeriodo.CALCULADO);
        }

        @Test
        @DisplayName("Debe lanzar PeriodoNominaInvalidoException si el período no está abierto")
        void should_ThrowException_When_PeriodoNoAbierto() {
            // Arrange
            periodoNomina.setEstado(EstadoPeriodo.CALCULADO);
            CalcularNominaRequestDTO request = new CalcularNominaRequestDTO(1L);
            when(periodoNominaRepository.findById(1L)).thenReturn(Optional.of(periodoNomina));

            // Act & Assert
            assertThatThrownBy(() -> nominaService.calcularNominaSemanal(request))
                    .isInstanceOf(PeriodoNominaInvalidoException.class);
        }
    }

    @Nested
    @DisplayName("CU-NOM-002: Ajustar Cálculo de Nómina")
    class AjustarCalculoNominaTests {

        @Test
        @DisplayName("Debe ajustar un cálculo de nómina existente")
        void should_AjustarCalculo_When_CalculoExiste() {
            // Arrange
            AjusteNominaDTO ajusteDTO = new AjusteNominaDTO("Bono Extra", new BigDecimal("100"), false, "Buen desempeño");
            periodoNomina.setEstado(EstadoPeriodo.CALCULADO); // Estado correcto para ajustar
            when(calculoNominaRepository.findById(1L)).thenReturn(Optional.of(calculoNomina));
            when(calculoNominaRepository.save(any(CalculoNomina.class))).thenReturn(calculoNomina);
            when(calculoNominaMapper.toDTO(any(CalculoNomina.class))).thenReturn(new CalculoNominaDTO(1L, 1L, new BigDecimal("1100")));

            // Act
            CalculoNominaDTO result = nominaService.ajustarCalculoNomina(1L, ajusteDTO);

            // Assert
            assertThat(result).isNotNull();
            verify(calculoNominaRepository).save(calculoNomina);
            assertThat(calculoNomina.getObservaciones()).contains("Buen desempeño");
        }

        @Test
        @DisplayName("Debe lanzar CalculoNominaNotFoundException si el cálculo no existe")
        void should_ThrowException_When_CalculoNoExiste() {
            // Arrange
            AjusteNominaDTO ajusteDTO = new AjusteNominaDTO("Bono", new BigDecimal("50"), false, "test");
            when(calculoNominaRepository.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> nominaService.ajustarCalculoNomina(99L, ajusteDTO))
                    .isInstanceOf(CalculoNominaNotFoundException.class);
        }

        @Test
        @DisplayName("Debe lanzar AjusteNominaNoPermitidoException si el período está cerrado")
        void should_ThrowException_When_PeriodoCerrado() {
            // Arrange
            AjusteNominaDTO ajusteDTO = new AjusteNominaDTO("Bono", new BigDecimal("50"), false, "test");
            periodoNomina.setEstado(EstadoPeriodo.PAGADO);
            when(calculoNominaRepository.findById(1L)).thenReturn(Optional.of(calculoNomina));

            // Act & Assert
            assertThatThrownBy(() -> nominaService.ajustarCalculoNomina(1L, ajusteDTO))
                    .isInstanceOf(AjusteNominaNoPermitidoException.class);
        }
    }

    @Nested
    @DisplayName("CU-NOM-003: Generar Comprobantes de Pago")
    class GenerarComprobantesPagoTests {

        @Test
        @DisplayName("Debe generar comprobantes para un período calculado")
        void should_GenerarComprobantes_When_PeriodoCalculado() {
            // Arrange
            periodoNomina.setEstado(EstadoPeriodo.CALCULADO);
            when(periodoNominaRepository.findById(1L)).thenReturn(Optional.of(periodoNomina));
            when(calculoNominaRepository.findByPeriodoId(1L)).thenReturn(List.of(calculoNomina));
            when(comprobantePagoRepository.save(any(ComprobantePago.class))).thenReturn(new ComprobantePago());
            when(comprobantePagoMapper.toDTO(any(ComprobantePago.class))).thenReturn(new ComprobantePagoDTO(1L, "COMP-001"));

            // Act
            List<ComprobantePagoDTO> result = nominaService.generarComprobantesPago(1L);

            // Assert
            assertThat(result).hasSize(1);
            verify(comprobantePagoRepository).save(any(ComprobantePago.class));
            assertThat(periodoNomina.getEstado()).isEqualTo(EstadoPeriodo.PAGADO);
        }

        @Test
        @DisplayName("Debe lanzar PeriodoNominaInvalidoException si el período no está calculado")
        void should_ThrowException_When_PeriodoNoCalculado() {
            // Arrange
            periodoNomina.setEstado(EstadoPeriodo.ABIERTO);
            when(periodoNominaRepository.findById(1L)).thenReturn(Optional.of(periodoNomina));

            // Act & Assert
            assertThatThrownBy(() -> nominaService.generarComprobantesPago(1L))
                    .isInstanceOf(PeriodoNominaInvalidoException.class);
        }
    }

    @Nested
    @DisplayName("CU-NOM-004: Consultar Historial de Pagos")
    class ConsultarHistorialPagosTests {

        @Test
        @DisplayName("Debe devolver el historial de pagos para un empleado")
        void should_ReturnHistorialPagos_For_Empleado() {
            // Arrange
            when(calculoNominaRepository.findByEmpleadoIdAndFechas(anyLong(), any(), any())).thenReturn(List.of(calculoNomina));
            when(calculoNominaMapper.toDTO(any(CalculoNomina.class))).thenReturn(new CalculoNominaDTO(1L, 1L, new BigDecimal("1000")));

            // Act
            List<CalculoNominaDTO> result = nominaService.consultarHistorialPagos(1L, LocalDate.now().minusMonths(1), LocalDate.now());

            // Assert
            assertThat(result).hasSize(1);
            verify(calculoNominaRepository).findByEmpleadoIdAndFechas(anyLong(), any(), any());
        }

        @Test
        @DisplayName("Debe devolver una lista vacía si no hay historial")
        void should_ReturnEmptyList_When_NoHistorial() {
            // Arrange
            when(calculoNominaRepository.findByEmpleadoIdAndFechas(anyLong(), any(), any())).thenReturn(Collections.emptyList());

            // Act
            List<CalculoNominaDTO> result = nominaService.consultarHistorialPagos(1L, LocalDate.now().minusMonths(1), LocalDate.now());

            // Assert
            assertThat(result).isEmpty();
        }
    }
}
