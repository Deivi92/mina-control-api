package com.minacontrol.shared.service.impl;

import com.minacontrol.autenticacion.model.Usuario;
import com.minacontrol.shared.service.IServicioCorreo;
import org.springframework.stereotype.Service;

@Service
public class ServicioCorreoImpl implements IServicioCorreo {

    @Override
    public void enviarCorreoRecuperacion(Usuario usuario, String token) {
        // Lógica para enviar correo de recuperación (placeholder)
        System.out.println("Enviando correo de recuperación a: " + usuario.getEmail() + " con token: " + token);
    }
}
