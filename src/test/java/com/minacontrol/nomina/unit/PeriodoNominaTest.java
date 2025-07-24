package com.minacontrol.nomina.unit;

import com.minacontrol.nomina.entity.PeriodoNomina;
import com.minacontrol.nomina.enums.EstadoPeriodo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para la Entidad PeriodoNomina")
class PeriodoNominaTest {

    @Test
    @DisplayName("Debe crear una instancia de PeriodoNomina correctamente")
    void should_CreateInstance_Correctly() {
        // Arrange
        LocalDate inicio = LocalDate.of(2025, 7, 21);
        LocalDate fin = LocalDate.of(2025, 7, 27);

        // Act
        PeriodoNomina periodo = new PeriodoNomina();
        periodo.setId(1L);
        periodo.setFechaInicio(inicio);
        periodo.setFechaFin(fin);
        periodo.setEstado(EstadoPeriodo.ABIERTO);

        // Assert
        assertThat(periodo).isNotNull();
        assertThat(periodo.getId()).isEqualTo(1L);
        assertThat(periodo.getFechaInicio()).isEqualTo(inicio);
        assertThat(periodo.getFechaFin()).isEqualTo(fin);
        assertThat(periodo.getEstado()).isEqualTo(EstadoPeriodo.ABIERTO);
    }
}
