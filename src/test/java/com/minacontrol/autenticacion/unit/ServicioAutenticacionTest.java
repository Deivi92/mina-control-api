package com.minacontrol.autenticacion.unit;

import com.minacontrol.empleado.model.Empleado;
import com.minacontrol.autenticacion.model.Usuario;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.autenticacion.repository.UsuarioRepository;
import com.minacontrol.autenticacion.service.IServicioAutenticacion;
import com.minacontrol.autenticacion.service.impl.ServicioAutenticacionImpl;
import com.minacontrol.empleado.exception.EmpleadoNotFoundException;
import com.minacontrol.autenticacion.exception.UsuarioYaExisteException;
import com.minacontrol.autenticacion.dto.request.RegistroUsuarioCreateDTO;
import com.minacontrol.autenticacion.dto.response.UsuarioDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioAutenticacionTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ServicioAutenticacionImpl servicioAutenticacion;

    private RegistroUsuarioCreateDTO registroUsuarioCreateDTO;
    private Empleado empleado;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        registroUsuarioCreateDTO = new RegistroUsuarioCreateDTO("test@example.com", "Password123!");
        
        empleado = new Empleado(); // Asume un constructor o setters para Empleado
        empleado.setEmail("test@example.com");
        empleado.setTieneUsuario(false); // Asume un setter para esta propiedad
        
        usuario = new Usuario(); // Asume un constructor o setters para Usuario
        usuario.setEmail("test@example.com");
        usuario.setPassword("hashedPassword");
        usuario.setEmpleado(empleado);
    }

    @Test
    @DisplayName("shouldRegisterUserSuccessfully - Debería registrar un usuario exitosamente")
    void deberiaRegistrarUsuarioExitosamente() {
        // Arrange
        when(empleadoRepository.findByEmail(registroUsuarioCreateDTO.email())).thenReturn(Optional.of(empleado));
        when(usuarioRepository.findByEmail(registroUsuarioCreateDTO.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registroUsuarioCreateDTO.password())).thenReturn("hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        UsuarioDTO result = servicioAutenticacion.registrarUsuario(registroUsuarioCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(registroUsuarioCreateDTO.email(), result.email());
        assertTrue(empleado.getTieneUsuario()); // Verifica que el empleado ahora tiene usuario
        verify(empleadoRepository, times(1)).findByEmail(registroUsuarioCreateDTO.email());
        verify(usuarioRepository, times(1)).findByEmail(registroUsuarioCreateDTO.email());
        verify(passwordEncoder, times(1)).encode(registroUsuarioCreateDTO.password());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("registerUser_nonExistentEmployeeEmail_throwsEmpleadoNotFoundException - Debería lanzar EmpleadoNotFoundException si el email del empleado no existe")
    void registrarUsuario_emailEmpleadoNoExistente_lanzaEmpleadoNotFoundException() {
        // Arrange
        when(empleadoRepository.findByEmail(registroUsuarioCreateDTO.email())).thenReturn(Optional.empty());

        // Act & Assert
        EmpleadoNotFoundException thrown = assertThrows(EmpleadoNotFoundException.class, () -> {
            servicioAutenticacion.registrarUsuario(registroUsuarioCreateDTO);
        });
        assertEquals("El empleado con el email proporcionado no fue encontrado.", thrown.getMessage());
        verify(empleadoRepository, times(1)).findByEmail(registroUsuarioCreateDTO.email());
        verify(usuarioRepository, never()).findByEmail(anyString()); // No debería llegar a buscar usuario
    }

    @Test
    @DisplayName("registerUser_employeeAlreadyHasAccount_throwsUsuarioYaExisteException - Debería lanzar UsuarioYaExisteException si el empleado ya tiene una cuenta")
    void registrarUsuario_empleadoYaTieneCuenta_lanzaUsuarioYaExisteException() {
        // Arrange
        empleado.setTieneUsuario(true); // Simula que el empleado ya tiene una cuenta
        when(empleadoRepository.findByEmail(registroUsuarioCreateDTO.email())).thenReturn(Optional.of(empleado));

        // Act & Assert
        UsuarioYaExisteException thrown = assertThrows(UsuarioYaExisteException.class, () -> {
            servicioAutenticacion.registrarUsuario(registroUsuarioCreateDTO);
        });
        assertEquals("El empleado ya tiene una cuenta.", thrown.getMessage());
        verify(empleadoRepository, times(1)).findByEmail(registroUsuarioCreateDTO.email());
        verify(usuarioRepository, never()).findByEmail(anyString()); // No debería llegar a buscar usuario
    }

    @Test
    @DisplayName("registerUser_emailAlreadyInUse_throwsUsuarioYaExisteException - Debería lanzar UsuarioYaExisteException si el email ya está en uso por otro usuario")
    void registrarUsuario_emailYaEnUso_lanzaUsuarioYaExisteException() {
        // Arrange
        when(empleadoRepository.findByEmail(registroUsuarioCreateDTO.email())).thenReturn(Optional.of(empleado));
        when(usuarioRepository.findByEmail(registroUsuarioCreateDTO.email())).thenReturn(Optional.of(new Usuario())); // Simula que el email ya está en uso

        // Act & Assert
        UsuarioYaExisteException thrown = assertThrows(UsuarioYaExisteException.class, () -> {
            servicioAutenticacion.registrarUsuario(registroUsuarioCreateDTO);
        });
        assertEquals("El email ya está en uso.", thrown.getMessage());
        verify(empleadoRepository, times(1)).findByEmail(registroUsuarioCreateDTO.email());
        verify(usuarioRepository, times(1)).findByEmail(registroUsuarioCreateDTO.email());
    }
}
