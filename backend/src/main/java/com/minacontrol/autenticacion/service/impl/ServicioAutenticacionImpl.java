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
import com.minacontrol.shared.security.jwt.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ServicioAutenticacionImpl implements IServicioAutenticacion {

    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public ServicioAutenticacionImpl(
            EmpleadoRepository empleadoRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            UserDetailsService userDetailsService,
            AuthenticationManager authenticationManager
    ) {
        this.empleadoRepository = empleadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
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
        // 1. Autenticar al usuario usando AuthenticationManager
        // Esto cargará los detalles del usuario y verificará la contraseña
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password())
        );

        // 2. Si la autenticación es exitosa, cargar los detalles del usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.email());

        // 3. Generar JWTs (Access Token y Refresh Token) reales
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        // 4. En un escenario completo, se guardaría el refresh token en la BD o en una cache.
        // Por simplicidad, lo dejamos así. En producción, se recomienda guardarlo.

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    @Override
    public void logoutUsuario(String refreshToken) {
        // En un escenario real, aquí se invalidaría el refresh token.
        // Esto podría ser añadiéndolo a una lista negra, eliminándolo de la BD, etc.
        // Por ahora, solo simulamos la invalidación.
        System.out.println("Refresh Token invalidado (simulado): " + refreshToken);
        // TODO: Implementar invalidación real (ej: agregar a lista negra)
    }

    @Override
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        // 1. Extraer el nombre de usuario del refresh token
        String username = jwtUtil.extractUsername(refreshTokenRequestDTO.refreshToken());

        // 2. Cargar los detalles del usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 3. Validar el refresh token
        if (jwtUtil.validateToken(refreshTokenRequestDTO.refreshToken(), userDetails)) {
            // 4. Generar nuevos tokens
            String newAccessToken = jwtUtil.generateAccessToken(userDetails);
            // Opcional: Generar un nuevo refresh token
            String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

            return new RefreshTokenResponseDTO(newAccessToken, newRefreshToken);
        } else {
            // 5. Si el token no es válido, lanzar una excepción
            throw new RuntimeException("Refresh token inválido o expirado");
            // En una implementación más robusta, se crearía una excepción personalizada.
        }
    }
}