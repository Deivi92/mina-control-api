package com.minacontrol.turnos.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.minacontrol.turnos.dto.request.ExcepcionAsistenciaDTO;
import com.minacontrol.turnos.dto.request.RegistrarAsistenciaDTO;
import com.minacontrol.turnos.dto.response.RegistroAsistenciaDTO;
import com.minacontrol.turnos.service.IAsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Turnos", description = "Gestión de turnos, horarios y asistencia.")
@RestController
@RequestMapping("/api/v1/asistencia")
@RequiredArgsConstructor
public class AsistenciaController {

    private final IAsistenciaService asistenciaService;

    @PostMapping("/registrar")
    public ResponseEntity<RegistroAsistenciaDTO> registrarAsistencia(@Valid @RequestBody RegistrarAsistenciaDTO registrarAsistenciaDTO) {
        return ResponseEntity.ok(asistenciaService.registrarAsistencia(registrarAsistenciaDTO));
    }

    @GetMapping("/consultar")
    public ResponseEntity<List<RegistroAsistenciaDTO>> consultarAsistencia(
            @RequestParam(required = false) Long empleadoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(asistenciaService.consultarAsistencia(empleadoId, fechaInicio, fechaFin));
    }

    @PostMapping("/excepciones")
    public ResponseEntity<RegistroAsistenciaDTO> gestionarExcepcionAsistencia(@Valid @RequestBody ExcepcionAsistenciaDTO excepcionAsistenciaDTO) {
        return ResponseEntity.ok(asistenciaService.gestionarExcepcionAsistencia(excepcionAsistenciaDTO));
    }
}
