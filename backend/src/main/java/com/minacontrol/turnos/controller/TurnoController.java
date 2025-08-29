
package com.minacontrol.turnos.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.minacontrol.turnos.dto.request.AsignacionTurnoCreateDTO;
import com.minacontrol.turnos.dto.request.TipoTurnoCreateDTO;
import com.minacontrol.turnos.dto.response.AsignacionTurnoDTO;
import com.minacontrol.turnos.dto.response.TipoTurnoDTO;
import com.minacontrol.turnos.service.ITurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Turnos", description = "Gestión y asignación de turnos de trabajo.")
@RestController
@RequestMapping("/api/v1/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final ITurnoService turnoService;

    @PostMapping
    public ResponseEntity<TipoTurnoDTO> crearTipoTurno(@Valid @RequestBody TipoTurnoCreateDTO createDTO) {
        TipoTurnoDTO nuevoTurno = turnoService.crearTipoTurno(createDTO);
        return new ResponseEntity<>(nuevoTurno, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TipoTurnoDTO>> listarTiposDeTurno() {
        List<TipoTurnoDTO> turnos = turnoService.listarTodosLosTiposDeTurno();
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoTurnoDTO> obtenerTipoTurnoPorId(@PathVariable Long id) {
        TipoTurnoDTO turno = turnoService.obtenerTipoTurnoPorId(id);
        return ResponseEntity.ok(turno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoTurnoDTO> actualizarTipoTurno(@PathVariable Long id, @Valid @RequestBody TipoTurnoCreateDTO updateDTO) {
        TipoTurnoDTO turnoActualizado = turnoService.actualizarTipoTurno(id, updateDTO);
        return ResponseEntity.ok(turnoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTipoTurno(@PathVariable Long id) {
        turnoService.eliminarTipoTurno(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/asignaciones")
    public ResponseEntity<AsignacionTurnoDTO> asignarEmpleadoATurno(@Valid @RequestBody AsignacionTurnoCreateDTO createDTO) {
        AsignacionTurnoDTO nuevaAsignacion = turnoService.asignarEmpleadoATurno(createDTO);
        return new ResponseEntity<>(nuevaAsignacion, HttpStatus.CREATED);
    }
}
