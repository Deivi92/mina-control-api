package com.minacontrol.logistica.mapper;

import com.minacontrol.logistica.domain.EstadoDespacho;
import com.minacontrol.logistica.dto.request.DespachoCreateDTO;
import com.minacontrol.logistica.dto.response.DespachoDTO;
import com.minacontrol.logistica.entity.Despacho;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DespachoMapper {

    DespachoDTO toDTO(Despacho despacho);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroDespacho", ignore = true)
    @Mapping(target = "fechaSalida", ignore = true)
    @Mapping(target = "fechaEntrega", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Despacho toEntity(DespachoCreateDTO createDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroDespacho", ignore = true)
    @Mapping(target = "fechaSalida", ignore = true)
    @Mapping(target = "fechaEntrega", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDTO(DespachoCreateDTO dto, @MappingTarget Despacho entity);
}
