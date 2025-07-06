
package com.minacontrol.turnos.unit;

import com.minacontrol.turnos.dto.request.TipoTurnoCreateDTO;
import com.minacontrol.turnos.dto.response.TipoTurnoDTO;
import com.minacontrol.turnos.entity.TipoTurno;
import com.minacontrol.turnos.mapper.TipoTurnoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para TipoTurnoMapper")
class TipoTurnoMapperTest {

    private final TipoTurnoMapper mapper = Mappers.getMapper(TipoTurnoMapper.class);

    @Test
    @DisplayName("Debe mapear TipoTurno a TipoTurnoDTO correctamente")
    void should_mapEntityToDto_correctly() {
        // Arrange
        var entity = TipoTurno.builder()
                .id(1L)
                .nombre("Turno de Noche")
                .horaInicio(LocalTime.of(22, 0))
                .horaFin(LocalTime.of(6, 0))
                .descripcion("Turno nocturno")
                .activo(true)
                .build();

        // Act
        TipoTurnoDTO dto = mapper.toDTO(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.nombre()).isEqualTo(entity.getNombre());
        assertThat(dto.horaInicio()).isEqualTo(entity.getHoraInicio());
        assertThat(dto.horaFin()).isEqualTo(entity.getHoraFin());
        assertThat(dto.descripcion()).isEqualTo(entity.getDescripcion());
        assertThat(dto.activo()).isEqualTo(entity.isActivo());
    }

    @Test
    @DisplayName("Debe mapear TipoTurnoCreateDTO a TipoTurno correctamente")
    void should_mapCreateDtoToEntity_correctly() {
        // Arrange
        var createDTO = new TipoTurnoCreateDTO(
                "Turno Mixto",
                LocalTime.of(12, 0),
                LocalTime.of(20, 0),
                "Turno que cubre tarde y noche"
        );

        // Act
        TipoTurno entity = mapper.toEntity(createDTO);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull(); // El ID debe ser nulo antes de persistir
        assertThat(entity.getNombre()).isEqualTo(createDTO.nombre());
        assertThat(entity.getHoraInicio()).isEqualTo(createDTO.horaInicio());
        assertThat(entity.getHoraFin()).isEqualTo(createDTO.horaFin());
        assertThat(entity.getDescripcion()).isEqualTo(createDTO.descripcion());
        assertThat(entity.isActivo()).isTrue(); // Por defecto debe ser activo
    }

    @Test
    @DisplayName("Debe manejar valores nulos al mapear de Entidad a DTO")
    void should_handleNulls_when_mappingEntityToDto() {
        // Arrange
        var entity = TipoTurno.builder()
                .id(1L)
                .nombre("Turno sin Descripción")
                .horaInicio(LocalTime.of(8, 0))
                .horaFin(LocalTime.of(16, 0))
                .descripcion(null) // Descripción nula
                .activo(true)
                .build();

        // Act
        TipoTurnoDTO dto = mapper.toDTO(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.descripcion()).isNull();
    }
}
