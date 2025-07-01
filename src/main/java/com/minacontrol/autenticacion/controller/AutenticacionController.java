package com.minacontrol.autenticacion.controller;

import com.minacontrol.autenticacion.dto.request.CambiarContrasenaRequestDTO;
import com.minacontrol.autenticacion.dto.request.LoginRequestDTO;
import com.minacontrol.autenticacion.dto.response.LoginResponseDTO;
import com.minacontrol.autenticacion.dto.request.RefreshTokenRequestDTO;
import com.minacontrol.autenticacion.dto.response.RefreshTokenResponseDTO;
import com.minacontrol.autenticacion.dto.request.RegistroUsuarioCreateDTO;
import com.minacontrol.autenticacion.dto.request.RecuperarContrasenaRequestDTO;
import com.minacontrol.autenticacion.dto.response.UsuarioDTO;
import com.minacontrol.autenticacion.service.IServicioAutenticacion;
import com.minacontrol.autenticacion.service.IServicioCambioContrasena;
import com.minacontrol.autenticacion.service.IServicioRecuperacionContrasena;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AutenticacionController {

    private final IServicioAutenticacion servicioAutenticacion;
    private final IServicioRecuperacionContrasena servicioRecuperacionContrasena;
    private final IServicioCambioContrasena servicioCambioContrasena;

    public AutenticacionController(IServicioAutenticacion servicioAutenticacion, IServicioRecuperacionContrasena servicioRecuperacionContrasena, IServicioCambioContrasena servicioCambioContrasena) {
        this.servicioAutenticacion = servicioAutenticacion;
        this.servicioRecuperacionContrasena = servicioRecuperacionContrasena;
        this.servicioCambioContrasena = servicioCambioContrasena;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> registrarUsuario(@Valid @RequestBody RegistroUsuarioCreateDTO registroUsuarioCreateDTO) {
        UsuarioDTO nuevoUsuario = servicioAutenticacion.registrarUsuario(registroUsuarioCreateDTO);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUsuario(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = servicioAutenticacion.loginUsuario(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUsuario(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        servicioAutenticacion.logoutUsuario(refreshTokenRequestDTO.refreshToken());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        RefreshTokenResponseDTO response = servicioAutenticacion.refreshToken(refreshTokenRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recover-password")
    public ResponseEntity<Void> recuperarContrasena(@Valid @RequestBody RecuperarContrasenaRequestDTO recuperarContrasenaRequestDTO) {
        servicioRecuperacionContrasena.iniciarRecuperacion(recuperarContrasenaRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> cambiarContrasena(@Valid @RequestBody CambiarContrasenaRequestDTO cambiarContrasenaRequestDTO) {
        // TODO: En un escenario real, se obtendría el email del usuario autenticado del contexto de seguridad
        // Por ahora, se asume que si no hay token, se intenta con el email (para pruebas o flujos específicos)
        if (cambiarContrasenaRequestDTO.token() != null && !cambiarContrasenaRequestDTO.token().isEmpty()) {
            servicioCambioContrasena.cambiarContrasena(cambiarContrasenaRequestDTO);
        } else {
            // Placeholder para el email del usuario autenticado. En un sistema real, se obtendría de Spring SecurityContext
            String emailUsuarioAutenticado = "usuario_autenticado@example.com"; // Esto debe ser dinámico
            servicioCambioContrasena.cambiarContrasena(cambiarContrasenaRequestDTO, emailUsuarioAutenticado);
        }
        return ResponseEntity.ok().build();
    }
}
