package com.minacontrol.nomina.dto.response;

public record CalculoNominaResumenDTO(
        int numeroDeEmpleadosCalculados,
        java.math.BigDecimal montoTotal
) {
}
