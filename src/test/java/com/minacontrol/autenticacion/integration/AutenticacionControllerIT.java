package com.minacontrol.autenticacion.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.autenticacion.dto.request.LoginRequestDTO;
import com.minacontrol.autenticacion.dto.request.RecuperarContrasenaRequestDTO;
import com.minacontrol.autenticacion.dto.request.RegistroUsuarioCreateDTO;
import com.minacontrol.autenticacion.model.Usuario;
import com.minacontrol.autenticacion.repository.UsuarioRepository;
import com.minacontrol.empleado.model.Empleado;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Pruebas de Integración para AutenticacionController")
public class AutenticacionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- Configuración General ---

    @BeforeEach
    void setUp() {
        // Limpieza de la BD antes de cada prueba
        usuarioRepository.deleteAll();
        empleadoRepository.deleteAll();
    }

    // --- Clases Anidadas para cada Endpoint ---

    @Nested
    @DisplayName("Endpoint: POST /api/auth/register")
    class RegisterTests {

        @BeforeEach
        void registerSetUp() {
            // Crear un empleado base para las pruebas de registro
            Empleado empleado = new Empleado();
            empleado.setEmail("empleado@example.com");
            empleado.setTieneUsuario(false);
            empleadoRepository.save(empleado);
        }

        @Test
        @DisplayName("Debería registrar un usuario exitosamente")
        void deberiaRegistrarUsuarioExitosamente() throws Exception {
            RegistroUsuarioCreateDTO registroDTO = new RegistroUsuarioCreateDTO("empleado@example.com", "Password123!");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registroDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.email").value("empleado@example.com"));

            Optional<Usuario> usuarioGuardado = usuarioRepository.findByEmail("empleado@example.com");
            assertThat(usuarioGuardado).isPresent();
            Optional<Empleado> empleadoActualizado = empleadoRepository.findByEmail("empleado@example.com");
            assertThat(empleadoActualizado).isPresent();
            assertThat(empleadoActualizado.get().getTieneUsuario()).isTrue();
        }

        @Test
        @DisplayName("Debería retornar 409 Conflict si el empleado ya tiene una cuenta")
        void deberiaRetornarConflictoSiEmpleadoYaTieneCuenta() throws Exception {
            crearUsuarioParaTest("empleado@example.com", "Password123!");
            RegistroUsuarioCreateDTO registroDTO = new RegistroUsuarioCreateDTO("empleado@example.com", "AnotherPassword123!");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registroDTO)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").value("El empleado ya tiene una cuenta."));
        }

        @Test
        @DisplayName("Debería retornar 404 Not Found si el empleado no existe")
        void deberiaRetornarNotFoundSiEmpleadoNoExiste() throws Exception {
            RegistroUsuarioCreateDTO registroDTO = new RegistroUsuarioCreateDTO("noexiste@example.com", "Password123!");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registroDTO)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("El empleado con el email proporcionado no fue encontrado."));
        }
    }

    @Nested
    @DisplayName("Endpoint: POST /api/auth/login")
    class LoginTests {

        @BeforeEach
        void loginSetUp() {
            crearUsuarioParaTest("user@example.com", "Password123!");
        }

        @Test
        @DisplayName("Debería autenticar y retornar tokens con credenciales válidas")
        void deberiaAutenticarExitosamente() throws Exception {
            LoginRequestDTO loginDTO = new LoginRequestDTO("user@example.com", "Password123!");

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").isNotEmpty())
                    .andExpect(jsonPath("$.refreshToken").isNotEmpty());
        }

        @Test
        @DisplayName("Debería retornar 401 Unauthorized con contraseña incorrecta")
        void deberiaFallarLoginConContrasenaIncorrecta() throws Exception {
            LoginRequestDTO loginDTO = new LoginRequestDTO("user@example.com", "wrongpassword");

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDTO)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("Unauthorized"));
        }

        @Test
        @DisplayName("Debería retornar 401 Unauthorized si el usuario no existe")
        void deberiaFallarLoginSiUsuarioNoExiste() throws Exception {
            LoginRequestDTO loginDTO = new LoginRequestDTO("noexiste@example.com", "Password123!");

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDTO)))
                    .andExpect(status().isUnauthorized());
        }
    }
    
    @Nested
    @DisplayName("Endpoint: POST /api/auth/recover-password")
    class RecoverPasswordTests {

        @BeforeEach
        void recoverPasswordSetUp() {
            crearUsuarioParaTest("user@example.com", "Password123!");
        }

        @Test
        @DisplayName("Debería iniciar la recuperación para un email existente y generar token")
        void deberiaIniciarRecuperacionContrasena() throws Exception {
            RecuperarContrasenaRequestDTO recoverDTO = new RecuperarContrasenaRequestDTO("user@example.com");

            mockMvc.perform(post("/api/auth/recover-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(recoverDTO)))
                    .andExpect(status().isOk());

            Usuario usuario = usuarioRepository.findByEmail("user@example.com").get();
            assertThat(usuario.getResetToken()).isNotNull();
            assertThat(usuario.getResetTokenExpiry()).isNotNull();
        }

        @Test
        @DisplayName("Debería retornar 404 Not Found si el email no existe")
        void deberiaFallarRecuperacionSiEmailNoExiste() throws Exception {
            RecuperarContrasenaRequestDTO recoverDTO = new RecuperarContrasenaRequestDTO("noexiste@example.com");

            mockMvc.perform(post("/api/auth/recover-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(recoverDTO)))
                    .andExpect(status().isUnauthorized());
        }
    }

    // --- Métodos de Utilidad ---

    private void crearUsuarioParaTest(String email, String password) {
        Empleado empleado = empleadoRepository.findByEmail(email).orElseGet(() -> {
            Empleado nuevoEmpleado = new Empleado();
            nuevoEmpleado.setEmail(email);
            return empleadoRepository.save(nuevoEmpleado);
        });
        empleado.setTieneUsuario(true);
        empleadoRepository.save(empleado);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setEmpleado(empleado);
        usuarioRepository.save(usuario);
    }
}