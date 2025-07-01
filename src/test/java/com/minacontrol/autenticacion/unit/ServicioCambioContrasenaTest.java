package com.minacontrol.autenticacion.unit;

import com.minacontrol.autenticacion.model.Usuario;
import com.minacontrol.autenticacion.repository.UsuarioRepository;
import com.minacontrol.autenticacion.service.IServicioCambioContrasena;
import com.minacontrol.autenticacion.service.impl.ServicioCambioContrasenaImpl;
import com.minacontrol.autenticacion.dto.request.CambiarContrasenaRequestDTO;
import com.minacontrol.autenticacion.exception.ContrasenaInvalidaException;
import com.minacontrol.autenticacion.exception.TokenInvalidoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServicioCambioContrasenaTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ServicioCambioContrasenaImpl servicioCambioContrasena;

    private CambiarContrasenaRequestDTO cambiarContrasenaRequestDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("usuario@example.com");
        usuario.setPassword("hashedOldPassword");
        // Asume que Usuario tiene un método para establecer el token de reseteo y su expiración
    }

    @Test
    @DisplayName("deberiaCambiarContrasenaConTokenExitosamente - Debería cambiar la contraseña usando un token de reseteo exitosamente")
    void deberiaCambiarContrasenaConTokenExitosamente() {
        // Arrange
        cambiarContrasenaRequestDTO = new CambiarContrasenaRequestDTO(null, "NewPassword123!", "tokenValido");
        when(usuarioRepository.findByResetToken("tokenValido")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("NewPassword123!")).thenReturn("hashedNewPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        servicioCambioContrasena.cambiarContrasena(cambiarContrasenaRequestDTO);

        // Assert
        verify(usuarioRepository, times(1)).findByResetToken("tokenValido");
        verify(passwordEncoder, times(1)).encode("NewPassword123!");
        verify(usuarioRepository, times(1)).save(usuario);
        assertNull(usuario.getResetToken()); // Verifica que el token se invalida
    }

    @Test
    @DisplayName("deberiaCambiarContrasenaConContrasenaActualExitosamente - Debería cambiar la contraseña usando la contraseña actual exitosamente")
    void deberiaCambiarContrasenaConContrasenaActualExitosamente() {
        // Arrange
        cambiarContrasenaRequestDTO = new CambiarContrasenaRequestDTO("OldPassword123!", "NewPassword123!", null);
        // Simula un usuario autenticado (esto se manejaría en la capa de controlador/seguridad real)
        // Para la prueba unitaria, asumimos que el usuario ya está disponible o se obtiene de algún contexto
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario)); // Asume que el servicio obtiene el usuario autenticado
        when(passwordEncoder.matches("OldPassword123!", "hashedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("NewPassword123!")).thenReturn("hashedNewPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        // En un escenario real, el email del usuario autenticado se pasaría al servicio
        servicioCambioContrasena.cambiarContrasena(cambiarContrasenaRequestDTO, usuario.getEmail());

        // Assert
        verify(usuarioRepository, times(1)).findByEmail(usuario.getEmail());
        verify(passwordEncoder, times(1)).matches("OldPassword123!", "hashedOldPassword");
        verify(passwordEncoder, times(1)).encode("NewPassword123!");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("lanzarContrasenaInvalidaExceptionSiContrasenaActualIncorrecta - Debería lanzar ContrasenaInvalidaException si la contraseña actual es incorrecta")
    void lanzarContrasenaInvalidaExceptionSiContrasenaActualIncorrecta() {
        // Arrange
        cambiarContrasenaRequestDTO = new CambiarContrasenaRequestDTO("WrongOldPassword!", "NewPassword123!", null);
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("WrongOldPassword!", "hashedOldPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(ContrasenaInvalidaException.class, () -> {
            servicioCambioContrasena.cambiarContrasena(cambiarContrasenaRequestDTO, usuario.getEmail());
        });
        verify(usuarioRepository, times(1)).findByEmail(usuario.getEmail());
        verify(passwordEncoder, times(1)).matches("WrongOldPassword!", "hashedOldPassword");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("lanzarTokenInvalidoExceptionSiTokenInvalido - Debería lanzar TokenInvalidoException si el token de reseteo es inválido")
    void lanzarTokenInvalidoExceptionSiTokenInvalido() {
        // Arrange
        cambiarContrasenaRequestDTO = new CambiarContrasenaRequestDTO(null, "NewPassword123!", "tokenInvalido");
        when(usuarioRepository.findByResetToken("tokenInvalido")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TokenInvalidoException.class, () -> {
            servicioCambioContrasena.cambiarContrasena(cambiarContrasenaRequestDTO);
        });
        verify(usuarioRepository, times(1)).findByResetToken("tokenInvalido");
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
