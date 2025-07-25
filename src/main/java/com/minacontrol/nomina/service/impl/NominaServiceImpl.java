package com.minacontrol.nomina.service.impl;

import com.minacontrol.empleado.service.IEmpleadoService;
import com.minacontrol.nomina.dto.request.AjusteNominaDTO;
import com.minacontrol.nomina.dto.request.CalcularNominaRequestDTO;
import com.minacontrol.nomina.dto.response.CalculoNominaDTO;
import com.minacontrol.nomina.dto.response.CalculoNominaResumenDTO;
import com.minacontrol.nomina.dto.response.ComprobantePagoDTO;
import com.minacontrol.nomina.entity.CalculoNomina;
import com.minacontrol.nomina.entity.ComprobantePago;
import com.minacontrol.nomina.entity.PeriodoNomina;
import com.minacontrol.nomina.enums.EstadoPeriodo;
import com.minacontrol.nomina.exception.AjusteNominaNoPermitidoException;
import com.minacontrol.nomina.exception.CalculoNominaNotFoundException;
import com.minacontrol.nomina.exception.PeriodoNominaInvalidoException;
import com.minacontrol.nomina.mapper.CalculoNominaMapper;
import com.minacontrol.nomina.mapper.ComprobantePagoMapper;
import com.minacontrol.nomina.repository.CalculoNominaRepository;
import com.minacontrol.nomina.repository.ComprobantePagoRepository;
import com.minacontrol.nomina.repository.PeriodoNominaRepository;
import com.minacontrol.nomina.service.INominaService;
import com.minacontrol.produccion.service.IProduccionService;
import com.minacontrol.turnos.service.IAsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NominaServiceImpl implements INominaService {

    private final PeriodoNominaRepository periodoNominaRepository;
    private final CalculoNominaRepository calculoNominaRepository;
    private final ComprobantePagoRepository comprobantePagoRepository;
    private final IAsistenciaService asistenciaService;
    private final IProduccionService produccionService;
    private final IEmpleadoService empleadoService; // Para obtener empleados activos
    private final CalculoNominaMapper calculoNominaMapper;
    private final ComprobantePagoMapper comprobantePagoMapper;

    @Override
    @Transactional
    public CalculoNominaResumenDTO calcularNominaSemanal(CalcularNominaRequestDTO request) {
        PeriodoNomina periodo = periodoNominaRepository.findById(request.periodoId())
                .orElseThrow(() -> new PeriodoNominaInvalidoException("El período de nómina no existe."));

        if (periodo.getEstado() != EstadoPeriodo.ABIERTO) {
            throw new PeriodoNominaInvalidoException("El período de nómina no está abierto para cálculo.");
        }

        // Suponiendo que IEmpleadoService tiene un método para obtener IDs de empleados activos
        List<Long> empleadosActivosIds = empleadoService.obtenerIdsEmpleadosActivos();

        Map<Long, BigDecimal> horasTrabajadas = asistenciaService.obtenerHorasTrabajadasPorPeriodo(
                request.periodoId(), periodo.getFechaInicio(), periodo.getFechaFin());
        Map<Long, BigDecimal> produccion = produccionService.obtenerProduccionPorPeriodo(
                request.periodoId(), periodo.getFechaInicio(), periodo.getFechaFin());

        BigDecimal montoTotal = BigDecimal.ZERO;

        for (Long empleadoId : empleadosActivosIds) {
            BigDecimal salarioBase = horasTrabajadas.getOrDefault(empleadoId, BigDecimal.ZERO).multiply(new BigDecimal("10")); // Tarifa por hora
            BigDecimal bonificaciones = produccion.getOrDefault(empleadoId, BigDecimal.ZERO).multiply(new BigDecimal("2")); // Bono por tonelada
            BigDecimal deducciones = BigDecimal.ZERO;
            BigDecimal totalBruto = salarioBase.add(bonificaciones);
            BigDecimal totalNeto = totalBruto.subtract(deducciones);

            CalculoNomina calculo = CalculoNomina.builder()
                    .periodo(periodo)
                    .empleadoId(empleadoId)
                    .salarioBase(salarioBase)
                    .bonificaciones(bonificaciones)
                    .deducciones(deducciones)
                    .totalBruto(totalBruto)
                    .totalNeto(totalNeto)
                    .build();
            calculoNominaRepository.save(calculo);
            montoTotal = montoTotal.add(totalNeto);
        }

        periodo.setEstado(EstadoPeriodo.CALCULADO);
        periodoNominaRepository.save(periodo);

        return new CalculoNominaResumenDTO(empleadosActivosIds.size(), montoTotal);
    }

    @Override
    @Transactional
    public CalculoNominaDTO ajustarCalculoNomina(Long id, AjusteNominaDTO ajusteDTO) {
        CalculoNomina calculo = calculoNominaRepository.findById(id)
                .orElseThrow(() -> new CalculoNominaNotFoundException("Cálculo de nómina no encontrado."));

        if (calculo.getPeriodo().getEstado() != EstadoPeriodo.CALCULADO) {
            throw new AjusteNominaNoPermitidoException("No se pueden realizar ajustes a una nómina que no está en estado CALCULADO.");
        }

        BigDecimal montoAjuste = ajusteDTO.monto();
        if (ajusteDTO.esDeduccion()) {
            calculo.setDeducciones(calculo.getDeducciones().add(montoAjuste));
            calculo.setTotalNeto(calculo.getTotalNeto().subtract(montoAjuste));
        } else {
            calculo.setBonificaciones(calculo.getBonificaciones().add(montoAjuste));
            calculo.setTotalNeto(calculo.getTotalNeto().add(montoAjuste));
        }

        String observacion = String.format("Ajuste manual: %s (%s) por %.2f. Justificación: %s",
                ajusteDTO.concepto(),
                ajusteDTO.esDeduccion() ? "Deducción" : "Bonificación",
                montoAjuste,
                ajusteDTO.justificacion());
        calculo.setObservaciones(calculo.getObservaciones() == null ? observacion : calculo.getObservaciones() + "\n" + observacion);

        CalculoNomina calculoActualizado = calculoNominaRepository.save(calculo);
        return calculoNominaMapper.toDTO(calculoActualizado);
    }

    @Override
    @Transactional
    public List<ComprobantePagoDTO> generarComprobantesPago(Long periodoId) {
        PeriodoNomina periodo = periodoNominaRepository.findById(periodoId)
                .orElseThrow(() -> new PeriodoNominaInvalidoException("El período de nómina no existe."));

        if (periodo.getEstado() != EstadoPeriodo.CALCULADO) {
            throw new PeriodoNominaInvalidoException("Solo se pueden generar comprobantes para períodos en estado CALCULADO.");
        }

        List<CalculoNomina> calculos = calculoNominaRepository.findByPeriodoId(periodoId);

        List<ComprobantePago> comprobantes = calculos.stream().map(calculo -> {
            ComprobantePago comprobante = ComprobantePago.builder()
                    .calculo(calculo)
                    .numeroComprobante("COMP-" + UUID.randomUUID().toString())
                    .fechaEmision(LocalDateTime.now())
                    .rutaArchivoPdf("/generated/pdfs/comp-" + calculo.getId() + ".pdf") // Simulación de ruta
                    .build();
            return comprobantePagoRepository.save(comprobante);
        }).collect(Collectors.toList());

        periodo.setEstado(EstadoPeriodo.PAGADO);
        periodoNominaRepository.save(periodo);

        return comprobantes.stream()
                .map(comprobantePagoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalculoNominaDTO> consultarHistorialPagos(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
                List<CalculoNomina> calculos = calculoNominaRepository.findByEmpleadoIdAndFechas(empleadoId, fechaInicio, fechaFin);
        return calculos.stream()
                .map(calculoNominaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> obtenerDatosCostosParaReporte(LocalDate fechaInicio, LocalDate fechaFin) {
        return calculoNominaRepository.findAll().stream()
                .filter(c -> !c.getPeriodo().getFechaInicio().isAfter(fechaFin) && !c.getPeriodo().getFechaFin().isBefore(fechaInicio))
                .map(calculoNominaMapper::toDTO)
                .collect(Collectors.toList());
    }
}