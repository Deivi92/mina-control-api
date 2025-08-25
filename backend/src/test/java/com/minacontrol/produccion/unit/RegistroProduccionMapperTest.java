
package com.minacontrol.produccion.unit;

import com.minacontrol.produccion.dto.request.RegistroProduccionCreateDTO;
import com.minacontrol.produccion.dto.response.RegistroProduccionDTO;
import com.minacontrol.produccion.entity.RegistroProduccion;
import com.minacontrol.produccion.mapper.ProduccionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para ProduccionMapper")
public class RegistroProduccionMapperTest {

    private ProduccionMapper produccionMapper;

    @BeforeEach
    void setUp() {
        produccionMapper = Mappers.getMapper(ProduccionMapper.class);
    }

    @Test
    @DisplayName("Debe mapear correctamente de Entidad a DTO de Respuesta")
    void should_MapEntityToResponseDto_Correctly() {
        // Arrange
        RegistroProduccion entity = RegistroProduccion.builder()
                .id(1L)
                .empleadoId(10L)
                .tipoTurnoId(2L)
                .fechaRegistro(LocalDate.of(2025, 7, 15))
                .cantidadExtraidaToneladas(new BigDecimal("25.5"))
                .ubicacionExtraccion("Veta Norte 3")
                .observaciones("Extracción sin incidentes.")
                .validado(true)
                .build();

        // Act
        RegistroProduccionDTO dto = produccionMapper.toDTO(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.empleadoId()).isEqualTo(entity.getEmpleadoId());
        assertThat(dto.fechaRegistro()).isEqualTo(entity.getFechaRegistro());
        assertThat(dto.cantidadExtraidaToneladas()).isEqualTo(entity.getCantidadExtraidaToneladas());
        assertThat(dto.validado()).isEqualTo(entity.isValidado());
        // Los campos enriquecidos como nombreEmpleado se prueban en el servicio
    }

    @Test
    @DisplayName("Debe mapear correctamente de DTO de Creación a Entidad")
    void should_MapCreateDtoToEntity_Correctly() {
        // Arrange
        RegistroProduccionCreateDTO createDTO = new RegistroProduccionCreateDTO(
                10L,
                2L,
                LocalDate.of(2025, 7, 15),
                new BigDecimal("25.5"),
                "Veta Norte 3",
                "Extracción sin incidentes."
        );

        // Act
        RegistroProduccion entity = produccionMapper.toEntity(createDTO);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getEmpleadoId()).isEqualTo(createDTO.empleadoId());
        assertThat(entity.getTipoTurnoId()).isEqualTo(createDTO.tipoTurnoId());
        assertThat(entity.getFechaRegistro()).isEqualTo(createDTO.fechaRegistro());
        assertThat(entity.getCantidadExtraidaToneladas()).isEqualTo(createDTO.cantidadExtraidaToneladas());
        assertThat(entity.getUbicacionExtraccion()).isEqualTo(createDTO.ubicacionExtraccion());
        assertThat(entity.getObservaciones()).isEqualTo(createDTO.observaciones());
        assertThat(entity.isValidado()).isFalse();
    }

    @Test
    @DisplayName("Debe actualizar una entidad desde un DTO sin sobreescribir el ID")
    void should_UpdateEntityFromDto_Correctly() {
        // Arrange
        RegistroProduccionCreateDTO updateDTO = new RegistroProduccionCreateDTO(
            null, // El ID del empleado no se debería actualizar
            null, // El ID del turno no se debería actualizar
            LocalDate.of(2025, 8, 20),
            new BigDecimal("30.0"),
            "Veta Sur 1",
            "Producción actualizada."
        );

        RegistroProduccion entityToUpdate = RegistroProduccion.builder()
            .id(100L)
            .empleadoId(5L)
            .tipoTurnoId(1L)
            .fechaRegistro(LocalDate.of(2025, 8, 1))
            .cantidadExtraidaToneladas(new BigDecimal("50.0"))
            .ubicacionExtraccion("Veta Original")
            .observaciones("Obs Original")
            .validado(false)
            .build();

        // Act
        produccionMapper.updateFromDTO(updateDTO, entityToUpdate);

        // Assert
        assertThat(entityToUpdate.getId()).isEqualTo(100L); // ID no cambia
        assertThat(entityToUpdate.getEmpleadoId()).isEqualTo(5L); // ID de empleado no cambia
        assertThat(entityToUpdate.getTipoTurnoId()).isEqualTo(1L); // ID de turno no cambia
        assertThat(entityToUpdate.getFechaRegistro()).isEqualTo(updateDTO.fechaRegistro());
        assertThat(entityToUpdate.getCantidadExtraidaToneladas()).isEqualTo(updateDTO.cantidadExtraidaToneladas());
        assertThat(entityToUpdate.getUbicacionExtraccion()).isEqualTo(updateDTO.ubicacionExtraccion());
        assertThat(entityToUpdate.getObservaciones()).isEqualTo(updateDTO.observaciones());
    }
}
