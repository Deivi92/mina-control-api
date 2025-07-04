
package com.minacontrol.empleado.dto.response;

import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.enums.RolSistema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmpleadoResponse(
    Long id,
    String nombres,
    String apellidos,
    String numeroIdentificacion,
    String email,
    String telefono,
    String cargo,
    LocalDate fechaContratacion,
    BigDecimal salarioBase,
    EstadoEmpleado estado,
    RolSistema rolSistema,
    Boolean tieneUsuario,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
