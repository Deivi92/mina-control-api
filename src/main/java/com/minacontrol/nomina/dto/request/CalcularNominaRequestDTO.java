package com.minacontrol.nomina.dto.request;

import jakarta.validation.constraints.NotNull;

public record CalcularNominaRequestDTO(
        @NotNull
        Long periodoId
) {
}
