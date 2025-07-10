package com.minacontrol.produccion.mapper;

import com.minacontrol.produccion.dto.request.RegistroProduccionCreateDTO;
import com.minacontrol.produccion.dto.response.RegistroProduccionDTO;
import com.minacontrol.produccion.entity.RegistroProduccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProduccionMapper {

    @Mapping(target = "nombreEmpleado", ignore = true)
    @Mapping(target = "nombreTurno", ignore = true)
    RegistroProduccionDTO toDTO(RegistroProduccion registroProduccion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "validado", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RegistroProduccion toEntity(RegistroProduccionCreateDTO createDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empleadoId", ignore = true)
    @Mapping(target = "tipoTurnoId", ignore = true)
    @Mapping(target = "validado", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDTO(RegistroProduccionCreateDTO dto, @MappingTarget RegistroProduccion entity);
}
