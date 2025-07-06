
package com.minacontrol.turnos.controller;

import com.minacontrol.turnos.dto.request.RegistrarAsistenciaDTO;
import com.minacontrol.turnos.dto.response.RegistroAsistenciaDTO;
import com.minacontrol.turnos.service.IAsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/asistencia")
@RequiredArgsConstructor
public class AsistenciaController {

    private final IAsistenciaService asistenciaService;

    @PostMapping("/registrar")
    public ResponseEntity<RegistroAsistenciaDTO> registrarAsistencia(@Valid @RequestBody RegistrarAsistenciaDTO registrarAsistenciaDTO) {
        return ResponseEntity.ok(asistenciaService.registrarAsistencia(registrarAsistenciaDTO));
    }
}
