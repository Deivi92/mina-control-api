package com.minacontrol.produccion.service.impl;

import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.exception.EmpleadoNotFoundException;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.produccion.dto.request.RegistroProduccionCreateDTO;
import com.minacontrol.produccion.dto.response.RegistroProduccionDTO;
import com.minacontrol.produccion.entity.RegistroProduccion;
import com.minacontrol.produccion.exception.RegistroProduccionDuplicateException;
import com.minacontrol.produccion.exception.RegistroProduccionNotFoundException;
import com.minacontrol.produccion.exception.RegistroProduccionValidatedException;
import com.minacontrol.produccion.mapper.ProduccionMapper;
import com.minacontrol.produccion.repository.RegistroProduccionRepository;
import com.minacontrol.produccion.service.IProduccionService;
import com.minacontrol.turnos.entity.TipoTurno;
import com.minacontrol.turnos.exception.TurnoNoEncontradoException;
import com.minacontrol.turnos.repository.TipoTurnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProduccionServiceImpl implements IProduccionService {

    private final RegistroProduccionRepository registroProduccionRepository;
    private final EmpleadoRepository empleadoRepository;
    private final TipoTurnoRepository tipoTurnoRepository;
    private final ProduccionMapper produccionMapper;

    @Override
    @Transactional
    public RegistroProduccionDTO registrarProduccion(RegistroProduccionCreateDTO createDTO) {
        if (!empleadoRepository.existsById(createDTO.empleadoId())) {
            throw new EmpleadoNotFoundException("Empleado no encontrado con ID: " + createDTO.empleadoId());
        }
        if (!tipoTurnoRepository.existsById(createDTO.tipoTurnoId())) {
            throw new TurnoNoEncontradoException("Tipo de turno no encontrado con ID: " + createDTO.tipoTurnoId());
        }
        if (registroProduccionRepository.existsByEmpleadoIdAndTipoTurnoIdAndFechaRegistro(createDTO.empleadoId(), createDTO.tipoTurnoId(), createDTO.fechaRegistro())) {
            throw new RegistroProduccionDuplicateException("Ya existe un registro de producción para el empleado en la fecha y turno especificados.");
        }

        RegistroProduccion entity = produccionMapper.toEntity(createDTO);
        RegistroProduccion savedEntity = registroProduccionRepository.save(entity);
        return enrichDto(produccionMapper.toDTO(savedEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistroProduccionDTO> listarRegistros(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin) {
        List<RegistroProduccion> registros;
        if (empleadoId != null && fechaInicio != null && fechaFin != null) {
            registros = registroProduccionRepository.findByEmpleadoIdAndFechaRegistroBetween(empleadoId, fechaInicio, fechaFin);
        } else if (empleadoId != null) {
            registros = registroProduccionRepository.findByEmpleadoId(empleadoId);
        } else if (fechaInicio != null && fechaFin != null) {
            registros = registroProduccionRepository.findByFechaRegistroBetween(fechaInicio, fechaFin);
        } else {
            registros = registroProduccionRepository.findAll();
        }
        return registros.stream()
                .map(produccionMapper::toDTO)
                .map(this::enrichDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RegistroProduccionDTO obtenerRegistroPorId(Long id) {
        return registroProduccionRepository.findById(id)
                .map(produccionMapper::toDTO)
                .map(this::enrichDto)
                .orElseThrow(() -> new RegistroProduccionNotFoundException("Registro de producción no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public RegistroProduccionDTO actualizarRegistro(Long id, RegistroProduccionCreateDTO updateDTO) {
        RegistroProduccion registro = registroProduccionRepository.findById(id)
                .orElseThrow(() -> new RegistroProduccionNotFoundException("Registro de producción no encontrado con ID: " + id));

        if (registro.isValidado()) {
            throw new RegistroProduccionValidatedException("El registro de producción con ID " + id + " ya ha sido validado y no puede ser modificado.");
        }

        produccionMapper.updateFromDTO(updateDTO, registro);
        RegistroProduccion updatedRegistro = registroProduccionRepository.save(registro);
        return enrichDto(produccionMapper.toDTO(updatedRegistro));
    }

    @Override
    @Transactional
    public void eliminarRegistro(Long id) {
        RegistroProduccion registro = registroProduccionRepository.findById(id)
                .orElseThrow(() -> new RegistroProduccionNotFoundException("Registro de producción no encontrado con ID: " + id));

        if (registro.isValidado()) {
            throw new RegistroProduccionValidatedException("El registro de producción con ID " + id + " ya ha sido validado y no puede ser eliminado.");
        }
        registroProduccionRepository.delete(registro);
    }

    @Override
    @Transactional
    public RegistroProduccionDTO validarRegistro(Long id) {
        RegistroProduccion registro = registroProduccionRepository.findById(id)
                .orElseThrow(() -> new RegistroProduccionNotFoundException("Registro de producción no encontrado con ID: " + id));

        if (registro.isValidado()) {
            return enrichDto(produccionMapper.toDTO(registro)); // Idempotent
        }

        registro.setValidado(true);
        RegistroProduccion savedRegistro = registroProduccionRepository.save(registro);
        return enrichDto(produccionMapper.toDTO(savedRegistro));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, BigDecimal> obtenerProduccionPorPeriodo(Long periodoId, LocalDate fechaInicio, LocalDate fechaFin) {
        // Esta es una implementación de marcador de posición.
        // La lógica real consultaría los registros de producción y los agregaría por empleado.
        return Collections.emptyMap();
    }

    private RegistroProduccionDTO enrichDto(RegistroProduccionDTO dto) {
        if (dto == null) return null;

        Empleado empleado = empleadoRepository.findById(dto.empleadoId()).orElse(null);
        TipoTurno turno = tipoTurnoRepository.findById(dto.tipoTurnoId()).orElse(null);

        String nombreEmpleado = (empleado != null) ? empleado.getNombres() + " " + empleado.getApellidos() : "N/A";
        String nombreTurno = (turno != null) ? turno.getNombre() : "N/A";

        return new RegistroProduccionDTO(
                dto.id(),
                dto.empleadoId(),
                nombreEmpleado,
                dto.tipoTurnoId(),
                nombreTurno,
                dto.fechaRegistro(),
                dto.cantidadExtraidaToneladas(),
                dto.ubicacionExtraccion(),
                dto.observaciones(),
                dto.validado()
        );
    }
}
