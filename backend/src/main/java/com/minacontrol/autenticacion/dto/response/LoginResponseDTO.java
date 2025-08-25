package com.minacontrol.autenticacion.dto.response;

public record LoginResponseDTO(
    String accessToken,
    String refreshToken
) {}
