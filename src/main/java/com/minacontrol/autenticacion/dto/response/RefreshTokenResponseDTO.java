package com.minacontrol.autenticacion.dto.response;

public record RefreshTokenResponseDTO(
    String accessToken,
    String refreshToken
) {}
