package com.minacontrol.empleado.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.empleado.dto.EmpleadoRequestDTO;
import com.minacontrol.empleado.dto.EmpleadoResponseDTO;
import com.minacontrol.empleado.model.EstadoEmpleado;
import com.minacontrol.empleado.service.EmpleadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(EmpleadoController.class)
public class EmpleadoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    @Qualifier("empleadoService") // Asegúrate que el qualifier coincida si lo usas en el servicio real
    private EmpleadoService empleadoService;

    private EmpleadoRequestDTO empleadoRequestValido;
    private EmpleadoResponseDTO empleadoResponseEsperado;

    @BeforeEach
    void setUp() {
        empleadoRequestValido = new EmpleadoRequestDTO(
                "1020304050", "Carlos", "Santana",
                LocalDate.of(1985, Month.JULY, 20), "Ingeniero de Minas",
                LocalDate.of(2022, Month.AUGUST, 15), new BigDecimal("7500000.00"),
                EstadoEmpleado.ACTIVO, "carlos.santana@example.com", "3101234567"
        );

        // Aseguramos que el response DTO use los nombres de campo correctos para la comparación
        empleadoResponseEsperado = new EmpleadoResponseDTO(
                1L, "1020304050", "Carlos", "Santana", // nombres, apellidos
                LocalDate.of(1985, Month.JULY, 20), "Ingeniero de Minas",
                LocalDate.of(2022, Month.AUGUST, 15), new BigDecimal("7500000.00"), // fechaIngreso, salarioBase
                EstadoEmpleado.ACTIVO, "carlos.santana@example.com", "3101234567"
        );
    }

    @Test
    @DisplayName("POST /api/empleados - Éxito: Crear empleado con datos válidos")
    void crearEmpleado_cuandoRequestEsValido_deberiaRetornar201YCreado() throws Exception {
        when(empleadoService.crearEmpleado(any(EmpleadoRequestDTO.class))).thenReturn(empleadoResponseEsperado);

        ResultActions resultActions = mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)));

        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombres", is(empleadoRequestValido.getNombre())))
                .andExpect(jsonPath("$.apellidos", is(empleadoRequestValido.getApellido())))
                .andExpect(jsonPath("$.numeroDocumento", is(empleadoRequestValido.getNumeroDocumento())))
                .andExpect(jsonPath("$.email", is(empleadoRequestValido.getEmail())))
                .andExpect(jsonPath("$.fechaIngreso", is(empleadoRequestValido.getFechaContratacion().toString()))) // Ajustado a los nombres del ResponseDTO
                .andExpect(jsonPath("$.salarioBase", is(empleadoRequestValido.getSalario().doubleValue()))); // Ajustado a los nombres del ResponseDTO
    }

    @Test
    @DisplayName("POST /api/empleados - Error: Numero de Documento vacío")
    void crearEmpleado_cuandoNumeroDocumentoEsVacio_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setNumeroDocumento("");

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.numeroDocumento", is("El número de documento no puede estar vacío")));
    }

    @Test
    @DisplayName("POST /api/empleados - Error: Nombre vacío")
    void crearEmpleado_cuandoNombreEsVacio_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setNombre("");

        ResultActions resultActions = mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // La validación de @NotBlank se aplica al campo 'nombre' del DTO
                .andExpect(jsonPath("$.errors.nombre", is("El nombre no puede estar vacío")));
    }

    @Test
    @DisplayName("POST /api/empleados - Error: Apellido vacío")
    void crearEmpleado_cuandoApellidoEsVacio_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setApellido("");

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.apellido", is("El apellido no puede estar vacío")));
    }

    @Test
    @DisplayName("POST /api/empleados - Error: Fecha de Nacimiento nula")
    void crearEmpleado_cuandoFechaNacimientoEsNula_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setFechaNacimiento(null);

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.fechaNacimiento", is("La fecha de nacimiento no puede ser nula")));
    }
    
    @Test
    @DisplayName("POST /api/empleados - Error: Cargo vacío")
    void crearEmpleado_cuandoCargoEsVacio_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setCargo("");

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.cargo", is("El cargo no puede estar vacío")));
    }

    @Test
    @DisplayName("POST /api/empleados - Error: Fecha de Contratación nula")
    void crearEmpleado_cuandoFechaContratacionEsNula_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setFechaContratacion(null);

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.fechaContratacion", is("La fecha de contratación no puede ser nula")));
    }

    @Test
    @DisplayName("POST /api/empleados - Error: Fecha de Contratación futura")
    void crearEmpleado_cuandoFechaContratacionEsFutura_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setFechaContratacion(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.fechaContratacion", is("La fecha de contratación debe ser una fecha pasada o presente")));
    }

    @Test
    @DisplayName("POST /api/empleados - Error: Salario nulo")
    void crearEmpleado_cuandoSalarioEsNulo_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setSalario(null);

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.salario", is("El salario no puede ser nulo")));
    }

    @Test
    @DisplayName("POST /api/empleados - Error: Salario es cero")
    void crearEmpleado_cuandoSalarioEsCero_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setSalario(BigDecimal.ZERO);

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.salario", is("El salario debe ser mayor que cero")));
    }
    
    @Test
    @DisplayName("POST /api/empleados - Error: Salario negativo")
    void crearEmpleado_cuandoSalarioEsNegativo_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setSalario(new BigDecimal("-100"));

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.salario", is("El salario debe ser mayor que cero")));
    }

    @Test
    @DisplayName("POST /api/empleados - Error: Estado nulo")
    void crearEmpleado_cuandoEstadoEsNulo_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setEstado(null);

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.estado", is("El estado no puede ser nulo")));
    }

    @Test
    @DisplayName("GET /api/empleados/{id} - Éxito: Obtener empleado por ID existente")
    void obtenerEmpleadoPorId_cuandoIdExiste_deberiaRetornar200YEmpleado() throws Exception {
        Long empleadoId = 1L;
        when(empleadoService.obtenerEmpleadoPorId(empleadoId)).thenReturn(Optional.of(empleadoResponseEsperado));

        mockMvc.perform(get("/api/empleados/{id}", empleadoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(empleadoId.intValue())))
                .andExpect(jsonPath("$.nombres", is(empleadoResponseEsperado.getNombres())));
    }

    @Test
    @DisplayName("GET /api/empleados/{id} - Error: Obtener empleado por ID no existente")
    void obtenerEmpleadoPorId_cuandoIdNoExiste_deberiaRetornar404() throws Exception {
        Long empleadoId = 99L;
        when(empleadoService.obtenerEmpleadoPorId(empleadoId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/empleados/{id}", empleadoId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/empleados - Éxito: Obtener todos los empleados")
    void obtenerTodosLosEmpleados_deberiaRetornar200YListaDeEmpleados() throws Exception {
        EmpleadoResponseDTO empleado2 = new EmpleadoResponseDTO(2L, "1122334455", "Ana", "Perez", LocalDate.now(), "Cargo2", LocalDate.now(), BigDecimal.TEN, EstadoEmpleado.INACTIVO, "ana@example.com", "3000000000");
        when(empleadoService.obtenerTodosLosEmpleados()).thenReturn(Arrays.asList(empleadoResponseEsperado, empleado2));

        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    @DisplayName("PUT /api/empleados/{id} - Éxito: Actualizar empleado existente")
    void actualizarEmpleado_cuandoIdExisteYRequestEsValido_deberiaRetornar200YActualizado() throws Exception {
        Long empleadoId = 1L;
        EmpleadoRequestDTO requestActualizacion = new EmpleadoRequestDTO(
            "1020304050", "Carlos Alberto", "Santana", // Nombre cambiado
            LocalDate.of(1985, Month.JULY, 20), "Ingeniero Senior de Minas", // Cargo cambiado
            LocalDate.of(2022, Month.AUGUST, 15), new BigDecimal("8500000.00"), // Salario cambiado
            EstadoEmpleado.ACTIVO, "carlos.santana.new@example.com", "3101234568"
        );
        // Aseguramos que el response DTO use los nombres de campo correctos para la comparación
        EmpleadoResponseDTO responseActualizado = new EmpleadoResponseDTO(
            empleadoId, requestActualizacion.getNumeroDocumento(), 
            requestActualizacion.getNombre(), // nombres
            requestActualizacion.getApellido(), // apellidos
            requestActualizacion.getFechaNacimiento(), 
            requestActualizacion.getCargo(), 
            requestActualizacion.getFechaContratacion(), // fechaIngreso
            requestActualizacion.getSalario(), // salarioBase
            requestActualizacion.getEstado(), 
            requestActualizacion.getEmail(), 
            requestActualizacion.getTelefono()
        );

        when(empleadoService.actualizarEmpleado(eq(empleadoId), any(EmpleadoRequestDTO.class))).thenReturn(Optional.of(responseActualizado));

        mockMvc.perform(put("/api/empleados/{id}", empleadoId) // Corregido: Eliminada la barra invertida extra
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestActualizacion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(empleadoId.intValue()))) // Corregido
                .andExpect(jsonPath("$.nombres", is("Carlos Alberto"))) // Corregido
                .andExpect(jsonPath("$.cargo", is("Ingeniero Senior de Minas"))) // Corregido
                .andExpect(jsonPath("$.salarioBase", is(8500000.00))) // Corregido
                .andExpect(jsonPath("$.fechaIngreso", is(requestActualizacion.getFechaContratacion().toString()))); // Corregido
    }

    @Test
    @DisplayName("PUT /api/empleados/{id} - Error: Actualizar empleado no existente")
    void actualizarEmpleado_cuandoIdNoExiste_deberiaRetornar404() throws Exception {
        Long empleadoId = 99L;
        when(empleadoService.actualizarEmpleado(eq(empleadoId), any(EmpleadoRequestDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/empleados/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/empleados/{id} - Éxito: Eliminar empleado existente")
    void eliminarEmpleado_cuandoIdExiste_deberiaRetornar204() throws Exception {
        Long empleadoId = 1L;
        when(empleadoService.eliminarEmpleado(empleadoId)).thenReturn(true);

        mockMvc.perform(delete("/api/empleados/{id}", empleadoId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/empleados/{id} - Error: Eliminar empleado no existente")
    void eliminarEmpleado_cuandoIdNoExiste_deberiaRetornar404() throws Exception {
        Long empleadoId = 99L;
        when(empleadoService.eliminarEmpleado(empleadoId)).thenReturn(false);

        mockMvc.perform(delete("/api/empleados/{id}", empleadoId))
                .andExpect(status().isNotFound());
    }

    // Aquí puedes añadir más pruebas para otros casos de validación
    // Por ejemplo, email inválido, fecha de nacimiento futura, etc.
    @Test
    @DisplayName("POST /api/empleados - Error: Email inválido")
    void crearEmpleado_cuandoEmailEsInvalido_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setEmail("email-invalido");

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email", is("El formato del email no es válido")));
    }

    @Test
    @DisplayName("POST /api/empleados - Error: Fecha de nacimiento futura")
    void crearEmpleado_cuandoFechaNacimientoEsFutura_deberiaRetornar400YMensajeDeError() throws Exception {
        empleadoRequestValido.setFechaNacimiento(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoRequestValido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.fechaNacimiento", is("La fecha de nacimiento debe ser una fecha pasada")));
    }
}
