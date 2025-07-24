package com.minacontrol.nomina.mapper;

import com.minacontrol.nomina.dto.response.CalculoNominaDTO;
import com.minacontrol.nomina.entity.CalculoNomina;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CalculoNominaMapper {
    CalculoNominaDTO toDTO(CalculoNomina entity);
}
