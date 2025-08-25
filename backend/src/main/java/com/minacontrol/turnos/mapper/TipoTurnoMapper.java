
package com.minacontrol.turnos.mapper;

import com.minacontrol.turnos.dto.request.TipoTurnoCreateDTO;
import com.minacontrol.turnos.dto.response.TipoTurnoDTO;
import com.minacontrol.turnos.entity.TipoTurno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TipoTurnoMapper {

    TipoTurnoDTO toDTO(TipoTurno tipoTurno);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    TipoTurno toEntity(TipoTurnoCreateDTO createDTO);

    void updateEntityFromDTO(TipoTurnoCreateDTO updateDTO, @MappingTarget TipoTurno tipoTurno);
}
