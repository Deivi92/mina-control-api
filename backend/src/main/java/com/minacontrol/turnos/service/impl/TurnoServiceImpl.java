
package com.minacontrol.turnos.service.impl;

import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.turnos.dto.request.AsignacionTurnoCreateDTO;
import com.minacontrol.turnos.dto.request.TipoTurnoCreateDTO;
import com.minacontrol.turnos.dto.response.AsignacionTurnoDTO;
import com.minacontrol.turnos.dto.response.TipoTurnoDTO;
import com.minacontrol.turnos.entity.AsignacionTurno;
import com.minacontrol.turnos.entity.TipoTurno;
import com.minacontrol.turnos.exception.AsignacionTurnoInvalidaException;
import com.minacontrol.turnos.exception.TurnoAlreadyExistsException;
import com.minacontrol.turnos.exception.TurnoInvalidoException;
import com.minacontrol.turnos.exception.TurnoNoEncontradoException;
import com.minacontrol.turnos.mapper.AsignacionTurnoMapper;
import com.minacontrol.turnos.mapper.TipoTurnoMapper;
import com.minacontrol.turnos.repository.AsignacionTurnoRepository;
import com.minacontrol.turnos.repository.TipoTurnoRepository;
import com.minacontrol.turnos.service.ITurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurnoServiceImpl implements ITurnoService {

    private final TipoTurnoRepository tipoTurnoRepository;
    private final AsignacionTurnoRepository asignacionTurnoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final TipoTurnoMapper tipoTurnoMapper;
    private final AsignacionTurnoMapper asignacionTurnoMapper;

    @Override
    @Transactional
    public TipoTurnoDTO crearTipoTurno(TipoTurnoCreateDTO createDTO) {
        if (createDTO.horaInicio().isAfter(createDTO.horaFin())) {
            throw new TurnoInvalidoException("La hora de inicio no puede ser posterior a la hora de fin.");
        }
        tipoTurnoRepository.findByNombre(createDTO.nombre()).ifPresent(t -> {
            throw new TurnoAlreadyExistsException("Ya existe un tipo de turno con el nombre '" + createDTO.nombre() + "'.");
        });

        TipoTurno tipoTurno = tipoTurnoMapper.toEntity(createDTO);
        TipoTurno savedTurno = tipoTurnoRepository.save(tipoTurno);
        return tipoTurnoMapper.toDTO(savedTurno);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoTurnoDTO> listarTodosLosTiposDeTurno() {
        return tipoTurnoRepository.findAll().stream()
                .map(tipoTurnoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TipoTurnoDTO obtenerTipoTurnoPorId(Long id) {
        return tipoTurnoRepository.findById(id)
                .map(tipoTurnoMapper::toDTO)
                .orElseThrow(() -> new TurnoNoEncontradoException("Tipo de turno no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public TipoTurnoDTO actualizarTipoTurno(Long id, TipoTurnoCreateDTO updateDTO) {
        TipoTurno tipoTurno = tipoTurnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNoEncontradoException("Tipo de turno no encontrado con ID: " + id));

        tipoTurnoRepository.findByNombre(updateDTO.nombre()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new TurnoAlreadyExistsException("El nombre '" + updateDTO.nombre() + "' ya está en uso por otro turno.");
            }
        });

        tipoTurnoMapper.updateEntityFromDTO(updateDTO, tipoTurno);
        TipoTurno updatedTurno = tipoTurnoRepository.save(tipoTurno);
        return tipoTurnoMapper.toDTO(updatedTurno);
    }

    @Override
    @Transactional
    public void eliminarTipoTurno(Long id) {
        TipoTurno tipoTurno = tipoTurnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNoEncontradoException("Tipo de turno no encontrado con ID: " + id));

        if (asignacionTurnoRepository.existsByTipoTurnoId(id)) {
            throw new IllegalStateException("No se puede eliminar el tipo de turno porque tiene empleados asignados.");
        }

        tipoTurnoRepository.delete(tipoTurno);
    }

    @Override
    @Transactional
    public AsignacionTurnoDTO asignarEmpleadoATurno(AsignacionTurnoCreateDTO createDTO) {
        Empleado empleado = empleadoRepository.findById(createDTO.empleadoId())
                .orElseThrow(() -> new com.minacontrol.empleado.exception.EmpleadoNotFoundException("Empleado no encontrado con ID: " + createDTO.empleadoId()));
        if (empleado.getEstado() != com.minacontrol.empleado.enums.EstadoEmpleado.ACTIVO) {
            throw new AsignacionTurnoInvalidaException("El empleado no está activo.");
        }

        TipoTurno tipoTurno = tipoTurnoRepository.findById(createDTO.tipoTurnoId())
                .orElseThrow(() -> new com.minacontrol.turnos.exception.TurnoNoEncontradoException("Tipo de turno no encontrado con ID: " + createDTO.tipoTurnoId()));
        if (!tipoTurno.isActivo()) {
            throw new AsignacionTurnoInvalidaException("El tipo de turno no está activo.");
        }

        if (createDTO.fechaInicio().isAfter(createDTO.fechaFin())) {
            throw new AsignacionTurnoInvalidaException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        List<AsignacionTurno> conflictos = asignacionTurnoRepository.findConflictosDeHorario(
                createDTO.empleadoId(), createDTO.fechaInicio(), createDTO.fechaFin());

        if (!conflictos.isEmpty()) {
            throw new AsignacionTurnoInvalidaException("El empleado ya tiene una asignación en el rango de fechas seleccionado.");
        }

        AsignacionTurno nuevaAsignacion = asignacionTurnoMapper.toEntity(createDTO);
        AsignacionTurno savedAsignacion = asignacionTurnoRepository.save(nuevaAsignacion);

        return asignacionTurnoMapper.toDTO(savedAsignacion);
    }
}
