package com.minacontrol.reportes.mapper;

import com.minacontrol.reportes.dto.response.ReporteDTO;
import com.minacontrol.reportes.entity.ReporteGenerado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReporteMapper {

    @Mapping(target = "urlDescarga", ignore = true) // Se poblará en el servicio
    ReporteDTO toDTO(ReporteGenerado reporteGenerado);
}
