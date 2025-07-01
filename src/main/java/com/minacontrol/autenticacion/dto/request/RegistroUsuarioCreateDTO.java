package com.minacontrol.autenticacion.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroUsuarioCreateDTO(
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    String email,

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String password
) {}
