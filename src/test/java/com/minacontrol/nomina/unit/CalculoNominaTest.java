package com.minacontrol.nomina.unit;

import com.minacontrol.nomina.entity.CalculoNomina;
import com.minacontrol.nomina.entity.PeriodoNomina;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para la Entidad CalculoNomina")
class CalculoNominaTest {

    @Test
    @DisplayName("Debe crear una instancia de CalculoNomina correctamente")
    void should_CreateInstance_Correctly() {
        // Arrange
        PeriodoNomina periodo = new PeriodoNomina();
        periodo.setId(1L);

        // Act
        CalculoNomina calculo = new CalculoNomina();
        calculo.setId(1L);
        calculo.setPeriodo(periodo);
        calculo.setEmpleadoId(10L);
        calculo.setTotalNeto(new BigDecimal("1250.75"));

        // Assert
        assertThat(calculo).isNotNull();
        assertThat(calculo.getId()).isEqualTo(1L);
        assertThat(calculo.getPeriodo().getId()).isEqualTo(1L);
        assertThat(calculo.getEmpleadoId()).isEqualTo(10L);
        assertThat(calculo.getTotalNeto()).isEqualTo(new BigDecimal("1250.75"));
    }
}