package com.minacontrol.nomina.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.nomina.dto.request.ConfiguracionTarifasCreateDTO;
import com.minacontrol.nomina.dto.response.ConfiguracionTarifasDTO;
import com.minacontrol.nomina.entity.ConfiguracionTarifas;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Pruebas de Integración para ConfiguracionTarifasController - Actualización")
class ConfiguracionTarifasControllerUpdateIT {

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
    @DisplayName("Debe actualizar una configuración de tarifas existente")
    void should_ActualizarConfiguracion_When_AdminUser() throws Exception {
        // Arrange
        // Primero, crear una configuración de tarifas
        ConfiguracionTarifasCreateDTO configuracionCreateDTO = new ConfiguracionTarifasCreateDTO(
                new BigDecimal("5931.25"),
                new BigDecimal("11500.00"),
                "COP",
                LocalDate.now()
        );

        String responseContent = mockMvc.perform(post("/api/configuracion/tarifas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configuracionCreateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ConfiguracionTarifasDTO createdConfiguracion = objectMapper.readValue(responseContent, ConfiguracionTarifasDTO.class);
        Long id = createdConfiguracion.id();

        // Datos actualizados
        ConfiguracionTarifasCreateDTO configuracionUpdateDTO = new ConfiguracionTarifasCreateDTO(
                new BigDecimal("6000.00"),
                new BigDecimal("12000.00"),
                "COP",
                LocalDate.now()
        );

        // Act & Assert
        mockMvc.perform(put("/api/configuracion/tarifas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configuracionUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tarifaPorHora").value(6000.00))
                .andExpect(jsonPath("$.bonoPorTonelada").value(12000.00));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe devolver 404 cuando se intenta actualizar una configuración que no existe")
    void should_Return404_When_ConfiguracionNotExists() throws Exception {
        // Arrange
        ConfiguracionTarifasCreateDTO configuracionUpdateDTO = new ConfiguracionTarifasCreateDTO(
                new BigDecimal("6000.00"),
                new BigDecimal("12000.00"),
                "COP",
                LocalDate.now()
        );

        // Act & Assert
        mockMvc.perform(put("/api/configuracion/tarifas/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configuracionUpdateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe devolver 403 cuando un usuario no admin intenta actualizar una configuración")
    void should_Return403_When_NonAdminUserTriesToUpdate() throws Exception {
        // Arrange
        ConfiguracionTarifasCreateDTO configuracionUpdateDTO = new ConfiguracionTarifasCreateDTO(
                new BigDecimal("6000.00"),
                new BigDecimal("12000.00"),
                "COP",
                LocalDate.now()
        );

        // Act & Assert
        mockMvc.perform(put("/api/configuracion/tarifas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configuracionUpdateDTO)))
                .andExpect(status().isForbidden());
    }
}