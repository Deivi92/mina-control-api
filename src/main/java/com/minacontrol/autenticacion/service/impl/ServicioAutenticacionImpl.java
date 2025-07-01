package com.minacontrol.autenticacion.service.impl;

import com.minacontrol.autenticacion.dto.LoginRequestDTO;
import com.minacontrol.autenticacion.dto.LoginResponseDTO;
import com.minacontrol.autenticacion.dto.RefreshTokenRequestDTO;
import com.minacontrol.autenticacion.dto.RefreshTokenResponseDTO;
import com.minacontrol.autenticacion.dto.RegistroUsuarioCreateDTO;
import com.minacontrol.autenticacion.dto.UsuarioDTO;
import com.minacontrol.autenticacion.exception.UsuarioYaExisteException;
import com.minacontrol.autenticacion.model.Usuario;
import com.minacontrol.autenticacion.repository.UsuarioRepository;
import com.minacontrol.autenticacion.service.IServicioAutenticacion;
import com.minacontrol.empleado.exception.EmpleadoNotFoundException;
import com.minacontrol.empleado.model.Empleado;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ServicioAutenticacionImpl implements IServicioAutenticacion {

    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    // private final JwtService jwtService; // Asumimos que existirá un servicio JWT

    public ServicioAutenticacionImpl(EmpleadoRepository empleadoRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder /*, JwtService jwtService*/) {
        this.empleadoRepository = empleadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        // this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public UsuarioDTO registrarUsuario(RegistroUsuarioCreateDTO registroUsuarioCreateDTO) {
        // 1. Verificar que el email corresponde a un empleado existente y activo
        Empleado empleado = empleadoRepository.findByEmail(registroUsuarioCreateDTO.email())
                .orElseThrow(() -> new EmpleadoNotFoundException("El empleado con el email proporcionado no fue encontrado."));

        // 2. Verificar que el empleado no tiene ya una cuenta de usuario
        if (empleado.getTieneUsuario()) {
            throw new UsuarioYaExisteException("El empleado ya tiene una cuenta.");
        }

        // 3. Verificar que el email no esté ya en uso como usuario
        usuarioRepository.findByEmail(registroUsuarioCreateDTO.email()).ifPresent(u -> {
            throw new UsuarioYaExisteException("El email ya está en uso.");
        });

        // 4. Crear hash de la contraseña
        String hashedPassword = passwordEncoder.encode(registroUsuarioCreateDTO.password());

        // 5. Crear y persistir la nueva entidad Usuario
        Usuario nuevoUsuario = Usuario.builder()
                .email(registroUsuarioCreateDTO.email())
                .password(hashedPassword)
                .empleado(empleado)
                .build();
        
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // 6. Actualizar el estado del empleado
        empleado.setTieneUsuario(true);
        empleadoRepository.save(empleado); // Guardar el empleado actualizado

        // 7. Devolver DTO del nuevo usuario
        return new UsuarioDTO(usuarioGuardado.getId(), usuarioGuardado.getEmail());
    }

    @Override
    public LoginResponseDTO loginUsuario(LoginRequestDTO loginRequestDTO) {
        // Implementación pendiente: Lógica de autenticación y generación de JWT
        throw new UnsupportedOperationException("Unimplemented method 'loginUsuario'");
    }

    @Override
    public void logoutUsuario(String refreshToken) {
        // Implementación pendiente: Invalidación de refresh token
        throw new UnsupportedOperationException("Unimplemented method 'logoutUsuario'");
    }

    @Override
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        // Implementación pendiente: Generación de nuevo access token usando refresh token
        throw new UnsupportedOperationException("Unimplemented method 'refreshToken'");
    }
}
