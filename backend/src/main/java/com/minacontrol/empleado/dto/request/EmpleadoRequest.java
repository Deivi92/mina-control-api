
package com.minacontrol.empleado.dto.request;

import com.minacontrol.empleado.enums.RolSistema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmpleadoRequest(
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    String nombres,

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede tener más de 100 caracteres")
    String apellidos,

    @NotBlank(message = "El número de identificación no puede estar vacío")
    @Size(max = 20, message = "El número de identificación no puede tener más de 20 caracteres")
    String numeroIdentificacion,

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    String email,

    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres")
    String telefono,

    @NotBlank(message = "El cargo no puede estar vacío")
    String cargo,

    @NotNull(message = "La fecha de contratación no puede ser nula")
    @PastOrPresent(message = "La fecha de contratación no puede ser en el futuro")
    LocalDate fechaContratacion,

    @NotNull(message = "El salario base no puede ser nulo")
    @Positive(message = "El salario base debe ser un número positivo")
    BigDecimal salarioBase,

    @NotNull(message = "El rol del sistema no puede ser nulo")
    RolSistema rolSistema
) {}
