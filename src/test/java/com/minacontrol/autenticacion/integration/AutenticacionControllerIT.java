package com.minacontrol.autenticacion.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.autenticacion.dto.RegistroUsuarioCreateDTO;
import com.minacontrol.autenticacion.model.Usuario;
import com.minacontrol.autenticacion.repository.UsuarioRepository;
import com.minacontrol.empleado.model.Empleado;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional // Para asegurar que cada test se ejecute en una transacción y se haga rollback
public class AutenticacionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Empleado empleadoExistente;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada test (Transactional debería manejarlo, pero es buena práctica)
        usuarioRepository.deleteAll();
        empleadoRepository.deleteAll();

        // Crear un empleado para las pruebas de registro
        empleadoExistente = new Empleado();
        empleadoExistente.setEmail("empleado@example.com");
        empleadoExistente.setTieneUsuario(false);
        empleadoRepository.save(empleadoExistente);
    }

    @Test
    @DisplayName("shouldRegisterUserSuccessfully - Debería registrar un usuario exitosamente a través del endpoint")
    void deberiaRegistrarUsuarioExitosamente() throws Exception {
        RegistroUsuarioCreateDTO registroDTO = new RegistroUsuarioCreateDTO("empleado@example.com", "Password123!");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("empleado@example.com"));

        // Verificar que el usuario fue guardado en la BD
        Optional<Usuario> usuarioGuardado = usuarioRepository.findByEmail("empleado@example.com");
        assertThat(usuarioGuardado).isPresent();
        assertThat(usuarioGuardado.get().getEmail()).isEqualTo("empleado@example.com");

        // Verificar que el empleado fue actualizado
        Optional<Empleado> empleadoActualizado = empleadoRepository.findByEmail("empleado@example.com");
        assertThat(empleadoActualizado).isPresent();
        assertThat(empleadoActualizado.get().getTieneUsuario()).isTrue();
    }

    @Test
    @DisplayName("shouldReturnConflictWhenEmailAlreadyInUse - Debería retornar 409 Conflict si el email ya está en uso")
    void deberiaRetornarConflictoSiEmailYaEnUso() throws Exception {
        // Primero, registrar un usuario
        RegistroUsuarioCreateDTO registroDTO = new RegistroUsuarioCreateDTO("empleado@example.com", "Password123!");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated());

        // Intentar registrar con el mismo email
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El email ya está en uso."));
    }

    @Test
    @DisplayName("shouldReturnNotFoundWhenEmployeeNotFound - Debería retornar 404 Not Found si el empleado no existe")
    void deberiaRetornarNotFoundSiEmpleadoNoExiste() throws Exception {
        RegistroUsuarioCreateDTO registroDTO = new RegistroUsuarioCreateDTO("noexiste@example.com", "Password123!");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("El empleado con el email proporcionado no fue encontrado."));
    }
}
