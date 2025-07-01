package com.minacontrol.autenticacion.service;

import com.minacontrol.autenticacion.dto.RecuperarContrasenaRequestDTO;

public interface IServicioRecuperacionContrasena {
    void iniciarRecuperacion(RecuperarContrasenaRequestDTO recuperarContrasenaRequestDTO);
}
