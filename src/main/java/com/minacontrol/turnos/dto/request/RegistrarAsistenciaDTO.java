
package com.minacontrol.turnos.dto.request;

import com.minacontrol.turnos.enums.TipoRegistro;
import jakarta.validation.constraints.NotNull;

public record RegistrarAsistenciaDTO(
        @NotNull Long empleadoId,
        @NotNull TipoRegistro tipoRegistro
) {}
