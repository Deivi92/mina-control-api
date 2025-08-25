
package com.minacontrol.turnos.unit;

import com.minacontrol.turnos.dto.request.AsignacionTurnoCreateDTO;
import com.minacontrol.turnos.dto.response.AsignacionTurnoDTO;
import com.minacontrol.turnos.entity.AsignacionTurno;
import com.minacontrol.turnos.mapper.AsignacionTurnoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para AsignacionTurnoMapper")
class AsignacionTurnoMapperTest {

    private final AsignacionTurnoMapper mapper = Mappers.getMapper(AsignacionTurnoMapper.class);

    @Test
    void should_mapEntityToDto_correctly() {
        // Arrange
        var entity = new AsignacionTurno(1L, 10L, 20L, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

        // Act
        AsignacionTurnoDTO dto = mapper.toDTO(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.empleadoId()).isEqualTo(entity.getEmpleadoId());
        assertThat(dto.tipoTurnoId()).isEqualTo(entity.getTipoTurnoId());
        assertThat(dto.fechaInicio()).isEqualTo(entity.getFechaInicio());
        assertThat(dto.fechaFin()).isEqualTo(entity.getFechaFin());
    }

    @Test
    void should_mapCreateDtoToEntity_correctly() {
        // Arrange
        var createDTO = new AsignacionTurnoCreateDTO(10L, 20L, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 28));

        // Act
        AsignacionTurno entity = mapper.toEntity(createDTO);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getEmpleadoId()).isEqualTo(createDTO.empleadoId());
        assertThat(entity.getTipoTurnoId()).isEqualTo(createDTO.tipoTurnoId());
        assertThat(entity.getFechaInicio()).isEqualTo(createDTO.fechaInicio());
        assertThat(entity.getFechaFin()).isEqualTo(createDTO.fechaFin());
    }
}
