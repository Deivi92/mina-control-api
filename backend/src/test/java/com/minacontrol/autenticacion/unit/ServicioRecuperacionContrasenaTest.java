package com.minacontrol.autenticacion.unit;

import com.minacontrol.autenticacion.exception.UsuarioNoEncontradoException;
import com.minacontrol.autenticacion.model.Usuario;
import com.minacontrol.autenticacion.repository.UsuarioRepository;
import com.minacontrol.autenticacion.service.impl.ServicioRecuperacionContrasenaImpl;
import com.minacontrol.autenticacion.dto.request.RecuperarContrasenaRequestDTO;
import com.minacontrol.shared.service.IServicioCorreo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioRecuperacionContrasenaTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    // Asumimos que hay un servicio para enviar correos, lo mockeamos
    @Mock
    private IServicioCorreo servicioCorreo;

    @InjectMocks
    private ServicioRecuperacionContrasenaImpl servicioRecuperacionContrasena;

    private RecuperarContrasenaRequestDTO recuperarContrasenaRequestDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        recuperarContrasenaRequestDTO = new RecuperarContrasenaRequestDTO("usuario@example.com");
        usuario = new Usuario();
        usuario.setEmail("usuario@example.com");
        // Asume que Usuario tiene un método para establecer el token de reseteo y su expiración
    }

    @Test
    @DisplayName("deberiaIniciarRecuperacionContrasenaExitosamente - Debería iniciar la recuperación de contraseña exitosamente")
    void deberiaIniciarRecuperacionContrasenaExitosamente() {
        // Arrange
        when(usuarioRepository.findByEmail(recuperarContrasenaRequestDTO.email())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        servicioRecuperacionContrasena.iniciarRecuperacion(recuperarContrasenaRequestDTO);

        // Assert
        verify(usuarioRepository, times(1)).findByEmail(recuperarContrasenaRequestDTO.email());
        verify(usuarioRepository, times(1)).save(any(Usuario.class)); // Verifica que el token se guarda
        verify(servicioCorreo, times(1)).enviarCorreoRecuperacion(eq(usuario), anyString()); // Verifica que se envía el correo
    }

    
}
