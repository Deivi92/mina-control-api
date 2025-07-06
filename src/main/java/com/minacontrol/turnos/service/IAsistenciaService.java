
package com.minacontrol.turnos.service;

import com.minacontrol.turnos.dto.request.RegistrarAsistenciaDTO;
import com.minacontrol.turnos.dto.response.RegistroAsistenciaDTO;

public interface IAsistenciaService {
    RegistroAsistenciaDTO registrarAsistencia(RegistrarAsistenciaDTO registrarAsistenciaDTO);
}
