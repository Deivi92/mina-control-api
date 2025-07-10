
package com.minacontrol.produccion.unit;

import com.minacontrol.produccion.entity.RegistroProduccion;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para la Entidad RegistroProduccion")
public class RegistroProduccionTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Debe crear una instancia de RegistroProduccion correctamente con el Builder")
    void should_CreateInstanceCorrectly_WithBuilder() {
        // Arrange
        LocalDate fecha = LocalDate.of(2025, 7, 15);
        BigDecimal cantidad = new BigDecimal("15.75");

        // Act
        RegistroProduccion registro = RegistroProduccion.builder()
                .id(1L)
                .empleadoId(10L)
                .tipoTurnoId(2L)
                .fechaRegistro(fecha)
                .cantidadExtraidaToneladas(cantidad)
                .ubicacionExtraccion("Sector B")
                .observaciones("Producción estándar")
                .validado(false)
                .build();

        // Assert
        assertThat(registro).isNotNull();
        assertThat(registro.getId()).isEqualTo(1L);
        assertThat(registro.getEmpleadoId()).isEqualTo(10L);
        assertThat(registro.getTipoTurnoId()).isEqualTo(2L);
        assertThat(registro.getFechaRegistro()).isEqualTo(fecha);
        assertThat(registro.getCantidadExtraidaToneladas()).isEqualTo(cantidad);
        assertThat(registro.getUbicacionExtraccion()).isEqualTo("Sector B");
        assertThat(registro.getObservaciones()).isEqualTo("Producción estándar");
        assertThat(registro.isValidado()).isFalse();
    }

    @Test
    @DisplayName("Debe pasar la validación con datos correctos")
    void should_PassValidation_When_DataIsCorrect() {
        // Arrange
        RegistroProduccion registro = RegistroProduccion.builder()
            .empleadoId(1L)
            .tipoTurnoId(1L)
            .fechaRegistro(LocalDate.now())
            .cantidadExtraidaToneladas(BigDecimal.TEN)
            .ubicacionExtraccion("Veta Principal")
            .build();
        
        // Act
        Set<ConstraintViolation<RegistroProduccion>> violations = validator.validate(registro);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Debe fallar la validación si la cantidad es nula, cero o negativa")
    void should_FailValidation_When_CantidadIsInvalid() {
        // Test con nulo
        RegistroProduccion registroNulo = RegistroProduccion.builder().cantidadExtraidaToneladas(null).build();
        Set<ConstraintViolation<RegistroProduccion>> violationsNulo = validator.validate(registroNulo);
        assertThat(violationsNulo).anyMatch(v -> v.getPropertyPath().toString().equals("cantidadExtraidaToneladas"));

        // Test con cero
        RegistroProduccion registroCero = RegistroProduccion.builder().cantidadExtraidaToneladas(BigDecimal.ZERO).build();
        Set<ConstraintViolation<RegistroProduccion>> violationsCero = validator.validate(registroCero);
        assertThat(violationsCero).anyMatch(v -> v.getPropertyPath().toString().equals("cantidadExtraidaToneladas"));

        // Test con negativo
        RegistroProduccion registroNegativo = RegistroProduccion.builder().cantidadExtraidaToneladas(new BigDecimal("-1")).build();
        Set<ConstraintViolation<RegistroProduccion>> violationsNegativo = validator.validate(registroNegativo);
        assertThat(violationsNegativo).anyMatch(v -> v.getPropertyPath().toString().equals("cantidadExtraidaToneladas"));
    }

    @Test
    @DisplayName("Debe fallar la validación si la ubicación está en blanco")
    void should_FailValidation_When_UbicacionIsBlank() {
        // Arrange
        RegistroProduccion registro = RegistroProduccion.builder()
            .empleadoId(1L)
            .tipoTurnoId(1L)
            .fechaRegistro(LocalDate.now())
            .cantidadExtraidaToneladas(BigDecimal.TEN)
            .ubicacionExtraccion("  ") // En blanco
            .build();

        // Act
        Set<ConstraintViolation<RegistroProduccion>> violations = validator.validate(registro);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("ubicacionExtraccion"));
    }
}
