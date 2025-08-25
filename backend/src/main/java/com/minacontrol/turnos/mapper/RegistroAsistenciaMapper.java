
package com.minacontrol.turnos.mapper;

import com.minacontrol.turnos.dto.request.RegistrarAsistenciaDTO;
import com.minacontrol.turnos.dto.response.RegistroAsistenciaDTO;
import com.minacontrol.turnos.entity.RegistroAsistencia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegistroAsistenciaMapper {
    RegistroAsistenciaDTO toDTO(RegistroAsistencia registroAsistencia);
    RegistroAsistencia toEntity(RegistrarAsistenciaDTO dto);
}
