package com.minacontrol.nomina.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.nomina.dto.request.ConfiguracionTarifasCreateDTO;
import com.minacontrol.nomina.entity.ConfiguracionTarifas;
import com.minacontrol.nomina.repository.ConfiguracionTarifasRepository;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Pruebas de Integración para ConfiguracionTarifasController")
class ConfiguracionTarifasControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConfiguracionTarifasRepository configuracionTarifasRepository;

    @BeforeEach
    void setUp() {
        configuracionTarifasRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe crear una nueva configuración de tarifas cuando los datos son válidos y el usuario es ADMIN")
    void should_CrearConfiguracionTarifas_When_DatosValidosYUsuarioAdmin() throws Exception {
        // Arrange
        ConfiguracionTarifasCreateDTO dto = new ConfiguracionTarifasCreateDTO(
                new BigDecimal("6823.00"),
                new BigDecimal("8400.00"),
                "COP",
                LocalDate.now()
        );

        // Act & Assert
        mockMvc.perform(post("/api/configuracion/tarifas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .requestAttr("userId", 1L))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.tarifaPorHora").value(6823.00))
                .andExpect(jsonPath("$.bonoPorTonelada").value(8400.00))
                .andExpect(jsonPath("$.moneda").value("COP"))
                .andExpect(jsonPath("$.fechaVigencia").value(LocalDate.now().toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe devolver 403 Forbidden cuando un usuario USER intenta crear configuración de tarifas")
    void should_ReturnForbidden_When_UsuarioNoEsAdmin() throws Exception {
        // Arrange
        ConfiguracionTarifasCreateDTO dto = new ConfiguracionTarifasCreateDTO(
                new BigDecimal("6823.00"),
                new BigDecimal("8400.00"),
                "COP",
                LocalDate.now()
        );

        // Act & Assert
        mockMvc.perform(post("/api/configuracion/tarifas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .requestAttr("userId", 1L))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe obtener la configuración de tarifas vigente cuando existe")
    void should_ObtenerConfiguracionVigente_When_ExisteConfiguracion() throws Exception {
        // Arrange
        ConfiguracionTarifas configuracion = ConfiguracionTarifas.builder()
                .tarifaPorHora(new BigDecimal("6823.00"))
                .bonoPorTonelada(new BigDecimal("8400.00"))
                .moneda("COP")
                .fechaVigencia(LocalDate.now())
                .creadoPor(1L)
                .fechaCreacion(LocalDate.now())
                .build();
        
        configuracionTarifasRepository.save(configuracion);

        // Act & Assert
        mockMvc.perform(get("/api/configuracion/tarifas/vigentes")
                .param("moneda", "COP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.tarifaPorHora").value(6823.00))
                .andExpect(jsonPath("$.bonoPorTonelada").value(8400.00))
                .andExpect(jsonPath("$.moneda").value("COP"))
                .andExpect(jsonPath("$.fechaVigencia").value(LocalDate.now().toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe devolver 404 Not Found cuando no existe configuración vigente")
    void should_ReturnNotFound_When_NoExisteConfiguracionVigente() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/configuracion/tarifas/vigentes")
                .param("moneda", "COP"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message", containsString("No se encontró configuración vigente")));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe permitir a usuarios USER obtener configuración vigente")
    void should_PermitirUsuarioUser_When_ObtenerConfiguracionVigente() throws Exception {
        // Arrange
        ConfiguracionTarifas configuracion = ConfiguracionTarifas.builder()
                .tarifaPorHora(new BigDecimal("6823.00"))
                .bonoPorTonelada(new BigDecimal("8400.00"))
                .moneda("COP")
                .fechaVigencia(LocalDate.now())
                .creadoPor(1L)
                .fechaCreacion(LocalDate.now())
                .build();
        
        configuracionTarifasRepository.save(configuracion);

        // Act & Assert
        mockMvc.perform(get("/api/configuracion/tarifas/vigentes")
                .param("moneda", "COP"))
                .andExpect(status().isOk());
    }
}