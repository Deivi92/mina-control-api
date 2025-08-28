package com.minacontrol.nomina.unit;

import com.minacontrol.nomina.entity.ConfiguracionTarifas;
import com.minacontrol.nomina.entity.HistorialConfiguracionTarifas;
import com.minacontrol.nomina.repository.ConfiguracionTarifasRepository;
import com.minacontrol.nomina.repository.HistorialConfiguracionTarifasRepository;
import com.minacontrol.nomina.service.impl.ConfiguracionTarifasServiceImpl;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para ConfiguracionTarifasService - Historial")
class ConfiguracionTarifasServiceHistorialTest {

    @Mock
    private ConfiguracionTarifasRepository configuracionTarifasRepository;

    @Mock
    private HistorialConfiguracionTarifasRepository historialRepository;

    @InjectMocks
    private ConfiguracionTarifasServiceImpl configuracionTarifasService;

    private ConfiguracionTarifas configuracionTarifas;

    @BeforeEach
    void setUp() {
        configuracionTarifas = ConfiguracionTarifas.builder()
                .id(1L)
                .tarifaPorHora(new BigDecimal("6823.00"))
                .bonoPorTonelada(new BigDecimal("8000.00"))
                .moneda("COP")
                .fechaVigencia(LocalDate.now())
                .creadoPor(1L)
                .fechaCreacion(LocalDate.now())
                .build();
    }

    @Nested
    @DisplayName("CU-NOM-XXX: Registrar Cambio en Configuración de Tarifas")
    class RegistrarCambioEnConfiguracionTests {

        @Test
        @DisplayName("Debe registrar un cambio en el historial al guardar una configuración")
        void should_RegistrarCambioEnHistorial_When_GuardarConfiguracion() {
            // Arrange
            when(configuracionTarifasRepository.save(any(ConfiguracionTarifas.class))).thenReturn(configuracionTarifas);

            // Act
            ConfiguracionTarifas result = configuracionTarifasService.guardarConfiguracion(configuracionTarifas, 1L);

            // Assert
            assertThat(result).isNotNull();
            verify(configuracionTarifasRepository).save(any(ConfiguracionTarifas.class));
            verify(historialRepository).save(any(HistorialConfiguracionTarifas.class));
        }
    }

    @Nested
    @DisplayName("CU-NOM-XXX: Obtener Historial de Configuración de Tarifas")
    class ObtenerHistorialDeConfiguracionTests {

        @Test
        @DisplayName("Debe obtener el historial de configuración de tarifas ordenado por fecha")
        void should_ObtenerHistorial_When_SolicitarHistorial() {
            // Arrange
            HistorialConfiguracionTarifas historial1 = HistorialConfiguracionTarifas.builder()
                    .id(1L)
                    .tarifaPorHora(new BigDecimal("6823.00"))
                    .bonoPorTonelada(new BigDecimal("8000.00"))
                    .moneda("COP")
                    .fechaVigencia(LocalDate.now())
                    .creadoPor(1L)
                    .fechaCreacion(LocalDateTime.now())
                    .accion("CREADO")
                    .build();

            HistorialConfiguracionTarifas historial2 = HistorialConfiguracionTarifas.builder()
                    .id(2L)
                    .tarifaPorHora(new BigDecimal("7000.00"))
                    .bonoPorTonelada(new BigDecimal("8500.00"))
                    .moneda("COP")
                    .fechaVigencia(LocalDate.now().plusDays(1))
                    .creadoPor(1L)
                    .fechaCreacion(LocalDateTime.now().plusMinutes(1))
                    .accion("ACTUALIZADO")
                    .build();

            when(historialRepository.findByOrderByFechaCreacionDesc()).thenReturn(List.of(historial2, historial1));

            // Act
            List<HistorialConfiguracionTarifas> result = configuracionTarifasService.obtenerHistorial();

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(2L); // El más reciente primero
            verify(historialRepository).findByOrderByFechaCreacionDesc();
        }
    }
}