package com.minacontrol.autenticacion.service.impl;

import com.minacontrol.autenticacion.dto.CambiarContrasenaRequestDTO;
import com.minacontrol.autenticacion.exception.ContrasenaInvalidaException;
import com.minacontrol.autenticacion.exception.TokenInvalidoException;
import com.minacontrol.autenticacion.model.Usuario;
import com.minacontrol.autenticacion.repository.UsuarioRepository;
import com.minacontrol.autenticacion.service.IServicioCambioContrasena;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ServicioCambioContrasenaImpl implements IServicioCambioContrasena {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ServicioCambioContrasenaImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void cambiarContrasena(CambiarContrasenaRequestDTO cambiarContrasenaRequestDTO) {
        // Flujo de recuperación de contraseña (con token)
        if (cambiarContrasenaRequestDTO.token() != null && !cambiarContrasenaRequestDTO.token().isEmpty()) {
            Usuario usuario = usuarioRepository.findByResetToken(cambiarContrasenaRequestDTO.token())
                    .orElseThrow(() -> new TokenInvalidoException("El token de reseteo es inválido o ha expirado."));

            // TODO: Validar que el token no haya expirado
            if (usuario.getResetTokenExpiry() != null && usuario.getResetTokenExpiry() < System.currentTimeMillis()) {
                throw new TokenInvalidoException("El token de reseteo ha expirado.");
            }

            usuario.setPassword(passwordEncoder.encode(cambiarContrasenaRequestDTO.newPassword()));
            usuario.setResetToken(null); // Invalidar token
            usuario.setResetTokenExpiry(null);
            usuarioRepository.save(usuario);
        } else {
            // Este método no debería ser llamado sin un email de usuario autenticado
            throw new IllegalArgumentException("Se requiere un token o el email del usuario autenticado para cambiar la contraseña.");
        }
    }

    @Override
    @Transactional
    public void cambiarContrasena(CambiarContrasenaRequestDTO cambiarContrasenaRequestDTO, String emailUsuarioAutenticado) {
        // Flujo de cambio de contraseña (con contraseña actual)
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioAutenticado)
                .orElseThrow(() -> new ContrasenaInvalidaException("Usuario no encontrado.")); // Esto no debería ocurrir si el usuario está autenticado

        if (!passwordEncoder.matches(cambiarContrasenaRequestDTO.oldPassword(), usuario.getPassword())) {
            throw new ContrasenaInvalidaException("La contraseña actual es incorrecta.");
        }

        usuario.setPassword(passwordEncoder.encode(cambiarContrasenaRequestDTO.newPassword()));
        usuarioRepository.save(usuario);
    }
}
