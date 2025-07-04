package com.minacontrol.empleado.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CrearEmpleadoRequestDTO(
    @NotBlank(message = "Los nombres no pueden estar vacíos")
    @Size(max = 100, message = "Los nombres no pueden exceder los 100 caracteres")
    String nombres,

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Size(max = 100, message = "Los apellidos no pueden exceder los 100 caracteres")
    String apellidos,

    @NotBlank(message = "El número de identificación no puede estar vacío")
    @Size(max = 50, message = "El número de identificación no puede exceder los 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9\\-]+$", message = "El número de identificación solo puede contener caracteres alfanuméricos y guiones")
    String numeroIdentificacion,

    @NotBlank(message = "El email no puede estar vacío")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    @Email(message = "El email debe ser válido")
    String email,

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    String telefono,

    @NotBlank(message = "El cargo no puede estar vacío")
    @Size(max = 100, message = "El cargo no puede exceder los 100 caracteres")
    String cargo,

    @NotNull(message = "La fecha de contratación no puede ser nula")
    @PastOrPresent(message = "La fecha de contratación no puede ser futura")
    LocalDate fechaContratacion,

    @NotNull(message = "El salario base no puede ser nulo")
    BigDecimal salarioBase,

    @NotBlank(message = "El rol del sistema no puede estar vacío")
    String rolSistema
) {
    @Builder
    public CrearEmpleadoRequestDTO {}
}
