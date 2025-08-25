package com.minacontrol.autenticacion.service.impl;

import com.minacontrol.autenticacion.dto.request.RecuperarContrasenaRequestDTO;
import com.minacontrol.autenticacion.exception.UsuarioNoEncontradoException;
import com.minacontrol.autenticacion.model.Usuario;
import com.minacontrol.autenticacion.repository.UsuarioRepository;
import com.minacontrol.autenticacion.service.IServicioRecuperacionContrasena;
import com.minacontrol.shared.service.IServicioCorreo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ServicioRecuperacionContrasenaImpl implements IServicioRecuperacionContrasena {

    private final UsuarioRepository usuarioRepository;
    private final IServicioCorreo servicioCorreo;

    public ServicioRecuperacionContrasenaImpl(UsuarioRepository usuarioRepository, IServicioCorreo servicioCorreo) {
        this.usuarioRepository = usuarioRepository;
        this.servicioCorreo = servicioCorreo;
    }

    @Override
    @Transactional
    public void iniciarRecuperacion(RecuperarContrasenaRequestDTO recuperarContrasenaRequestDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(recuperarContrasenaRequestDTO.email());

        if (usuarioOptional.isEmpty()) {
            // Por seguridad, no revelamos si el email existe o no.
            // Simplemente registramos el intento y retornamos.
            System.out.println("Intento de recuperación de contraseña para email no registrado: " + recuperarContrasenaRequestDTO.email());
            return;
        }

        Usuario usuario = usuarioOptional.get();
        String resetToken = UUID.randomUUID().toString();
        // TODO: Implementar lógica para establecer la expiración del token (ej. 24 horas)
        usuario.setResetToken(resetToken);
        usuario.setResetTokenExpiry(System.currentTimeMillis() + (24 * 60 * 60 * 1000)); // 24 horas
        usuarioRepository.save(usuario);
        servicioCorreo.enviarCorreoRecuperacion(usuario, resetToken);
    }
}
