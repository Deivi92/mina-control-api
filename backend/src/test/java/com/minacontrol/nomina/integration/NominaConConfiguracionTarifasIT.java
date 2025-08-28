package com.minacontrol.nomina.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.enums.RolSistema;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.nomina.dto.request.CalcularNominaRequestDTO;
import com.minacontrol.nomina.entity.CalculoNomina;
import com.minacontrol.nomina.entity.ConfiguracionTarifas;
import com.minacontrol.nomina.entity.PeriodoNomina;
import com.minacontrol.nomina.enums.EstadoPeriodo;
import com.minacontrol.nomina.repository.CalculoNominaRepository;
import com.minacontrol.nomina.repository.ConfiguracionTarifasRepository;
import com.minacontrol.nomina.repository.PeriodoNominaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Pruebas de Integración: Nómina con Configuración de Tarifas")
class NominaConConfiguracionTarifasIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Repositorios del dominio principal
    @Autowired
    private PeriodoNominaRepository periodoNominaRepository;
    @Autowired
    private ConfiguracionTarifasRepository configuracionTarifasRepository;
    @Autowired
    private CalculoNominaRepository calculoNominaRepository;

    // Repositorios de dominios dependientes
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @BeforeEach
    void setUp() {
        // Limpieza en orden correcto para evitar errores de FK
        calculoNominaRepository.deleteAll();
        empleadoRepository.deleteAll();
        configuracionTarifasRepository.deleteAll();
        periodoNominaRepository.deleteAll();
    }

    @Nested
    @DisplayName("CU-NOM-001: POST /api/nomina/calcular con Configuración de Tarifas")
    @WithMockUser(roles = "GERENTE")
    class CalcularNominaConConfiguracionIT {

        @Test
        @DisplayName("Happy Path: should calculate payroll using dynamic tariff configuration")
        void should_CalculatePayroll_Using_DynamicTariffConfiguration() throws Exception {
            // Arrange
            ConfiguracionTarifas configuracion = createAndSaveConfiguracion(
                    new BigDecimal("6823.00"), // Tarifa por hora
                    new BigDecimal("8400.00"), // Bono por tonelada
                    "COP"
            );
            
            Empleado empleado = createAndSaveEmpleado("1", "test1@test.com", RolSistema.EMPLEADO);
            PeriodoNomina periodo = createAndSavePeriodo(
                    LocalDate.now().minusDays(7), 
                    LocalDate.now(), 
                    EstadoPeriodo.ABIERTO
            );

            CalcularNominaRequestDTO requestDTO = new CalcularNominaRequestDTO(periodo.getId());

            // Act
            mockMvc.perform(post("/api/nomina/calcular")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numeroDeEmpleadosCalculados").value(1));

            // Assert - Verificar que se haya creado un cálculo de nómina
            List<CalculoNomina> calculos = calculoNominaRepository.findByPeriodoId(periodo.getId());
            assertThat(calculos).hasSize(1);
            
            // Verificar que el período se haya actualizado a CALCULADO
            PeriodoNomina periodoActualizado = periodoNominaRepository.findById(periodo.getId()).get();
            assertThat(periodoActualizado.getEstado()).isEqualTo(EstadoPeriodo.CALCULADO);
        }

        @Test
        @DisplayName("Fallback: should use default values when no tariff configuration exists")
        void should_UseDefaultValues_When_NoTariffConfigurationExists() throws Exception {
            // Arrange - No creamos configuración de tarifas
            
            Empleado empleado = createAndSaveEmpleado("2", "test2@test.com", RolSistema.EMPLEADO);
            PeriodoNomina periodo = createAndSavePeriodo(
                    LocalDate.now().minusDays(7), 
                    LocalDate.now(), 
                    EstadoPeriodo.ABIERTO
            );

            CalcularNominaRequestDTO requestDTO = new CalcularNominaRequestDTO(periodo.getId());

            // Act
            mockMvc.perform(post("/api/nomina/calcular")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numeroDeEmpleadosCalculados").value(1));

            // Assert - Verificar que se haya creado un cálculo de nómina
            List<CalculoNomina> calculos = calculoNominaRepository.findByPeriodoId(periodo.getId());
            assertThat(calculos).hasSize(1);
            
            // Verificar que el período se haya actualizado a CALCULADO
            PeriodoNomina periodoActualizado = periodoNominaRepository.findById(periodo.getId()).get();
            assertThat(periodoActualizado.getEstado()).isEqualTo(EstadoPeriodo.CALCULADO);
        }
    }

    // Helper methods to create entities
    private Empleado createAndSaveEmpleado(String unique, String email, RolSistema rol) {
        Empleado empleado = Empleado.builder()
                .nombres("Test").apellidos("User")
                .numeroIdentificacion("12345" + unique)
                .email(email)
                .cargo("Minero")
                .fechaContratacion(LocalDate.now())
                .salarioBase(new BigDecimal("20000"))
                .estado(EstadoEmpleado.ACTIVO)
                .rolSistema(rol)
                .build();
        return empleadoRepository.save(empleado);
    }

    private PeriodoNomina createAndSavePeriodo(LocalDate inicio, LocalDate fin, EstadoPeriodo estado) {
        PeriodoNomina periodo = PeriodoNomina.builder()
                .fechaInicio(inicio).fechaFin(fin).estado(estado).build();
        return periodoNominaRepository.save(periodo);
    }

    private ConfiguracionTarifas createAndSaveConfiguracion(BigDecimal tarifaHora, BigDecimal bonoTonelada, String moneda) {
        ConfiguracionTarifas configuracion = ConfiguracionTarifas.builder()
                .tarifaPorHora(tarifaHora)
                .bonoPorTonelada(bonoTonelada)
                .moneda(moneda)
                .fechaVigencia(LocalDate.now())
                .creadoPor(1L)
                .fechaCreacion(LocalDate.now())
                .build();
        return configuracionTarifasRepository.save(configuracion);
    }
}