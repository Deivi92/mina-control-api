
package com.minacontrol.turnos.unit;

import com.minacontrol.turnos.entity.TipoTurno;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para Entidad TipoTurno")
class TipoTurnoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Validación de nombre no debe estar en blanco")
    void should_failValidation_when_nombreIsBlank() {
        // Arrange
        var turno = TipoTurno.builder()
                .nombre("") // Blank
                .horaInicio(LocalTime.of(8, 0))
                .horaFin(LocalTime.of(16, 0))
                .build();

        // Act
        Set<ConstraintViolation<TipoTurno>> violations = validator.validate(turno);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("nombre"));
    }

    @Test
    @DisplayName("Validación de hora de inicio no debe ser nula")
    void should_failValidation_when_horaInicioIsNull() {
        // Arrange
        var turno = TipoTurno.builder()
                .nombre("Turno de Mañana")
                .horaInicio(null) // Null
                .horaFin(LocalTime.of(16, 0))
                .build();

        // Act
        Set<ConstraintViolation<TipoTurno>> violations = validator.validate(turno);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("horaInicio"));
    }

    @Test
    @DisplayName("Validación de hora de fin no debe ser nula")
    void should_failValidation_when_horaFinIsNull() {
        // Arrange
        var turno = TipoTurno.builder()
                .nombre("Turno de Mañana")
                .horaInicio(LocalTime.of(8, 0))
                .horaFin(null) // Null
                .build();

        // Act
        Set<ConstraintViolation<TipoTurno>> violations = validator.validate(turno);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("horaFin"));
    }

    @Test
    @DisplayName("Validación exitosa con datos correctos")
    void should_passValidation_when_dataIsCorrect() {
        // Arrange
        var turno = TipoTurno.builder()
                .nombre("Turno Válido")
                .horaInicio(LocalTime.of(9, 0))
                .horaFin(LocalTime.of(17, 0))
                .descripcion("Una descripción válida")
                .activo(true)
                .build();

        // Act
        Set<ConstraintViolation<TipoTurno>> violations = validator.validate(turno);

        // Assert
        assertThat(violations).isEmpty();
    }
}
