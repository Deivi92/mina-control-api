package com.minacontrol.empleado.controller;

import com.minacontrol.empleado.dto.request.EmpleadoRequest;
import com.minacontrol.empleado.dto.response.EmpleadoResponse;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.service.IEmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final IEmpleadoService empleadoService;

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crearEmpleado(@Valid @RequestBody EmpleadoRequest request) {
        EmpleadoResponse response = empleadoService.crearEmpleado(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoResponse>> listarEmpleados(
            @RequestParam(required = false) EstadoEmpleado estado,
            @RequestParam(required = false) String cargo) {
        List<EmpleadoResponse> response = empleadoService.listarEmpleados(estado, cargo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> obtenerEmpleadoPorId(@PathVariable Long id) {
        EmpleadoResponse response = empleadoService.obtenerEmpleadoPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> actualizarEmpleado(@PathVariable Long id, @Valid @RequestBody EmpleadoRequest request) {
        EmpleadoResponse response = empleadoService.actualizarEmpleado(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id) {
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EmpleadoResponse> cambiarEstadoEmpleado(@PathVariable Long id, @RequestBody EstadoEmpleado nuevoEstado) {
        EmpleadoResponse response = empleadoService.cambiarEstadoEmpleado(id, nuevoEstado);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/perfil")
    public ResponseEntity<EmpleadoResponse> obtenerPerfilPersonal(Authentication authentication) {
        String username = authentication.getName();
        EmpleadoResponse response = empleadoService.obtenerPerfilPersonal(username);
        return ResponseEntity.ok(response);
    }
}
