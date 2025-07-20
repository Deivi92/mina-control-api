package com.minacontrol.logistica.service.impl;

import com.minacontrol.logistica.domain.EstadoDespacho;
import com.minacontrol.logistica.dto.request.DespachoCreateDTO;
import com.minacontrol.logistica.dto.response.DespachoDTO;
import com.minacontrol.logistica.entity.Despacho;
import com.minacontrol.logistica.exception.DespachoNotFoundException;
import com.minacontrol.logistica.exception.EstadoDespachoInvalidoException;
import com.minacontrol.logistica.exception.InvalidDateRangeException;
import com.minacontrol.logistica.mapper.DespachoMapper;
import com.minacontrol.logistica.repository.DespachoRepository;
import com.minacontrol.logistica.service.ILogisticaService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogisticaServiceImpl implements ILogisticaService {

    private final DespachoRepository despachoRepository;
    private final DespachoMapper despachoMapper;

    @Override
    @Transactional
    public DespachoDTO registrarDespacho(DespachoCreateDTO createDTO) {
        Despacho despacho = despachoMapper.toEntity(createDTO);
        despacho.setNumeroDespacho(UUID.randomUUID().toString()); // Placeholder for unique number generation
        despacho.setEstado(EstadoDespacho.PROGRAMADO);
        Despacho savedDespacho = despachoRepository.save(despacho);
        return despachoMapper.toDTO(savedDespacho);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DespachoDTO> consultarDespachos(LocalDate fechaInicio, LocalDate fechaFin, EstadoDespacho estado, String destino) {
        if (fechaInicio != null && fechaFin != null && fechaInicio.isAfter(fechaFin)) {
            throw new InvalidDateRangeException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        Specification<Despacho> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (fechaInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaProgramada"), fechaInicio));
            }
            if (fechaFin != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaProgramada"), fechaFin));
            }
            if (estado != null) {
                predicates.add(cb.equal(root.get("estado"), estado));
            }
            if (StringUtils.hasText(destino)) {
                predicates.add(cb.like(cb.lower(root.get("destino")), "%" + destino.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<Despacho> despachos = despachoRepository.findAll(spec);
        return despachos.stream()
                .map(despachoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DespachoDTO actualizarEstadoDespacho(Long id, EstadoDespacho nuevoEstado) {
        Despacho despacho = despachoRepository.findById(id)
                .orElseThrow(() -> new DespachoNotFoundException("Despacho no encontrado con ID: " + id));

        if (!esTransicionValida(despacho.getEstado(), nuevoEstado)) {
            throw new EstadoDespachoInvalidoException("No se puede cambiar el estado de " + despacho.getEstado() + " a " + nuevoEstado);
        }

        despacho.setEstado(nuevoEstado);
        if (nuevoEstado == EstadoDespacho.EN_TRANSITO) {
            despacho.setFechaSalida(LocalDateTime.now());
        } else if (nuevoEstado == EstadoDespacho.ENTREGADO) {
            despacho.setFechaEntrega(LocalDateTime.now());
        }

        Despacho updatedDespacho = despachoRepository.save(despacho);
        return despachoMapper.toDTO(updatedDespacho);
    }

    private boolean esTransicionValida(EstadoDespacho actual, EstadoDespacho nuevo) {
        if (actual == nuevo) return true; // Idempotent
        if (actual == EstadoDespacho.CANCELADO || actual == EstadoDespacho.ENTREGADO) return false; // Estado final

        return switch (actual) {
            case PROGRAMADO -> nuevo == EstadoDespacho.EN_TRANSITO || nuevo == EstadoDespacho.CANCELADO;
            case EN_TRANSITO -> nuevo == EstadoDespacho.ENTREGADO || nuevo == EstadoDespacho.CANCELADO;
            default -> false;
        };
    }
}
