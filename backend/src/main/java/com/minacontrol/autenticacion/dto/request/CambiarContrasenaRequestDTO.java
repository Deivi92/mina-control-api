package com.minacontrol.autenticacion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambiarContrasenaRequestDTO(
    String oldPassword, // Removed @NotBlank

    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    @Size(min = 6, message = "La nueva contraseña debe tener al menos 6 caracteres")
    String newPassword,

    String token // Removed @NotBlank
) {}
