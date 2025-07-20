package com.minacontrol.logistica.unit;

import com.minacontrol.logistica.entity.Despacho;
import com.minacontrol.logistica.domain.EstadoDespacho;
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

@DisplayName("Pruebas Unitarias para la Entidad Despacho")
class DespachoTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Debe crear una instancia de Despacho correctamente")
    void should_CreateInstanceCorrectly() {
        // Arrange & Act
        Despacho despacho = new Despacho();
        despacho.setId(1L);
        despacho.setNumeroDespacho("DES-001");
        despacho.setDestino("Planta Central");
        despacho.setEstado(EstadoDespacho.PROGRAMADO);

        // Assert
        assertThat(despacho).isNotNull();
        assertThat(despacho.getNumeroDespacho()).isEqualTo("DES-001");
    }

    @Test
    @DisplayName("Debe pasar la validación con datos correctos")
    void should_PassValidation_When_DataIsCorrect() {
        // Arrange
        Despacho despacho = new Despacho();
        despacho.setNombreConductor("Juan Perez");
        despacho.setPlacaVehiculo("ABC-123");
        despacho.setCantidadDespachadaToneladas(new BigDecimal("25.5"));
        despacho.setDestino("Destino Válido");
        despacho.setFechaProgramada(LocalDate.now());

        // Act
        Set<ConstraintViolation<Despacho>> violations = validator.validate(despacho);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Debe fallar la validación si los campos obligatorios están en blanco")
    void should_FailValidation_When_RequiredFieldsAreBlank() {
        // Arrange
        Despacho despacho = new Despacho(); // Campos en blanco

        // Act
        Set<ConstraintViolation<Despacho>> violations = validator.validate(despacho);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombreConductor"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("placaVehiculo"))).isTrue();
    }

    @Test
    @DisplayName("Debe fallar la validación si la cantidad es negativa")
    void should_FailValidation_When_CantidadIsNegative() {
        // Arrange
        Despacho despacho = new Despacho();
        despacho.setNombreConductor("Conductor");
        despacho.setPlacaVehiculo("XYZ-789");
        despacho.setDestino("Destino");
        despacho.setFechaProgramada(LocalDate.now());
        despacho.setCantidadDespachadaToneladas(new BigDecimal("-10.0"));

        // Act
        Set<ConstraintViolation<Despacho>> violations = validator.validate(despacho);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cantidadDespachadaToneladas"))).isTrue();
    }
}
