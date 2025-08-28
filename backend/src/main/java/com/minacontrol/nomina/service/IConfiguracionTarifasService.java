package com.minacontrol.nomina.service;

import com.minacontrol.nomina.entity.ConfiguracionTarifas;
import com.minacontrol.nomina.entity.HistorialConfiguracionTarifas;

import java.util.List;
import java.util.Optional;

public interface IConfiguracionTarifasService {

    ConfiguracionTarifas guardarConfiguracion(ConfiguracionTarifas configuracionTarifas, Long usuarioId);

    Optional<ConfiguracionTarifas> obtenerConfiguracionVigente(String moneda);
    
    List<HistorialConfiguracionTarifas> obtenerHistorial();
    
    Optional<ConfiguracionTarifas> obtenerConfiguracionPorId(Long id);
    
    ConfiguracionTarifas actualizarConfiguracion(ConfiguracionTarifas configuracionTarifas, Long usuarioId);
}