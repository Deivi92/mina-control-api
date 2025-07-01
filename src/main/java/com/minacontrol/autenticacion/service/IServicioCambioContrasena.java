package com.minacontrol.autenticacion.service;

import com.minacontrol.autenticacion.dto.request.CambiarContrasenaRequestDTO;

public interface IServicioCambioContrasena {
    void cambiarContrasena(CambiarContrasenaRequestDTO cambiarContrasenaRequestDTO);
    void cambiarContrasena(CambiarContrasenaRequestDTO cambiarContrasenaRequestDTO, String emailUsuarioAutenticado);
}
