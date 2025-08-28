package com.minacontrol.nomina.unit;

import com.minacontrol.nomina.entity.ConfiguracionTarifas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para ConfiguracionTarifas")
class ConfiguracionTarifasTest {

    private ConfiguracionTarifas configuracionTarifas;

    @BeforeEach
    void setUp() {
        configuracionTarifas = ConfiguracionTarifas.builder()
                .tarifaPorHora(new BigDecimal("6823.00"))
                .bonoPorTonelada(new BigDecimal("8400.00"))
                .moneda("COP")
                .fechaVigencia(LocalDate.now())
                .creadoPor(1L)
                .build();
    }

    @Nested
    @DisplayName("Validaciones de Campos")
    class ValidacionesCamposTests {

        @Test
        @DisplayName("Debe crear una configuración de tarifas con valores válidos")
        void should_CrearConfiguracion_When_ValoresValidos() {
            // Assert
            assertThat(configuracionTarifas).isNotNull();
            assertThat(configuracionTarifas.getTarifaPorHora()).isEqualTo(new BigDecimal("6823.00"));
            assertThat(configuracionTarifas.getBonoPorTonelada()).isEqualTo(new BigDecimal("8400.00"));
            assertThat(configuracionTarifas.getMoneda()).isEqualTo("COP");
            assertThat(configuracionTarifas.getFechaVigencia()).isNotNull();
            assertThat(configuracionTarifas.getCreadoPor()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Debe permitir tarifas con decimales")
        void should_PermitirDecimales_When_ConfigurarTarifas() {
            // Arrange
            BigDecimal tarifaConDecimales = new BigDecimal("6823.50");
            BigDecimal bonoConDecimales = new BigDecimal("8400.75");

            // Act
            configuracionTarifas.setTarifaPorHora(tarifaConDecimales);
            configuracionTarifas.setBonoPorTonelada(bonoConDecimales);

            // Assert
            assertThat(configuracionTarifas.getTarifaPorHora()).isEqualTo(tarifaConDecimales);
            assertThat(configuracionTarifas.getBonoPorTonelada()).isEqualTo(bonoConDecimales);
        }

        @Test
        @DisplayName("Debe permitir diferentes monedas")
        void should_PermitirDiferentesMonedas_When_Configurar() {
            // Act
            configuracionTarifas.setMoneda("USD");

            // Assert
            assertThat(configuracionTarifas.getMoneda()).isEqualTo("USD");
        }
    }

    @Nested
    @DisplayName("Cálculos y Operaciones")
    class CalculosOperacionesTests {

        @Test
        @DisplayName("Debe calcular correctamente el salario diario")
        void should_CalcularSalarioDiario_When_TarifaPorHoraDefinida() {
            // Arrange
            BigDecimal horasDiarias = new BigDecimal("8");
            BigDecimal salarioDiarioEsperado = new BigDecimal("6823.00").multiply(horasDiarias);

            // Act
            BigDecimal salarioDiario = configuracionTarifas.getTarifaPorHora().multiply(horasDiarias);

            // Assert
            assertThat(salarioDiario).isEqualByComparingTo(salarioDiarioEsperado);
        }

        @Test
        @DisplayName("Debe calcular correctamente el bono por producción")
        void should_CalcularBonoProduccion_When_ProduccionYBonoDefinidos() {
            // Arrange
            BigDecimal toneladas = new BigDecimal("5");
            BigDecimal bonoEsperado = new BigDecimal("8400.00").multiply(toneladas);

            // Act
            BigDecimal bonoCalculado = configuracionTarifas.getBonoPorTonelada().multiply(toneladas);

            // Assert
            assertThat(bonoCalculado).isEqualByComparingTo(bonoEsperado);
        }
    }
}