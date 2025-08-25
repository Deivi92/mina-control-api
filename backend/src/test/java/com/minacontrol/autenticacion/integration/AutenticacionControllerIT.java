package com.minacontrol.autenticacion.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.autenticacion.dto.request.CambiarContrasenaRequestDTO;
import com.minacontrol.autenticacion.dto.request.CambiarContrasenaRequestDTO;
import com.minacontrol.autenticacion.dto.request.LoginRequestDTO;
import com.minacontrol.autenticacion.dto.request.LogoutRequestDTO;
import com.minacontrol.autenticacion.dto.request.RecuperarContrasenaRequestDTO;
import com.minacontrol.autenticacion.dto.request.RegistroUsuarioCreateDTO;
import com.minacontrol.autenticacion.model.Usuario;
import com.minacontrol.autenticacion.repository.UsuarioRepository;
import com.minacontrol.empleado.entity.Empleado;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
            Empleado empleado = Empleado.builder()
                    .email("empleado@example.com")
                    .nombres("Reg")
                    .apellidos("User")
                    .numeroIdentificacion("REG_ID_123")
                    .fechaContratacion(java.time.LocalDate.now())
                    .salarioBase(java.math.BigDecimal.valueOf(1000.00))
                    .cargo("Registrar")
                    .telefono("1112223333")
                    .estado(com.minacontrol.empleado.enums.EstadoEmpleado.ACTIVO)
                    .rolSistema(com.minacontrol.empleado.enums.RolSistema.EMPLEADO)
                    .tieneUsuario(false) // This is important for registration tests
                    .build();
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
            assertThat(empleadoActualizado.get().isTieneUsuario()).isTrue();
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

        @Test
        @DisplayName("Debería retornar 400 Bad Request para un email inválido")
        void deberiaRetornarBadRequestParaEmailInvalido() throws Exception {
            RegistroUsuarioCreateDTO registroDTO = new RegistroUsuarioCreateDTO("email-invalido", "Password123!");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registroDTO)))
                    .andExpect(status().isBadRequest());
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

        @Test
        @DisplayName("Debería retornar 400 Bad Request para un email inválido en login")
        void deberiaRetornarBadRequestParaEmailInvalidoEnLogin() throws Exception {
            LoginRequestDTO loginDTO = new LoginRequestDTO("email-invalido", "Password123!");

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDTO)))
                    .andExpect(status().isBadRequest());
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
        @DisplayName("Debería retornar 200 OK incluso si el email no existe (por seguridad)")
        void deberiaRetornarOkSiEmailNoExistePorSeguridad() throws Exception {
            RecuperarContrasenaRequestDTO recoverDTO = new RecuperarContrasenaRequestDTO("noexiste@example.com");

            mockMvc.perform(post("/api/auth/recover-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(recoverDTO)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debería retornar 400 Bad Request para un email inválido en recuperación")
        void deberiaRetornarBadRequestParaEmailInvalidoEnRecuperacion() throws Exception {
            RecuperarContrasenaRequestDTO recoverDTO = new RecuperarContrasenaRequestDTO("email-invalido");

            mockMvc.perform(post("/api/auth/recover-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(recoverDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    // --- Métodos de Utilidad ---

    private void crearUsuarioParaTest(String email, String password) {
        Empleado empleado = empleadoRepository.findByEmail(email).orElseGet(() -> {
            Empleado nuevoEmpleado = Empleado.builder()
                    .email(email)
                    .nombres("Test")
                    .apellidos("User")
                    .numeroIdentificacion(java.util.UUID.randomUUID().toString().substring(0, 20))
                    .fechaContratacion(java.time.LocalDate.now())
                    .salarioBase(java.math.BigDecimal.valueOf(1000.00))
                    .cargo("Tester")
                    .telefono("1234567890")
                    .estado(com.minacontrol.empleado.enums.EstadoEmpleado.ACTIVO)
                    .rolSistema(com.minacontrol.empleado.enums.RolSistema.EMPLEADO)
                    .build();
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

    @Nested
    @DisplayName("Endpoint: POST /api/auth/logout")
    class LogoutTests {

        @Test
        @DisplayName("Debería cerrar la sesión exitosamente")
        void deberiaCerrarSesionExitosamente() throws Exception {
            // Arrange: No necesitamos un token real para esta prueba de integración básica
            // ya que la implementación actual del servicio solo imprime un mensaje.
            // En un escenario real, aquí se enviaría un token de refresco válido.
            LogoutRequestDTO logoutDTO = new LogoutRequestDTO("someDummyRefreshToken");

            mockMvc.perform(post("/api/auth/logout")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(logoutDTO)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debería retornar 400 Bad Request si el refresh token es nulo o vacío")
        void deberiaRetornarBadRequestSiRefreshTokenEsInvalido() throws Exception {
            // Arrange: refresh token nulo
            LogoutRequestDTO logoutDTO = new LogoutRequestDTO(null);

            mockMvc.perform(post("/api/auth/logout")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(logoutDTO)))
                    .andExpect(status().isBadRequest());

            // Arrange: refresh token vacío
            logoutDTO = new LogoutRequestDTO("");

            mockMvc.perform(post("/api/auth/logout")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(logoutDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Endpoint: POST /api/auth/refresh-token")
    class RefreshTokenTests {

        @BeforeEach
        void refreshTokenSetUp() {
            crearUsuarioParaTest("refresh.user@example.com", "Password123!");
        }

        @Test
        @DisplayName("Debería generar un nuevo access token y refresh token exitosamente")
        void should_GenerateNewTokens_Successfully() throws Exception {
            // Arrange: Hacer login primero para obtener un refresh token real
            LoginRequestDTO loginDTO = new LoginRequestDTO("refresh.user@example.com", "Password123!");
            
            String refreshToken = mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                    .andReturn()
                    .getResponse()
                    .getContentAsString()
                    .split("\"refreshToken\":\"")[1]
                    .split("\"")[0];

            var refreshTokenRequestDTO = new com.minacontrol.autenticacion.dto.request.RefreshTokenRequestDTO(refreshToken);

            // Act & Assert
            mockMvc.perform(post("/api/auth/refresh-token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(refreshTokenRequestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").isNotEmpty())
                    .andExpect(jsonPath("$.refreshToken").isNotEmpty());
        }

        @Test
        @DisplayName("Debería retornar 400 Bad Request si el refresh token es nulo o vacío")
        void deberiaRetornarBadRequestSiRefreshTokenEsInvalido() throws Exception {
            // Arrange: refresh token nulo
            var refreshTokenRequestDTO = new com.minacontrol.autenticacion.dto.request.RefreshTokenRequestDTO(null);

            mockMvc.perform(post("/api/auth/refresh-token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(refreshTokenRequestDTO)))
                    .andExpect(status().isBadRequest());

            // Arrange: refresh token vacío
            refreshTokenRequestDTO = new com.minacontrol.autenticacion.dto.request.RefreshTokenRequestDTO("");

            mockMvc.perform(post("/api/auth/refresh-token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(refreshTokenRequestDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Endpoint: POST /api/auth/change-password")
    class ChangePasswordTests {

        @BeforeEach
        void changePasswordSetUp() {
            // Crear un usuario para las pruebas de cambio de contraseña
            crearUsuarioParaTest("change.password@example.com", "OldPassword123!");
        }

        @Test
        @DisplayName("Debería cambiar la contraseña usando un token de reseteo exitosamente")
        void deberiaCambiarContrasenaConTokenExitosamente() throws Exception {
            // Arrange: Simular la solicitud de recuperación para obtener un token
            RecuperarContrasenaRequestDTO recoverDTO = new RecuperarContrasenaRequestDTO("change.password@example.com");
            mockMvc.perform(post("/api/auth/recover-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(recoverDTO)))
                    .andExpect(status().isOk());

            // Obtener el token de reseteo del usuario (simulado)
            Usuario usuario = usuarioRepository.findByEmail("change.password@example.com").get();
            String resetToken = usuario.getResetToken();

            CambiarContrasenaRequestDTO changePasswordDTO = new CambiarContrasenaRequestDTO(null, "NewPassword456!", resetToken);

            // Act & Assert
            mockMvc.perform(post("/api/auth/change-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(changePasswordDTO)))
                    .andExpect(status().isOk());

            // Verificar que la contraseña ha cambiado y el token se ha invalidado
            Usuario updatedUser = usuarioRepository.findByEmail("change.password@example.com").get();
            assertThat(passwordEncoder.matches("NewPassword456!", updatedUser.getPassword())).isTrue();
            assertThat(updatedUser.getResetToken()).isNull();
            assertThat(updatedUser.getResetTokenExpiry()).isNull();
        }

        @Test
        @DisplayName("Debería retornar 400 Bad Request si el token de reseteo es inválido")
        void deberiaRetornarBadRequestSiTokenInvalido() throws Exception {
            CambiarContrasenaRequestDTO changePasswordDTO = new CambiarContrasenaRequestDTO(null, "NewPassword456!", "invalidToken");

            mockMvc.perform(post("/api/auth/change-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(changePasswordDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("El token de reseteo es inválido o ha expirado."));
        }

        @Test
        @DisplayName("Debería retornar 400 Bad Request si el token de reseteo ha expirado")
        void deberiaRetornarBadRequestSiTokenExpirado() throws Exception {
            // Arrange: Crear un usuario y un token expirado
            Usuario usuario = usuarioRepository.findByEmail("change.password@example.com").get();
            usuario.setResetToken("expiredToken");
            usuario.setResetTokenExpiry(System.currentTimeMillis() - 1000); // Token expirado hace 1 segundo
            usuarioRepository.save(usuario);

            CambiarContrasenaRequestDTO changePasswordDTO = new CambiarContrasenaRequestDTO(null, "NewPassword456!", "expiredToken");

            // Act & Assert
            mockMvc.perform(post("/api/auth/change-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(changePasswordDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("El token de reseteo ha expirado."));
        }

        @Test
        @DisplayName("Debería cambiar la contraseña con la contraseña actual exitosamente")
        void deberiaCambiarContrasenaConContrasenaActualExitosamente() throws Exception {
            // Arrange: Crear un usuario y simular login para tener una contraseña actual
            crearUsuarioParaTest("user.change@example.com", "CurrentPassword123!");
            LoginRequestDTO loginDTO = new LoginRequestDTO("user.change@example.com", "CurrentPassword123!");
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDTO)))
                    .andExpect(status().isOk());

            CambiarContrasenaRequestDTO changePasswordDTO = new CambiarContrasenaRequestDTO("CurrentPassword123!", "NewPassword456!", null);

            // Act & Assert
            mockMvc.perform(post("/api/auth/change-password")
                            .with(user("user.change@example.com"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(changePasswordDTO)))
                    .andExpect(status().isOk());

            // Verificar que la contraseña ha cambiado
            Usuario updatedUser = usuarioRepository.findByEmail("user.change@example.com").get();
            assertThat(passwordEncoder.matches("NewPassword456!", updatedUser.getPassword())).isTrue();
        }

        @Test
        @DisplayName("Debería retornar 400 Bad Request si la contraseña actual es incorrecta")
        void deberiaRetornarBadRequestSiContrasenaActualIncorrecta() throws Exception {
            // Arrange: Crear un usuario y simular login
            crearUsuarioParaTest("user.wrongpass@example.com", "CorrectPassword123!");
            LoginRequestDTO loginDTO = new LoginRequestDTO("user.wrongpass@example.com", "CorrectPassword123!");
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginDTO)))
                    .andExpect(status().isOk());

            CambiarContrasenaRequestDTO changePasswordDTO = new CambiarContrasenaRequestDTO("WrongPassword!", "NewPassword456!", null);

            // Act & Assert
            mockMvc.perform(post("/api/auth/change-password")
                            .with(user("user.wrongpass@example.com"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(changePasswordDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("La contraseña actual es incorrecta."));
        }
    }
}