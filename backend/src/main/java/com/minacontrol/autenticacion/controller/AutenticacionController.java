package com.minacontrol.autenticacion.controller;

import com.minacontrol.autenticacion.dto.request.CambiarContrasenaRequestDTO;
import com.minacontrol.autenticacion.dto.request.LoginRequestDTO;
import com.minacontrol.autenticacion.dto.response.LoginResponseDTO;
import com.minacontrol.autenticacion.dto.request.RefreshTokenRequestDTO;
import com.minacontrol.autenticacion.dto.response.RefreshTokenResponseDTO;
import com.minacontrol.autenticacion.dto.request.RegistroUsuarioCreateDTO;
import com.minacontrol.autenticacion.dto.request.LogoutRequestDTO;
import com.minacontrol.autenticacion.dto.request.RecuperarContrasenaRequestDTO;
import com.minacontrol.autenticacion.dto.response.UsuarioDTO;
import com.minacontrol.autenticacion.service.IServicioAutenticacion;
import com.minacontrol.autenticacion.service.IServicioCambioContrasena;
import com.minacontrol.autenticacion.service.IServicioRecuperacionContrasena;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<Void> logoutUsuario(@Valid @RequestBody LogoutRequestDTO logoutRequestDTO) {
        servicioAutenticacion.logoutUsuario(logoutRequestDTO.refreshToken());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        RefreshTokenResponseDTO response = servicioAutenticacion.refreshToken(refreshTokenRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recover-password")
    public ResponseEntity<Void> recuperarContrasena(@Valid @RequestBody RecuperarContrasenaRequestDTO recuperarContrasenaRequestDTO) {
        servicioRecuperacionContrasena.iniciarRecuperacion(recuperarContrasenaRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> cambiarContrasena(@RequestBody CambiarContrasenaRequestDTO cambiarContrasenaRequestDTO) {
        if (cambiarContrasenaRequestDTO.token() != null && !cambiarContrasenaRequestDTO.token().isEmpty()) {
            // Flujo de recuperación de contraseña (con token)
            if (cambiarContrasenaRequestDTO.newPassword() == null || cambiarContrasenaRequestDTO.newPassword().isEmpty()) {
                throw new IllegalArgumentException("La nueva contraseña no puede estar vacía.");
            }
            servicioCambioContrasena.cambiarContrasena(cambiarContrasenaRequestDTO);
        } else if (cambiarContrasenaRequestDTO.oldPassword() != null && !cambiarContrasenaRequestDTO.oldPassword().isEmpty()) {
            // Flujo de cambio de contraseña (con contraseña actual)
            if (cambiarContrasenaRequestDTO.newPassword() == null || cambiarContrasenaRequestDTO.newPassword().isEmpty()) {
                throw new IllegalArgumentException("La nueva contraseña no puede estar vacía.");
            }
            String emailUsuarioAutenticado = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            servicioCambioContrasena.cambiarContrasena(cambiarContrasenaRequestDTO, emailUsuarioAutenticado);
        } else {
            throw new IllegalArgumentException("Se requiere la contraseña actual o un token para cambiar la contraseña.");
        }
        return ResponseEntity.ok().build();
    }
}
