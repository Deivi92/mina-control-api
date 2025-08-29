package com.minacontrol.produccion.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.minacontrol.produccion.dto.request.RegistroProduccionCreateDTO;
import com.minacontrol.produccion.dto.response.RegistroProduccionDTO;
import com.minacontrol.produccion.service.IProduccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Producción", description = "Operaciones para registrar y consultar la producción.")
@RestController
@RequestMapping("/api/v1/produccion")
@RequiredArgsConstructor
public class ProduccionController {

    private final IProduccionService produccionService;

    @PostMapping
    public ResponseEntity<RegistroProduccionDTO> registrarProduccion(@Valid @RequestBody RegistroProduccionCreateDTO createDTO) {
        RegistroProduccionDTO response = produccionService.registrarProduccion(createDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RegistroProduccionDTO>> listarRegistros(
            @RequestParam(required = false) Long empleadoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<RegistroProduccionDTO> response = produccionService.listarRegistros(empleadoId, fechaInicio, fechaFin);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroProduccionDTO> obtenerRegistroPorId(@PathVariable Long id) {
        RegistroProduccionDTO response = produccionService.obtenerRegistroPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistroProduccionDTO> actualizarRegistro(@PathVariable Long id, @Valid @RequestBody RegistroProduccionCreateDTO updateDTO) {
        RegistroProduccionDTO response = produccionService.actualizarRegistro(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRegistro(@PathVariable Long id) {
        produccionService.eliminarRegistro(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/validar")
    public ResponseEntity<RegistroProduccionDTO> validarRegistro(@PathVariable Long id) {
        RegistroProduccionDTO response = produccionService.validarRegistro(id);
        return ResponseEntity.ok(response);
    }
}
