package com.minacontrol.empleado.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmpleadoAlreadyInactiveException extends RuntimeException {
    public EmpleadoAlreadyInactiveException(String message) {
        super(message);
    }
}
