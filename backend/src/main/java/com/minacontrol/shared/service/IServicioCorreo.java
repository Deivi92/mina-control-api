package com.minacontrol.shared.service;

import com.minacontrol.autenticacion.model.Usuario;

public interface IServicioCorreo {
    void enviarCorreoRecuperacion(Usuario usuario, String token);
}