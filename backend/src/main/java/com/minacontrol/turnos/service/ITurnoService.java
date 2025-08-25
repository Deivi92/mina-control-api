
package com.minacontrol.turnos.service;

import com.minacontrol.turnos.dto.request.AsignacionTurnoCreateDTO;
import com.minacontrol.turnos.dto.request.TipoTurnoCreateDTO;
import com.minacontrol.turnos.dto.response.AsignacionTurnoDTO;
import com.minacontrol.turnos.dto.response.TipoTurnoDTO;

import java.util.List;

public interface ITurnoService {
    TipoTurnoDTO crearTipoTurno(TipoTurnoCreateDTO createDTO);
    List<TipoTurnoDTO> listarTodosLosTiposDeTurno();
    TipoTurnoDTO obtenerTipoTurnoPorId(Long id);
    TipoTurnoDTO actualizarTipoTurno(Long id, TipoTurnoCreateDTO updateDTO);
    void eliminarTipoTurno(Long id);
    AsignacionTurnoDTO asignarEmpleadoATurno(AsignacionTurnoCreateDTO createDTO);
}
