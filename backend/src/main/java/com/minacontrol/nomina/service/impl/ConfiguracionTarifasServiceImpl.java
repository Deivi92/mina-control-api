package com.minacontrol.nomina.service.impl;

import com.minacontrol.nomina.entity.ConfiguracionTarifas;
import com.minacontrol.nomina.entity.HistorialConfiguracionTarifas;
import com.minacontrol.nomina.repository.ConfiguracionTarifasRepository;
import com.minacontrol.nomina.repository.HistorialConfiguracionTarifasRepository;
import com.minacontrol.nomina.service.IConfiguracionTarifasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConfiguracionTarifasServiceImpl implements IConfiguracionTarifasService {

    @Autowired
    private ConfiguracionTarifasRepository configuracionTarifasRepository;
    
    @Autowired
    private HistorialConfiguracionTarifasRepository historialRepository;

    @Override
    public ConfiguracionTarifas guardarConfiguracion(ConfiguracionTarifas configuracionTarifas, Long usuarioId) {
        configuracionTarifas.setCreadoPor(usuarioId);
        configuracionTarifas.setFechaCreacion(LocalDate.now());
        ConfiguracionTarifas guardada = configuracionTarifasRepository.save(configuracionTarifas);
        
        // Registrar en el historial
        HistorialConfiguracionTarifas historial = HistorialConfiguracionTarifas.builder()
                .tarifaPorHora(guardada.getTarifaPorHora())
                .bonoPorTonelada(guardada.getBonoPorTonelada())
                .moneda(guardada.getMoneda())
                .fechaVigencia(guardada.getFechaVigencia())
                .creadoPor(guardada.getCreadoPor())
                .fechaCreacion(LocalDateTime.now())
                .accion("CREADO") // O "ACTUALIZADO" si es una actualización
                .build();
        historialRepository.save(historial);
        
        return guardada;
    }

    @Override
    public Optional<ConfiguracionTarifas> obtenerConfiguracionVigente(String moneda) {
        return configuracionTarifasRepository.findConfiguracionVigente(LocalDate.now(), moneda);
    }
    
    @Override
    public List<HistorialConfiguracionTarifas> obtenerHistorial() {
        return historialRepository.findByOrderByFechaCreacionDesc();
    }
    
    @Override
    public Optional<ConfiguracionTarifas> obtenerConfiguracionPorId(Long id) {
        return configuracionTarifasRepository.findById(id);
    }
    
    @Override
    public ConfiguracionTarifas actualizarConfiguracion(ConfiguracionTarifas configuracionTarifas, Long usuarioId) {
        // Preservar los valores de auditoría de la entidad existente
        ConfiguracionTarifas existente = configuracionTarifasRepository.findById(configuracionTarifas.getId())
                .orElseThrow(() -> new IllegalArgumentException("Configuración de tarifas no encontrada con ID: " + configuracionTarifas.getId()));
        
        // Actualizar solo los campos modificables, manteniendo la auditoría original
        existente.setTarifaPorHora(configuracionTarifas.getTarifaPorHora());
        existente.setBonoPorTonelada(configuracionTarifas.getBonoPorTonelada());
        existente.setMoneda(configuracionTarifas.getMoneda());
        existente.setFechaVigencia(configuracionTarifas.getFechaVigencia());
        // No se actualiza creadoPor ni fechaCreacion para preservar la auditoría
        
        ConfiguracionTarifas actualizada = configuracionTarifasRepository.save(existente);
        
        // Registrar en el historial
        HistorialConfiguracionTarifas historial = HistorialConfiguracionTarifas.builder()
                .tarifaPorHora(actualizada.getTarifaPorHora())
                .bonoPorTonelada(actualizada.getBonoPorTonelada())
                .moneda(actualizada.getMoneda())
                .fechaVigencia(actualizada.getFechaVigencia())
                .creadoPor(actualizada.getCreadoPor())
                .fechaCreacion(LocalDateTime.now())
                .accion("ACTUALIZADO")
                .build();
        historialRepository.save(historial);
        
        return actualizada;
    }
}