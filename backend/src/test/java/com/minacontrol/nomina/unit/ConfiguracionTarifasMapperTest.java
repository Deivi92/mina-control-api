package com.minacontrol.nomina.unit;

import com.minacontrol.nomina.dto.request.ConfiguracionTarifasCreateDTO;
import com.minacontrol.nomina.dto.response.ConfiguracionTarifasDTO;
import com.minacontrol.nomina.entity.ConfiguracionTarifas;
import com.minacontrol.nomina.mapper.ConfiguracionTarifasMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para ConfiguracionTarifasMapper")
class ConfiguracionTarifasMapperTest {

    private ConfiguracionTarifasMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ConfiguracionTarifasMapper.class);
    }

    @Test
    @DisplayName("Debe convertir ConfiguracionTarifasCreateDTO a ConfiguracionTarifas entity")
    void should_ConvertCreateDTOToEntity_When_GivenCreateDTO() {
        // Arrange
        ConfiguracionTarifasCreateDTO dto = new ConfiguracionTarifasCreateDTO(
                new BigDecimal("6823.00"),
                new BigDecimal("8400.00"),
                "COP",
                LocalDate.now()
        );

        // Act
        ConfiguracionTarifas entity = mapper.toEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getTarifaPorHora()).isEqualTo(dto.tarifaPorHora());
        assertThat(entity.getBonoPorTonelada()).isEqualTo(dto.bonoPorTonelada());
        assertThat(entity.getMoneda()).isEqualTo(dto.moneda());
        assertThat(entity.getFechaVigencia()).isEqualTo(dto.fechaVigencia());
        // Campos que deben ser ignorados
        assertThat(entity.getId()).isNull();
        assertThat(entity.getCreadoPor()).isNull();
        assertThat(entity.getFechaCreacion()).isNull();
    }

    @Test
    @DisplayName("Debe convertir ConfiguracionTarifas entity a ConfiguracionTarifasDTO")
    void should_ConvertEntityToDTO_When_GivenEntity() {
        // Arrange
        ConfiguracionTarifas entity = ConfiguracionTarifas.builder()
                .id(1L)
                .tarifaPorHora(new BigDecimal("6823.00"))
                .bonoPorTonelada(new BigDecimal("8400.00"))
                .moneda("COP")
                .fechaVigencia(LocalDate.now())
                .creadoPor(1L)
                .fechaCreacion(LocalDate.now())
                .build();

        // Act
        ConfiguracionTarifasDTO dto = mapper.toDTO(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.tarifaPorHora()).isEqualTo(entity.getTarifaPorHora());
        assertThat(dto.bonoPorTonelada()).isEqualTo(entity.getBonoPorTonelada());
        assertThat(dto.moneda()).isEqualTo(entity.getMoneda());
        assertThat(dto.fechaVigencia()).isEqualTo(entity.getFechaVigencia());
    }
}