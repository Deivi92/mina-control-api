package com.minacontrol.nomina.unit;

import com.minacontrol.nomina.dto.response.ComprobantePagoDTO;
import com.minacontrol.nomina.entity.ComprobantePago;
import com.minacontrol.nomina.mapper.ComprobantePagoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para ComprobantePagoMapper")
class ComprobantePagoMapperTest {

    private final ComprobantePagoMapper mapper = Mappers.getMapper(ComprobantePagoMapper.class);

    @Test
    @DisplayName("Debe mapear de Entidad a DTO correctamente")
    void should_MapEntityToDto_Correctly() {
        // Arrange
        ComprobantePago entity = new ComprobantePago();
        entity.setId(1L);
        entity.setNumeroComprobante("COMP-001");

        // Act
        ComprobantePagoDTO dto = mapper.toDTO(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.numeroComprobante()).isEqualTo(entity.getNumeroComprobante());
    }
}
