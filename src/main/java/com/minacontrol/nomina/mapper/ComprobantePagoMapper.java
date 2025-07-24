package com.minacontrol.nomina.mapper;

import com.minacontrol.nomina.dto.response.ComprobantePagoDTO;
import com.minacontrol.nomina.entity.ComprobantePago;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComprobantePagoMapper {
    ComprobantePagoDTO toDTO(ComprobantePago entity);
}
