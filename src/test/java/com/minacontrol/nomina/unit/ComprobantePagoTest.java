package com.minacontrol.nomina.unit;

import com.minacontrol.nomina.entity.CalculoNomina;
import com.minacontrol.nomina.entity.ComprobantePago;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para la Entidad ComprobantePago")
class ComprobantePagoTest {

    @Test
    @DisplayName("Debe crear una instancia de ComprobantePago correctamente")
    void should_CreateInstance_Correctly() {
        // Arrange
        CalculoNomina calculo = new CalculoNomina();
        calculo.setId(1L);

        // Act
        ComprobantePago comprobante = new ComprobantePago();
        comprobante.setId(1L);
        comprobante.setCalculo(calculo);
        comprobante.setRutaArchivoPdf("/path/to/comprobante.pdf");

        // Assert
        assertThat(comprobante).isNotNull();
        assertThat(comprobante.getId()).isEqualTo(1L);
        assertThat(comprobante.getCalculo().getId()).isEqualTo(1L);
        assertThat(comprobante.getRutaArchivoPdf()).isEqualTo("/path/to/comprobante.pdf");
    }
}
