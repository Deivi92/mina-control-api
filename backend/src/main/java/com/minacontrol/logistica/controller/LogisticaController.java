package com.minacontrol.logistica.controller;

import com.minacontrol.logistica.domain.EstadoDespacho;
import com.minacontrol.logistica.dto.request.DespachoCreateDTO;
import com.minacontrol.logistica.dto.response.DespachoDTO;
import com.minacontrol.logistica.service.ILogisticaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/logistica/despachos")
@RequiredArgsConstructor
public class LogisticaController {

    private final ILogisticaService logisticaService;

    @PostMapping
    public ResponseEntity<DespachoDTO> registrarDespacho(@Valid @RequestBody DespachoCreateDTO createDTO) {
        DespachoDTO response = logisticaService.registrarDespacho(createDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DespachoDTO>> consultarDespachos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) EstadoDespacho estado,
            @RequestParam(required = false) String destino) {
        List<DespachoDTO> response = logisticaService.consultarDespachos(fechaInicio, fechaFin, estado, destino);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<DespachoDTO> actualizarEstadoDespacho(@PathVariable Long id, @RequestBody EstadoDespacho nuevoEstado) {
        DespachoDTO response = logisticaService.actualizarEstadoDespacho(id, nuevoEstado);
        return ResponseEntity.ok(response);
    }
}
