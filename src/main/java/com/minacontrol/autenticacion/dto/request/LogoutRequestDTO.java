package com.minacontrol.autenticacion.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequestDTO(
    @NotBlank(message = "El refresh token no puede estar vacío")
    String refreshToken
) {}
