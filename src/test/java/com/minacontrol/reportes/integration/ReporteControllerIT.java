package com.minacontrol.reportes.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.enums.RolSistema;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.nomina.entity.CalculoNomina;
import com.minacontrol.nomina.entity.PeriodoNomina;
import com.minacontrol.nomina.enums.EstadoPeriodo;
import com.minacontrol.nomina.repository.CalculoNominaRepository;
import com.minacontrol.nomina.repository.PeriodoNominaRepository;
import com.minacontrol.produccion.entity.RegistroProduccion;
import com.minacontrol.produccion.repository.RegistroProduccionRepository;
import com.minacontrol.reportes.dto.request.DatosOperacionalesDTO;
import com.minacontrol.reportes.dto.request.ParametrosReporteDTO;
import com.minacontrol.reportes.enums.FormatoReporte;
import com.minacontrol.reportes.enums.TipoReporte;
import com.minacontrol.reportes.repository.ReporteGeneradoRepository;
import com.minacontrol.turnos.entity.RegistroAsistencia;
import com.minacontrol.turnos.entity.TipoTurno;
import com.minacontrol.turnos.repository.AsignacionTurnoRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Pruebas de Integración para ReporteController")
class ReporteControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    // Repositorios
    @Autowired private ReporteGeneradoRepository reporteGeneradoRepository;
    @Autowired private EmpleadoRepository empleadoRepository;
    @Autowired private TipoTurnoRepository tipoTurnoRepository;
    @Autowired private RegistroProduccionRepository registroProduccionRepository;
    @Autowired private RegistroAsistenciaRepository registroAsistenciaRepository;
    @Autowired private PeriodoNominaRepository periodoNominaRepository;
    @Autowired private CalculoNominaRepository calculoNominaRepository;
    @Autowired private AsignacionTurnoRepository asignacionTurnoRepository;

    private Empleado empleado;
    private TipoTurno turno;

    @BeforeEach
    void setUp() {
        // Limpieza de la BD
        reporteGeneradoRepository.deleteAll();
        calculoNominaRepository.deleteAll();
        periodoNominaRepository.deleteAll();
        registroProduccionRepository.deleteAll();
        registroAsistenciaRepository.deleteAll();
        asignacionTurnoRepository.deleteAll();
        empleadoRepository.deleteAll();
        tipoTurnoRepository.deleteAll();

        // --- Crear datos de prueba --- 
        empleado = Empleado.builder()
                .nombres("Test").apellidos("User").numeroIdentificacion("12345")
                .email("test@example.com").cargo("Tester").fechaContratacion(LocalDate.now())
                .salarioBase(BigDecimal.TEN).estado(EstadoEmpleado.ACTIVO).rolSistema(RolSistema.EMPLEADO)
                .build();
        empleadoRepository.save(empleado);

        turno = TipoTurno.builder()
                .nombre("Turno de Prueba").horaInicio(LocalTime.of(8, 0)).horaFin(LocalTime.of(16, 0)).activo(true)
                .build();
        tipoTurnoRepository.save(turno);

        // Datos para reporte de producción
        RegistroProduccion produccion = RegistroProduccion.builder()
                .empleadoId(empleado.getId()).tipoTurnoId(turno.getId()).fechaRegistro(LocalDate.now().minusDays(1))
                .cantidadExtraidaToneladas(BigDecimal.TEN).ubicacionExtraccion("Veta de Prueba").build();
        registroProduccionRepository.save(produccion);

        // Datos para reporte de asistencia
        RegistroAsistencia asistencia = RegistroAsistencia.builder()
                .empleadoId(empleado.getId()).fecha(LocalDate.now().minusDays(2)).horasTrabajadas(8.0).build();
        registroAsistenciaRepository.save(asistencia);

        // Datos para reporte de costos
        PeriodoNomina periodo = PeriodoNomina.builder()
                .fechaInicio(LocalDate.now().minusDays(7)).fechaFin(LocalDate.now().minusDays(1)).estado(EstadoPeriodo.PAGADO).build();
        periodoNominaRepository.save(periodo);
        CalculoNomina nomina = CalculoNomina.builder()
                .periodo(periodo).empleadoId(empleado.getId()).totalNeto(new BigDecimal("500"))
                .salarioBase(BigDecimal.ZERO).bonificaciones(BigDecimal.ZERO).deducciones(BigDecimal.ZERO).totalBruto(BigDecimal.ZERO)
                .build();
        calculoNominaRepository.save(nomina);
    }

    @Nested
    @DisplayName("Endpoint: POST /api/reportes/produccion")
    @WithMockUser(roles = "GERENTE")
    class GenerarReporteProduccionIT {

        @Test
        @DisplayName("Happy Path: Debe generar un reporte de producción y verificar la BD")
        void should_GenerateReport_And_VerifyDatabase() throws Exception {
            // Arrange
            ParametrosReporteDTO parametros = new ParametrosReporteDTO(TipoReporte.PRODUCCION, LocalDate.now().minusDays(7), LocalDate.now(), FormatoReporte.PDF, null, null, true, null);

            // Act & Assert
            mockMvc.perform(post("/api/reportes/produccion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(parametros)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.tipoReporte", is("PRODUCCION")))
                    .andExpect(jsonPath("$.urlDescarga").exists());

            // DB Verification
            List<com.minacontrol.reportes.entity.ReporteGenerado> reportes = reporteGeneradoRepository.findAll();
            assertThat(reportes).hasSize(1);
            assertThat(reportes.get(0).getTipoReporte()).isEqualTo(TipoReporte.PRODUCCION);
            assertThat(reportes.get(0).getFormato()).isEqualTo(FormatoReporte.PDF);
        }

        @Test
        @DisplayName("Error 400: Debe devolver Bad Request y ErrorResponseDTO para parámetros inválidos")
        void should_Return400_When_InvalidParams() throws Exception {
            // Arrange
            ParametrosReporteDTO parametros = new ParametrosReporteDTO(null, LocalDate.now(), LocalDate.now().minusDays(1), null, null, null, true, null);

            // Act & Assert
            mockMvc.perform(post("/api/reportes/produccion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(parametros)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.error", is("Bad Request")))
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.path", is("/api/reportes/produccion")));
        }

        @Test
        @WithMockUser(roles = "EMPLEADO")
        @DisplayName("Error 403: Debe devolver Forbidden si el rol no es GERENTE")
        void should_Return403_When_UserHasNoPermission() throws Exception {
            // Arrange
            ParametrosReporteDTO parametros = new ParametrosReporteDTO(TipoReporte.PRODUCCION, LocalDate.now().minusDays(7), LocalDate.now(), FormatoReporte.PDF, null, null, true, null);

            // Act & Assert
            mockMvc.perform(post("/api/reportes/produccion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(parametros)))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Endpoint: POST /api/reportes/asistencia")
    @WithMockUser(roles = "GERENTE")
    class GenerarReporteAsistenciaIT {

        @Test
        @DisplayName("Happy Path: Debe generar un reporte de asistencia y verificar la BD")
        void should_GenerateAssistanceReport_And_VerifyDatabase() throws Exception {
            // Arrange
            ParametrosReporteDTO parametros = new ParametrosReporteDTO(TipoReporte.ASISTENCIA, LocalDate.now().minusDays(7), LocalDate.now(), FormatoReporte.EXCEL, null, null, true, null);

            // Act & Assert
            mockMvc.perform(post("/api/reportes/asistencia")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(parametros)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.tipoReporte", is("ASISTENCIA")))
                    .andExpect(jsonPath("$.formato", is("EXCEL")));

            // DB Verification
            List<com.minacontrol.reportes.entity.ReporteGenerado> reportes = reporteGeneradoRepository.findAll();
            assertThat(reportes).hasSize(1);
            assertThat(reportes.get(0).getTipoReporte()).isEqualTo(TipoReporte.ASISTENCIA);
        }
    }

    @Nested
    @DisplayName("Endpoint: POST /api/reportes/costos-laborales")
    @WithMockUser(roles = "GERENTE")
    class GenerarReporteCostosLaboralesIT {

        @Test
        @DisplayName("Happy Path: Debe generar un reporte de costos y verificar la BD")
        void should_GenerateCostsReport_And_VerifyDatabase() throws Exception {
            // Arrange
            ParametrosReporteDTO parametros = new ParametrosReporteDTO(TipoReporte.COSTOS_LABORALES, LocalDate.now().minusDays(7), LocalDate.now(), FormatoReporte.CSV, null, null, true, null);

            // Act & Assert
            mockMvc.perform(post("/api/reportes/costos-laborales")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(parametros)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.tipoReporte", is("COSTOS_LABORALES")));

            // DB Verification
            assertThat(reporteGeneradoRepository.findAll()).hasSize(1);
        }

        @Test
        @DisplayName("Error 404: Debe devolver Not Found si no hay datos suficientes")
        void should_Return404_When_InsufficientData() throws Exception {
            // Arrange
            calculoNominaRepository.deleteAll(); // Eliminar datos de nómina para forzar el error
            ParametrosReporteDTO parametros = new ParametrosReporteDTO(TipoReporte.COSTOS_LABORALES, LocalDate.now().minusDays(7), LocalDate.now(), FormatoReporte.PDF, null, null, true, null);

            // Act & Assert
            mockMvc.perform(post("/api/reportes/costos-laborales")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(parametros)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.error", is("Not Found")))
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Endpoint: POST /api/reportes/exportar-datos")
    @WithMockUser(roles = "GERENTE")
    class ExportarDatosOperacionalesIT {

        @Test
        @DisplayName("Happy Path: Debe exportar datos operacionales y verificar la BD")
        void should_ExportData_And_VerifyDatabase() throws Exception {
            // Arrange
            DatosOperacionalesDTO parametros = new DatosOperacionalesDTO(List.of("produccion"), FormatoReporte.CSV, LocalDate.now().minusDays(7), LocalDate.now(), null);

            // Act & Assert
            mockMvc.perform(post("/api/reportes/exportar-datos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(parametros)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.tipoReporte", is("OPERACIONAL")));

            // DB Verification
            assertThat(reporteGeneradoRepository.findAll()).hasSize(1);
        }
    }
}