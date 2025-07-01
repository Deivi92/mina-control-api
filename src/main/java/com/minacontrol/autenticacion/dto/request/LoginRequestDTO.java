package com.minacontrol.autenticacion.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    String email,

    @NotBlank(message = "La contraseña no puede estar vacía")
    String password
) {}
