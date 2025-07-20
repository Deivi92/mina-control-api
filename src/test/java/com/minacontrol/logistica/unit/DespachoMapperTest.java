package com.minacontrol.logistica.unit;

import com.minacontrol.logistica.entity.Despacho;
import com.minacontrol.logistica.dto.request.DespachoCreateDTO;
import com.minacontrol.logistica.dto.response.DespachoDTO;
import com.minacontrol.logistica.mapper.DespachoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para DespachoMapper")
class DespachoMapperTest {

    private DespachoMapper despachoMapper;

    @BeforeEach
    void setUp() {
        despachoMapper = Mappers.getMapper(DespachoMapper.class);
    }

    @Test
    @DisplayName("Debe mapear correctamente de Entidad a DTO de Respuesta")
    void should_MapEntityToResponseDto_Correctly() {
        // Arrange
        Despacho entity = new Despacho();
        entity.setId(1L);
        entity.setDestino("Destino de Prueba");

        // Act
        DespachoDTO dto = despachoMapper.toDTO(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.destino()).isEqualTo(entity.getDestino());
    }

    @Test
    @DisplayName("Debe mapear correctamente de DTO de Creación a Entidad")
    void should_MapCreateDtoToEntity_Correctly() {
        // Arrange
        var createDTO = new DespachoCreateDTO(
                "Juan Conductor",
                "XYZ-123",
                BigDecimal.TEN,
                "Nuevo Destino",
                LocalDate.now(),
                "Observaciones"
        );

        // Act
        Despacho entity = despachoMapper.toEntity(createDTO);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getDestino()).isEqualTo(createDTO.destino());
        assertThat(entity.getCantidadDespachadaToneladas()).isEqualTo(createDTO.cantidadDespachadaToneladas());
    }

    @Test
    @DisplayName("Debe actualizar una entidad desde un DTO")
    void should_UpdateEntityFromDto_Correctly() {
        // Arrange
        var updateDTO = new DespachoCreateDTO(
                "Pedro Conductor",
                "ABC-456",
                new BigDecimal("25.5"),
                "Destino Actualizado",
                LocalDate.now().plusDays(1),
                "Obs actualizada"
        );

        Despacho entityToUpdate = new Despacho();
        entityToUpdate.setId(1L);
        entityToUpdate.setDestino("Destino Original");

        // Act
        despachoMapper.updateFromDTO(updateDTO, entityToUpdate);

        // Assert
        assertThat(entityToUpdate.getId()).isEqualTo(1L);
        assertThat(entityToUpdate.getDestino()).isEqualTo(updateDTO.destino());
    }
}
