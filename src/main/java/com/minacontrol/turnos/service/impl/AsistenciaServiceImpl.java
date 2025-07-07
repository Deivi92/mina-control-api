
package com.minacontrol.turnos.service.impl;

import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.empleado.exception.EmpleadoNoEncontradoException;
import com.minacontrol.turnos.dto.request.ExcepcionAsistenciaDTO;
import com.minacontrol.turnos.dto.request.RegistrarAsistenciaDTO;
import com.minacontrol.turnos.dto.response.RegistroAsistenciaDTO;
import com.minacontrol.turnos.entity.RegistroAsistencia;
import com.minacontrol.turnos.enums.TipoRegistro;
import com.minacontrol.turnos.exception.AsignacionTurnoInvalidaException;
import com.minacontrol.turnos.exception.RegistroAsistenciaInvalidoException;
import com.minacontrol.turnos.mapper.RegistroAsistenciaMapper;
import com.minacontrol.turnos.repository.AsignacionTurnoRepository;
import com.minacontrol.turnos.repository.RegistroAsistenciaRepository;
import com.minacontrol.turnos.service.IAsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AsistenciaServiceImpl implements IAsistenciaService {

    private final RegistroAsistenciaRepository registroAsistenciaRepository;
    private final AsignacionTurnoRepository asignacionTurnoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final RegistroAsistenciaMapper registroAsistenciaMapper;

    @Override
    public RegistroAsistenciaDTO registrarAsistencia(RegistrarAsistenciaDTO registrarAsistenciaDTO) {
        asignacionTurnoRepository.findConflictosDeHorario(
                registrarAsistenciaDTO.empleadoId(),
                LocalDate.now(),
                LocalDate.now()
        ).stream().findFirst().orElseThrow(() -> new AsignacionTurnoInvalidaException("El empleado no tiene un turno asignado para hoy."));

        var registroExistente = registroAsistenciaRepository.findByEmpleadoIdAndFecha(registrarAsistenciaDTO.empleadoId(), LocalDate.now());

        if (registrarAsistenciaDTO.tipoRegistro() == TipoRegistro.ENTRADA) {
            if (registroExistente.isPresent()) {
                throw new RegistroAsistenciaInvalidoException("Ya existe una entrada registrada hoy.");
            }
            var nuevoRegistro = new RegistroAsistencia();
            nuevoRegistro.setEmpleadoId(registrarAsistenciaDTO.empleadoId());
            nuevoRegistro.setFecha(LocalDate.now());
            nuevoRegistro.setHoraEntrada(LocalTime.now());
            nuevoRegistro.setEstado(registrarAsistenciaDTO.tipoRegistro());
            return registroAsistenciaMapper.toDTO(registroAsistenciaRepository.save(nuevoRegistro));
        } else { // SALIDA
            var registro = registroExistente.orElseThrow(() -> new RegistroAsistenciaInvalidoException("No se puede registrar salida sin una entrada previa."));
            registro.setHoraSalida(LocalTime.now());
            registro.setEstado(registrarAsistenciaDTO.tipoRegistro());
            if (registro.getHoraEntrada() != null) {
                double horas = Duration.between(registro.getHoraEntrada(), registro.getHoraSalida()).toMinutes() / 60.0;
                registro.setHorasTrabajadas(horas);
            }
            return registroAsistenciaMapper.toDTO(registroAsistenciaRepository.save(registro));
        }
    }

    @Override
    public List<RegistroAsistenciaDTO> consultarAsistencia(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        List<RegistroAsistencia> registros;
        if (empleadoId != null) {
            registros = registroAsistenciaRepository.findByEmpleadoIdAndFechaBetween(empleadoId, fechaInicio, fechaFin);
        } else {
            registros = registroAsistenciaRepository.findByFechaBetween(fechaInicio, fechaFin);
        }
        return registros.stream()
                .map(registroAsistenciaMapper::toDTO)
                .toList();
    }

    @Override
    public RegistroAsistenciaDTO gestionarExcepcionAsistencia(ExcepcionAsistenciaDTO excepcionAsistenciaDTO) {
        empleadoRepository.findById(excepcionAsistenciaDTO.empleadoId()).orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado no encontrado"));

        var registro = registroAsistenciaRepository.findByEmpleadoIdAndFecha(excepcionAsistenciaDTO.empleadoId(), excepcionAsistenciaDTO.fecha())
                .orElse(new RegistroAsistencia());

        registro.setEmpleadoId(excepcionAsistenciaDTO.empleadoId());
        registro.setFecha(excepcionAsistenciaDTO.fecha());
        registro.setEstado(TipoRegistro.valueOf(excepcionAsistenciaDTO.tipoExcepcion().name()));
        registro.setMotivo(excepcionAsistenciaDTO.motivo());

        return registroAsistenciaMapper.toDTO(registroAsistenciaRepository.save(registro));
    }
}

