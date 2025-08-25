package com.minacontrol.reportes.unit;

import com.minacontrol.reportes.entity.ReporteGenerado;
import com.minacontrol.reportes.enums.FormatoReporte;
import com.minacontrol.reportes.enums.TipoReporte;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para la Entidad ReporteGenerado")
class ReporteGeneradoTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Debe crear una instancia de ReporteGenerado correctamente")
    void should_CreateInstanceCorrectly() {
        // Arrange & Act
        ReporteGenerado reporte = ReporteGenerado.builder()
                .id(1L)
                .tipoReporte(TipoReporte.PRODUCCION)
                .nombreReporte("Reporte de Producción Mensual")
                .fechaGeneracion(LocalDateTime.now())
                .rutaArchivo("/reports/produccion_mensual.pdf")
                .formato(FormatoReporte.PDF)
                .generadoPor(10L)
                .build();

        // Assert
        assertThat(reporte).isNotNull();
        assertThat(reporte.getNombreReporte()).isEqualTo("Reporte de Producción Mensual");
        assertThat(reporte.getFormato()).isEqualTo(FormatoReporte.PDF);
    }

    @Test
    @DisplayName("Debe pasar la validación con datos correctos")
    void should_PassValidation_When_DataIsCorrect() {
        // Arrange
        ReporteGenerado reporte = ReporteGenerado.builder()
                .tipoReporte(TipoReporte.ASISTENCIA)
                .nombreReporte("Reporte de Asistencia")
                .fechaGeneracion(LocalDateTime.now())
                .fechaInicioDatos(LocalDate.now().minusDays(7))
                .fechaFinDatos(LocalDate.now())
                .rutaArchivo("/reports/asistencia.csv")
                .formato(FormatoReporte.CSV)
                .generadoPor(1L)
                .build();

        // Act
        Set<ConstraintViolation<ReporteGenerado>> violations = validator.validate(reporte);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Debe fallar la validación si los campos obligatorios son nulos")
    void should_FailValidation_When_RequiredFieldsAreNull() {
        // Arrange
        ReporteGenerado reporte = new ReporteGenerado(); // Campos nulos

        // Act
        Set<ConstraintViolation<ReporteGenerado>> violations = validator.validate(reporte);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tipoReporte"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombreReporte"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("rutaArchivo"))).isTrue();
    }
}
