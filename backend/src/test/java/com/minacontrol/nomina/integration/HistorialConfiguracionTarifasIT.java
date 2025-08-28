package com.minacontrol.nomina.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.nomina.dto.request.ConfiguracionTarifasCreateDTO;
import com.minacontrol.nomina.entity.ConfiguracionTarifas;
import com.minacontrol.nomina.entity.HistorialConfiguracionTarifas;
import com.minacontrol.nomina.repository.ConfiguracionTarifasRepository;
import com.minacontrol.nomina.repository.HistorialConfiguracionTarifasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Pruebas de Integración para Historial de Configuración de Tarifas")
class HistorialConfiguracionTarifasIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConfiguracionTarifasRepository configuracionTarifasRepository;
    
    @Autowired
    private HistorialConfiguracionTarifasRepository historialRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada prueba
        historialRepository.deleteAll();
        configuracionTarifasRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe obtener el historial de configuración de tarifas")
    void should_ObtenerHistorial_When_AdminUser() throws Exception {
        // Arrange
        ConfiguracionTarifasCreateDTO configuracionCreateDTO = new ConfiguracionTarifasCreateDTO(
                new BigDecimal("6823.00"),
                new BigDecimal("8000.00"),
                "COP",
                LocalDate.now()
        );

        // Primero, crear una configuración de tarifas para generar un registro en el historial
        mockMvc.perform(post("/api/configuracion/tarifas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configuracionCreateDTO)))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(get("/api/configuracion/tarifas/historial")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tarifaPorHora").value(6823.00))
                .andExpect(jsonPath("$[0].accion").value("CREADO"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe devolver 403 cuando un usuario no admin intenta obtener el historial")
    void should_Return403_When_NonAdminUserTriesToGetHistorial() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/configuracion/tarifas/historial")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}