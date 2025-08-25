package com.minacontrol.nomina.unit;

import com.minacontrol.nomina.dto.response.CalculoNominaDTO;
import com.minacontrol.nomina.entity.CalculoNomina;
import com.minacontrol.nomina.mapper.CalculoNominaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para CalculoNominaMapper")
class CalculoNominaMapperTest {

    private final CalculoNominaMapper mapper = Mappers.getMapper(CalculoNominaMapper.class);

    @Test
    @DisplayName("Debe mapear de Entidad a DTO correctamente")
    void should_MapEntityToDto_Correctly() {
        // Arrange
        CalculoNomina entity = new CalculoNomina();
        entity.setId(1L);
        entity.setEmpleadoId(10L);
        entity.setTotalNeto(new BigDecimal("1500.00"));

        // Act
        CalculoNominaDTO dto = mapper.toDTO(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.empleadoId()).isEqualTo(entity.getEmpleadoId());
        assertThat(dto.totalNeto()).isEqualTo(entity.getTotalNeto());
    }
}
