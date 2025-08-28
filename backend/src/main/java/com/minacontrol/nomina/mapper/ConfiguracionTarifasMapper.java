package com.minacontrol.nomina.mapper;

import com.minacontrol.nomina.dto.request.ConfiguracionTarifasCreateDTO;
import com.minacontrol.nomina.dto.response.ConfiguracionTarifasDTO;
import com.minacontrol.nomina.entity.ConfiguracionTarifas;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConfiguracionTarifasMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    ConfiguracionTarifas toEntity(ConfiguracionTarifasCreateDTO dto);

    ConfiguracionTarifasDTO toDTO(ConfiguracionTarifas entity);
}