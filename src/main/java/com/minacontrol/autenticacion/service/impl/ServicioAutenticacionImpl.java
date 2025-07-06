package com.minacontrol.autenticacion.service.impl;

import com.minacontrol.autenticacion.dto.request.LoginRequestDTO;
import com.minacontrol.autenticacion.dto.response.LoginResponseDTO;
import com.minacontrol.autenticacion.dto.request.RefreshTokenRequestDTO;
import com.minacontrol.autenticacion.dto.response.RefreshTokenResponseDTO;
import com.minacontrol.autenticacion.dto.request.RegistroUsuarioCreateDTO;
import com.minacontrol.autenticacion.dto.response.UsuarioDTO;
import com.minacontrol.autenticacion.exception.ContrasenaInvalidaException;
import com.minacontrol.autenticacion.exception.UsuarioNoEncontradoException;
import com.minacontrol.autenticacion.exception.UsuarioYaExisteException;
import com.minacontrol.autenticacion.model.Usuario;
import com.minacontrol.autenticacion.repository.UsuarioRepository;
import com.minacontrol.autenticacion.service.IServicioAutenticacion;
import com.minacontrol.empleado.exception.EmpleadoNotFoundException;
import com.minacontrol.empleado.entity.Empleado;
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
        if (empleado.isTieneUsuario()) {
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
        // 1. Buscar el usuario por email
        Usuario usuario = usuarioRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario o contraseña inválidos."));

        // 2. Verificar la contraseña
        if (!passwordEncoder.matches(loginRequestDTO.password(), usuario.getPassword())) {
            throw new ContrasenaInvalidaException("Usuario o contraseña inválidos.");
        }

        // 3. Generar JWTs (Access Token y Refresh Token)
        // TODO: Implementar la lógica real de generación de JWTs aquí
        String accessToken = "dummyAccessToken"; // Placeholder
        String refreshToken = "dummyRefreshToken"; // Placeholder

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    @Override
    public void logoutUsuario(String refreshToken) {
        // En un escenario real, aquí se invalidaría el refresh token.
        // Esto podría ser añadiéndolo a una lista negra, eliminándolo de la BD, etc.
        // Por ahora, solo simulamos la invalidación.
        System.out.println("Refresh Token invalidado (simulado): " + refreshToken);
    }

    @Override
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        // En un escenario real, aquí se validaría el refreshToken y se generaría un nuevo accessToken.
        // Por ahora, simulamos la generación de tokens.
        System.out.println("Generando nuevo Access Token para Refresh Token: " + refreshTokenRequestDTO.refreshToken());
        String newAccessToken = "newDummyAccessToken_" + System.currentTimeMillis();
        String newRefreshToken = "newDummyRefreshToken_" + System.currentTimeMillis();
        return new RefreshTokenResponseDTO(newAccessToken, newRefreshToken);
    }
}
