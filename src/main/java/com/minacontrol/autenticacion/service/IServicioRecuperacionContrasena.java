package com.minacontrol.autenticacion.service;

import com.minacontrol.autenticacion.dto.request.RecuperarContrasenaRequestDTO;

public interface IServicioRecuperacionContrasena {
    void iniciarRecuperacion(RecuperarContrasenaRequestDTO recuperarContrasenaRequestDTO);
}
