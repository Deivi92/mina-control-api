package com.minacontrol.empleado.exception;

public class EmpleadoAlreadyExistsException extends RuntimeException {
    public EmpleadoAlreadyExistsException(String message) {
        super(message);
    }
}
