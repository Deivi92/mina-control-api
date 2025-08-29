package com.minacontrol.nomina.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.minacontrol.nomina.dto.request.AjusteNominaDTO;
import com.minacontrol.nomina.dto.request.CalcularNominaRequestDTO;
import com.minacontrol.nomina.dto.response.CalculoNominaDTO;
import com.minacontrol.nomina.dto.response.CalculoNominaResumenDTO;
import com.minacontrol.nomina.dto.response.ComprobantePagoDTO;
import com.minacontrol.nomina.service.INominaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Nómina", description = "Operaciones para gestionar la nómina, incluyendo tarifas, historial y cálculos.")
@RestController
@RequestMapping("/api/nomina")
@RequiredArgsConstructor
public class NominaController {

    private final INominaService nominaService;

    @PostMapping("/calcular")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMINISTRADOR')")
    public ResponseEntity<CalculoNominaResumenDTO> calcularNominaSemanal(@Valid @RequestBody CalcularNominaRequestDTO request) {
        return ResponseEntity.ok(nominaService.calcularNominaSemanal(request));
    }

    @PatchMapping("/calculos/{id}/ajustar")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<CalculoNominaDTO> ajustarCalculoNomina(@PathVariable Long id, @Valid @RequestBody AjusteNominaDTO ajusteDTO) {
        return ResponseEntity.ok(nominaService.ajustarCalculoNomina(id, ajusteDTO));
    }

    @PostMapping("/periodos/{id}/generar-comprobantes")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<List<ComprobantePagoDTO>> generarComprobantesPago(@PathVariable Long id) {
        return ResponseEntity.ok(nominaService.generarComprobantesPago(id));
    }

    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO')")
    public ResponseEntity<List<CalculoNominaDTO>> consultarHistorialPagos(
            @RequestParam Long empleadoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        // Aquí se necesitaría lógica adicional para asegurar que un EMPLEADO solo pueda ver su propio historial.
        // Esto se puede hacer en el servicio, obteniendo el ID del usuario autenticado.
        return ResponseEntity.ok(nominaService.consultarHistorialPagos(empleadoId, fechaInicio, fechaFin));
    }
}
