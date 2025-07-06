
package com.minacontrol.turnos.mapper;

import com.minacontrol.turnos.dto.request.AsignacionTurnoCreateDTO;
import com.minacontrol.turnos.dto.response.AsignacionTurnoDTO;
import com.minacontrol.turnos.entity.AsignacionTurno;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AsignacionTurnoMapper {
    AsignacionTurnoDTO toDTO(AsignacionTurno asignacionTurno);
    AsignacionTurno toEntity(AsignacionTurnoCreateDTO createDTO);
}
