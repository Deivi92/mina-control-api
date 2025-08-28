package com.minacontrol.nomina.controller;

import com.minacontrol.nomina.entity.HistorialConfiguracionTarifas;
import com.minacontrol.nomina.service.IConfiguracionTarifasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/configuracion/tarifas/historial")
public class HistorialConfiguracionTarifasController {

    @Autowired
    private IConfiguracionTarifasService configuracionTarifasService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HistorialConfiguracionTarifas>> obtenerHistorial() {
        List<HistorialConfiguracionTarifas> historial = configuracionTarifasService.obtenerHistorial();
        return ResponseEntity.ok(historial);
    }
}