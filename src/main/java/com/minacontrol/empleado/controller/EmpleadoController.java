package com.minacontrol.empleado.controller;

import com.minacontrol.empleado.dto.EmpleadoRequestDTO;
import com.minacontrol.empleado.dto.EmpleadoResponseDTO;
import com.minacontrol.empleado.service.EmpleadoService;
import com.minacontrol.exception.RecursoNoEncontradoException; // Actualizado al nuevo nombre en español

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(@Qualifier("empleadoService") EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponseDTO> crearEmpleado(@Valid @RequestBody EmpleadoRequestDTO empleadoRequestDTO) {
        EmpleadoResponseDTO nuevoEmpleado = empleadoService.crearEmpleado(empleadoRequestDTO);
        return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> obtenerEmpleadoPorId(@PathVariable Long id) {
        return empleadoService.obtenerEmpleadoPorId(id)
                .map(empleado -> new ResponseEntity<>(empleado, HttpStatus.OK))
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado con id: " + id));
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerTodosLosEmpleados() {
        List<EmpleadoResponseDTO> empleados = empleadoService.obtenerTodosLosEmpleados();
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> actualizarEmpleado(@PathVariable Long id, @Valid @RequestBody EmpleadoRequestDTO empleadoRequestDTO) {
        return empleadoService.actualizarEmpleado(id, empleadoRequestDTO)
                .map(empleado -> new ResponseEntity<>(empleado, HttpStatus.OK))
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado con id: " + id + " para actualizar"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id) {
        if (empleadoService.eliminarEmpleado(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw new RecursoNoEncontradoException("Empleado no encontrado con id: " + id + " para eliminar");
    }
}
