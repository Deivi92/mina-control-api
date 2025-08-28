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
@DisplayName("Pruebas Unitarias para ConfiguracionTarifasService")
class ConfiguracionTarifasServiceTest {

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
                .tarifaPorHora(new BigDecimal("5931.25"))
                .bonoPorTonelada(new BigDecimal("11500.00"))
                .moneda("COP")
                .fechaVigencia(LocalDate.now())
                .creadoPor(1L)
                .fechaCreacion(LocalDate.now())
                .build();
    }

    @Nested
    @DisplayName("CU-NOM-XXX: Guardar Configuración de Tarifas")
    class GuardarConfiguracionTests {

        @Test
        @DisplayName("Debe guardar una configuración de tarifas y registrar en el historial")
        void should_GuardarConfiguracion_When_ValidData() {
            // Arrange
            when(configuracionTarifasRepository.save(any(ConfiguracionTarifas.class))).thenReturn(configuracionTarifas);

            // Act
            ConfiguracionTarifas result = configuracionTarifasService.guardarConfiguracion(configuracionTarifas, 1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getTarifaPorHora()).isEqualTo(new BigDecimal("5931.25"));
            verify(configuracionTarifasRepository).save(any(ConfiguracionTarifas.class));
            verify(historialRepository).save(any(HistorialConfiguracionTarifas.class));
        }
    }

    @Nested
    @DisplayName("CU-NOM-XXX: Obtener Configuración Vigente")
    class ObtenerConfiguracionVigenteTests {

        @Test
        @DisplayName("Debe obtener la configuración vigente para una moneda")
        void should_ObtenerConfiguracionVigente_When_MonedaExists() {
            // Arrange
            when(configuracionTarifasRepository.findConfiguracionVigente(any(LocalDate.class), any(String.class)))
                    .thenReturn(Optional.of(configuracionTarifas));

            // Act
            Optional<ConfiguracionTarifas> result = configuracionTarifasService.obtenerConfiguracionVigente("COP");

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getTarifaPorHora()).isEqualTo(new BigDecimal("5931.25"));
            verify(configuracionTarifasRepository).findConfiguracionVigente(any(LocalDate.class), any(String.class));
        }

        @Test
        @DisplayName("Debe devolver Optional vacío si no hay configuración vigente")
        void should_ReturnEmptyOptional_When_NoConfiguracionVigente() {
            // Arrange
            when(configuracionTarifasRepository.findConfiguracionVigente(any(LocalDate.class), any(String.class)))
                    .thenReturn(Optional.empty());

            // Act
            Optional<ConfiguracionTarifas> result = configuracionTarifasService.obtenerConfiguracionVigente("COP");

            // Assert
            assertThat(result).isEmpty();
            verify(configuracionTarifasRepository).findConfiguracionVigente(any(LocalDate.class), any(String.class));
        }
    }

    @Nested
    @DisplayName("CU-NOM-XXX: Obtener Historial de Configuración de Tarifas")
    class ObtenerHistorialTests {

        @Test
        @DisplayName("Debe obtener el historial de configuración de tarifas ordenado por fecha")
        void should_ObtenerHistorial_When_SolicitarHistorial() {
            // Arrange
            HistorialConfiguracionTarifas historial1 = HistorialConfiguracionTarifas.builder()
                    .id(1L)
                    .tarifaPorHora(new BigDecimal("5931.25"))
                    .bonoPorTonelada(new BigDecimal("11500.00"))
                    .moneda("COP")
                    .fechaVigencia(LocalDate.now())
                    .creadoPor(1L)
                    .fechaCreacion(LocalDateTime.now())
                    .accion("CREADO")
                    .build();

            HistorialConfiguracionTarifas historial2 = HistorialConfiguracionTarifas.builder()
                    .id(2L)
                    .tarifaPorHora(new BigDecimal("6000.00"))
                    .bonoPorTonelada(new BigDecimal("12000.00"))
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

    @Nested
    @DisplayName("CU-NOM-XXX: Obtener Configuración por ID")
    class ObtenerConfiguracionPorIdTests {

        @Test
        @DisplayName("Debe obtener una configuración de tarifas por su ID")
        void should_ObtenerConfiguracionPorId_When_IdExists() {
            // Arrange
            when(configuracionTarifasRepository.findById(1L)).thenReturn(Optional.of(configuracionTarifas));

            // Act
            Optional<ConfiguracionTarifas> result = configuracionTarifasService.obtenerConfiguracionPorId(1L);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
            verify(configuracionTarifasRepository).findById(1L);
        }

        @Test
        @DisplayName("Debe devolver Optional vacío si no hay configuración con el ID")
        void should_ReturnEmptyOptional_When_IdNotExists() {
            // Arrange
            when(configuracionTarifasRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Optional<ConfiguracionTarifas> result = configuracionTarifasService.obtenerConfiguracionPorId(999L);

            // Assert
            assertThat(result).isEmpty();
            verify(configuracionTarifasRepository).findById(999L);
        }
    }

    @Nested
    @DisplayName("CU-NOM-XXX: Actualizar Configuración de Tarifas")
    class ActualizarConfiguracionTests {

        @Test
        @DisplayName("Debe actualizar una configuración de tarifas y registrar en el historial")
        void should_ActualizarConfiguracion_When_ValidData() {
            // Arrange
            LocalDate fechaCreacion = LocalDate.now();
            ConfiguracionTarifas configuracionExistente = ConfiguracionTarifas.builder()
                    .id(1L)
                    .tarifaPorHora(new BigDecimal("5931.25"))
                    .bonoPorTonelada(new BigDecimal("11500.00"))
                    .moneda("COP")
                    .fechaVigencia(LocalDate.now())
                    .creadoPor(1L)
                    .fechaCreacion(fechaCreacion)
                    .build();
                    
            ConfiguracionTarifas configuracionParaActualizar = ConfiguracionTarifas.builder()
                    .id(1L)
                    .tarifaPorHora(new BigDecimal("6000.00"))
                    .bonoPorTonelada(new BigDecimal("12000.00"))
                    .moneda("COP")
                    .fechaVigencia(LocalDate.now())
                    .creadoPor(1L)
                    .build();
                    
            ConfiguracionTarifas configuracionActualizada = ConfiguracionTarifas.builder()
                    .id(1L)
                    .tarifaPorHora(new BigDecimal("6000.00"))
                    .bonoPorTonelada(new BigDecimal("12000.00"))
                    .moneda("COP")
                    .fechaVigencia(LocalDate.now())
                    .creadoPor(1L)
                    .fechaCreacion(fechaCreacion) // Mantener la fecha de creación original
                    .build();
                    
            when(configuracionTarifasRepository.findById(1L)).thenReturn(Optional.of(configuracionExistente));
            when(configuracionTarifasRepository.save(any(ConfiguracionTarifas.class))).thenReturn(configuracionActualizada);

            // Act
            ConfiguracionTarifas result = configuracionTarifasService.actualizarConfiguracion(configuracionParaActualizar, 1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getTarifaPorHora()).isEqualTo(new BigDecimal("6000.00"));
            // Verificar que los campos de auditoría se mantuvieron
            assertThat(result.getCreadoPor()).isEqualTo(1L);
            assertThat(result.getFechaCreacion()).isEqualTo(fechaCreacion);
            verify(configuracionTarifasRepository).findById(1L);
            verify(configuracionTarifasRepository).save(any(ConfiguracionTarifas.class));
            verify(historialRepository).save(any(HistorialConfiguracionTarifas.class));
        }
    }
}