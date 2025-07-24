
package com.minacontrol.nomina.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.enums.RolSistema;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.nomina.dto.request.AjusteNominaDTO;
import com.minacontrol.nomina.dto.request.CalcularNominaRequestDTO;
import com.minacontrol.nomina.entity.CalculoNomina;
import com.minacontrol.nomina.entity.PeriodoNomina;
import com.minacontrol.nomina.enums.EstadoPeriodo;
import com.minacontrol.nomina.repository.CalculoNominaRepository;
import com.minacontrol.nomina.repository.ComprobantePagoRepository;
import com.minacontrol.nomina.repository.PeriodoNominaRepository;
import com.minacontrol.produccion.entity.RegistroProduccion;
import com.minacontrol.produccion.repository.RegistroProduccionRepository;
import com.minacontrol.turnos.entity.RegistroAsistencia;
import com.minacontrol.turnos.entity.TipoTurno;
import com.minacontrol.turnos.repository.RegistroAsistenciaRepository;
import com.minacontrol.turnos.repository.TipoTurnoRepository;
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
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Pruebas de Integración para NominaController")
class NominaControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    // Repositorios del dominio principal
    @Autowired
    private PeriodoNominaRepository periodoNominaRepository;
    @Autowired
    private CalculoNominaRepository calculoNominaRepository;
    @Autowired
    private ComprobantePagoRepository comprobantePagoRepository;

    // Repositorios de dominios dependientes
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private RegistroAsistenciaRepository registroAsistenciaRepository;
    @Autowired
    private RegistroProduccionRepository registroProduccionRepository;
    @Autowired
    private TipoTurnoRepository tipoTurnoRepository;


    @BeforeEach
    void setUp() {
        // Limpieza en orden inverso de dependencias para evitar errores de FK
        comprobantePagoRepository.deleteAll();
        calculoNominaRepository.deleteAll();
        registroProduccionRepository.deleteAll();
        registroAsistenciaRepository.deleteAll();
        tipoTurnoRepository.deleteAll();
        empleadoRepository.deleteAll();
        periodoNominaRepository.deleteAll();
    }

    @Nested
    @DisplayName("CU-NOM-001: POST /api/nomina/calcular")
    @WithMockUser(roles = "GERENTE")
    class CalcularNominaIT {

        @Test
        @DisplayName("Happy Path: should return 200 OK and calculate payroll correctly")
        void should_Return200_When_HappyPath() throws Exception {
            // Arrange
            Empleado empleado1 = createAndSaveEmpleado("1", "test1@test.com", RolSistema.EMPLEADO);
            Empleado empleado2 = createAndSaveEmpleado("2", "test2@test.com", RolSistema.EMPLEADO);
            PeriodoNomina periodo = createAndSavePeriodo(LocalDate.now().minusDays(7), LocalDate.now(), EstadoPeriodo.ABIERTO);
            // Simulación de servicios externos
            createAndSaveAsistencia(empleado1.getId(), periodo.getFechaInicio(), 40.0);
            createAndSaveAsistencia(empleado2.getId(), periodo.getFechaInicio(), 38.0);
            TipoTurno turno = createAndSaveTurno();
            createAndSaveProduccion(empleado1.getId(), turno.getId(), periodo.getFechaInicio(), new BigDecimal("10.5"));


            CalcularNominaRequestDTO requestDTO = new CalcularNominaRequestDTO(periodo.getId());

            // Act
            ResultActions result = mockMvc.perform(post("/api/nomina/calcular")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO)));

            // Assert
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.numeroDeEmpleadosCalculados").value(2));

            PeriodoNomina periodoActualizado = periodoNominaRepository.findById(periodo.getId()).get();
            assertThat(periodoActualizado.getEstado()).isEqualTo(EstadoPeriodo.CALCULADO);

            List<CalculoNomina> calculos = calculoNominaRepository.findByPeriodoId(periodo.getId());
            assertThat(calculos).hasSize(2);
        }

        @Test
        @DisplayName("Error 404: should return 404 Not Found when PeriodoNomina does not exist")
        void should_Return404_When_PeriodoNotFound() throws Exception {
            CalcularNominaRequestDTO requestDTO = new CalcularNominaRequestDTO(999L);

            mockMvc.perform(post("/api/nomina/calcular")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(409))
                    .andExpect(jsonPath("$.error").value("Conflict"));
        }

        @Test
        @DisplayName("Error 409: should return 409 Conflict when PeriodoNomina is not OPEN")
        void should_Return409_When_PeriodoNotOpen() throws Exception {
            PeriodoNomina periodo = createAndSavePeriodo(LocalDate.now().minusDays(7), LocalDate.now(), EstadoPeriodo.CALCULADO);
            CalcularNominaRequestDTO requestDTO = new CalcularNominaRequestDTO(periodo.getId());

            mockMvc.perform(post("/api/nomina/calcular")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(409))
                    .andExpect(jsonPath("$.error").value("Conflict"));
        }

        @Test
        @DisplayName("Error 400: should return 400 Bad Request when request body is invalid")
        void should_Return400_When_BodyIsInvalid() throws Exception {
            CalcularNominaRequestDTO requestDTO = new CalcularNominaRequestDTO(null);

            mockMvc.perform(post("/api/nomina/calcular")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("CU-NOM-002: PATCH /api/nomina/calculos/{id}/ajustar")
    @WithMockUser(roles = "GERENTE")
    class AjustarCalculoIT {

        @Test
        @DisplayName("Happy Path: should return 200 OK and update calculation")
        void should_Return200_When_HappyPath() throws Exception {
            // Arrange
            PeriodoNomina periodo = createAndSavePeriodo(LocalDate.now().minusDays(7), LocalDate.now(), EstadoPeriodo.CALCULADO);
            CalculoNomina calculo = createAndSaveCalculo(periodo, 1L, new BigDecimal("1000.00"));
            AjusteNominaDTO ajusteDTO = new AjusteNominaDTO("Bono Extra", new BigDecimal("150.50"), false, "Excelente desempeño");

            // Act
            ResultActions result = mockMvc.perform(patch("/api/nomina/calculos/{id}/ajustar", calculo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(ajusteDTO)));

            // Assert
            result.andExpect(status().isOk())
                  .andExpect(jsonPath("$.totalNeto").value(1150.50));

            CalculoNomina calculoActualizado = calculoNominaRepository.findById(calculo.getId()).get();
            assertThat(calculoActualizado.getTotalNeto()).isEqualByComparingTo("1150.50");
            assertThat(calculoActualizado.getObservaciones()).contains("Excelente desempeño");
        }

        @Test
        @DisplayName("Error 404: should return 404 Not Found when calculation does not exist")
        void should_Return404_When_CalculoNotFound() throws Exception {
            AjusteNominaDTO ajusteDTO = new AjusteNominaDTO("Bono", new BigDecimal("100"), false, "test");
            mockMvc.perform(patch("/api/nomina/calculos/{id}/ajustar", 999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(ajusteDTO)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Error 409: should return 409 Conflict when period is not CALCULADO")
        void should_Return409_When_PeriodoNotCalculado() throws Exception {
            PeriodoNomina periodo = createAndSavePeriodo(LocalDate.now().minusDays(7), LocalDate.now(), EstadoPeriodo.PAGADO);
            CalculoNomina calculo = createAndSaveCalculo(periodo, 1L, new BigDecimal("1000"));
            AjusteNominaDTO ajusteDTO = new AjusteNominaDTO("Bono", new BigDecimal("100"), false, "test");

            mockMvc.perform(patch("/api/nomina/calculos/{id}/ajustar", calculo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(ajusteDTO)))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("CU-NOM-003: POST /api/nomina/periodos/{id}/generar-comprobantes")
    @WithMockUser(roles = "GERENTE")
    class GenerarComprobantesIT {

        @Test
        @DisplayName("Happy Path: should return 200 OK and generate payment slips")
        void should_Return200_When_HappyPath() throws Exception {
            // Arrange
            PeriodoNomina periodo = createAndSavePeriodo(LocalDate.now().minusDays(7), LocalDate.now(), EstadoPeriodo.CALCULADO);
            createAndSaveCalculo(periodo, 1L, new BigDecimal("1200"));
            createAndSaveCalculo(periodo, 2L, new BigDecimal("1300"));

            // Act
            ResultActions result = mockMvc.perform(post("/api/nomina/periodos/{id}/generar-comprobantes", periodo.getId()));

            // Assert
            result.andExpect(status().isOk())
                  .andExpect(jsonPath("$", hasSize(2)));

            PeriodoNomina periodoActualizado = periodoNominaRepository.findById(periodo.getId()).get();
            assertThat(periodoActualizado.getEstado()).isEqualTo(EstadoPeriodo.PAGADO);
            assertThat(comprobantePagoRepository.findAll()).hasSize(2);
        }

        @Test
        @DisplayName("Error 409: should return 409 Conflict when period does not exist")
        void should_Return409_When_PeriodoNotFound() throws Exception {
            mockMvc.perform(post("/api/nomina/periodos/{id}/generar-comprobantes", 999L))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Error 409: should return 409 Conflict when period is not CALCULADO")
        void should_Return409_When_PeriodoNotCalculado() throws Exception {
            PeriodoNomina periodo = createAndSavePeriodo(LocalDate.now().minusDays(7), LocalDate.now(), EstadoPeriodo.ABIERTO);
            mockMvc.perform(post("/api/nomina/periodos/{id}/generar-comprobantes", periodo.getId()))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("CU-NOM-004: GET /api/nomina/historial")
    @WithMockUser(roles = "GERENTE")
    class ConsultarHistorialIT {

        @Test
        @DisplayName("Happy Path: should return 200 OK and payment history")
        void should_Return200_When_HappyPath() throws Exception {
            // Arrange
            Empleado empleado = createAndSaveEmpleado("1", "test1@test.com", RolSistema.EMPLEADO);
            PeriodoNomina p1 = createAndSavePeriodo(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 7), EstadoPeriodo.PAGADO);
            PeriodoNomina p2 = createAndSavePeriodo(LocalDate.of(2025, 1, 8), LocalDate.of(2025, 1, 15), EstadoPeriodo.PAGADO);
            createAndSaveCalculo(p1, empleado.getId(), new BigDecimal("100"));
            createAndSaveCalculo(p2, empleado.getId(), new BigDecimal("200"));

            // Act & Assert
            mockMvc.perform(get("/api/nomina/historial")
                            .param("empleadoId", empleado.getId().toString())
                            .param("fechaInicio", "2025-01-01")
                            .param("fechaFin", "2025-01-31"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].totalNeto", is(100.0)))
                    .andExpect(jsonPath("$[1].totalNeto", is(200.0)));
        }

        @Test
        @DisplayName("Error 400: should return 400 Bad Request when date range is invalid")
        void should_Return400_When_DateRangeIsInvalid() throws Exception {
            mockMvc.perform(get("/api/nomina/historial")
                            .param("empleadoId", "1")
                            .param("fechaInicio", "2025-02-01")
                            .param("fechaFin", "2025-01-01"))
                    .andExpect(status().isBadRequest());
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

    private void createAndSaveAsistencia(Long empleadoId, LocalDate fecha, double horas) {
        RegistroAsistencia asistencia = RegistroAsistencia.builder()
                .empleadoId(empleadoId).fecha(fecha).horasTrabajadas(horas).build();
        registroAsistenciaRepository.save(asistencia);
    }

    private TipoTurno createAndSaveTurno() {
        return tipoTurnoRepository.findByNombre("Diurno").orElseGet(() -> {
            TipoTurno turno = new TipoTurno();
            turno.setNombre("Diurno");
            turno.setHoraInicio(java.time.LocalTime.of(8, 0));
            turno.setHoraFin(java.time.LocalTime.of(16, 0));
            turno.setActivo(true);
            return tipoTurnoRepository.save(turno);
        });
    }

    private void createAndSaveProduccion(Long empleadoId, Long turnoId, LocalDate fecha, BigDecimal toneladas) {
        RegistroProduccion produccion = RegistroProduccion.builder()
                .empleadoId(empleadoId)
                .tipoTurnoId(turnoId)
                .fechaRegistro(fecha)
                .cantidadExtraidaToneladas(toneladas)
                .ubicacionExtraccion("Mina A")
                .build();
        registroProduccionRepository.save(produccion);
    }

    private CalculoNomina createAndSaveCalculo(PeriodoNomina periodo, Long empleadoId, BigDecimal neto) {
        CalculoNomina calculo = CalculoNomina.builder()
                .periodo(periodo)
                .empleadoId(empleadoId)
                .salarioBase(neto)
                .bonificaciones(BigDecimal.ZERO)
                .deducciones(BigDecimal.ZERO)
                .totalBruto(neto)
                .totalNeto(neto)
                .build();
        return calculoNominaRepository.save(calculo);
    }
}
