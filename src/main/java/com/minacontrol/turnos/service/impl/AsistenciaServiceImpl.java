
package com.minacontrol.turnos.service.impl;

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

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AsistenciaServiceImpl implements IAsistenciaService {

    private final RegistroAsistenciaRepository registroAsistenciaRepository;
    private final AsignacionTurnoRepository asignacionTurnoRepository;
    private final RegistroAsistenciaMapper registroAsistenciaMapper;

    @Override
    public RegistroAsistenciaDTO registrarAsistencia(RegistrarAsistenciaDTO registrarAsistenciaDTO) {
        LocalDate hoy = LocalDate.now();
        asignacionTurnoRepository.findConflictosDeHorario(registrarAsistenciaDTO.empleadoId(), hoy, hoy)
                .stream().findFirst()
                .orElseThrow(() -> new AsignacionTurnoInvalidaException("El empleado no tiene un turno asignado para hoy."));

        if (registrarAsistenciaDTO.tipoRegistro() == TipoRegistro.ENTRADA) {
            registroAsistenciaRepository.findByEmpleadoIdAndFecha(registrarAsistenciaDTO.empleadoId(), hoy)
                    .ifPresent(r -> {
                        throw new RegistroAsistenciaInvalidoException("Ya existe una entrada registrada hoy.");
                    });

            RegistroAsistencia nuevoRegistro = RegistroAsistencia.builder()
                    .empleadoId(registrarAsistenciaDTO.empleadoId())
                    .fecha(hoy)
                    .horaEntrada(LocalTime.now())
                    .estado(TipoRegistro.ENTRADA)
                    .build();
            return registroAsistenciaMapper.toDTO(registroAsistenciaRepository.save(nuevoRegistro));
        } else { // SALIDA
            RegistroAsistencia registro = registroAsistenciaRepository.findByEmpleadoIdAndFecha(registrarAsistenciaDTO.empleadoId(), hoy)
                    .orElseThrow(() -> new RegistroAsistenciaInvalidoException("No se puede registrar salida sin una entrada previa."));

            if (registro.getHoraSalida() != null) {
                throw new RegistroAsistenciaInvalidoException("Ya se ha registrado una salida para este turno.");
            }

            registro.setHoraSalida(LocalTime.now());
            registro.setEstado(TipoRegistro.SALIDA);
            // TODO: Calcular horas trabajadas
            return registroAsistenciaMapper.toDTO(registroAsistenciaRepository.save(registro));
        }
    }
}
