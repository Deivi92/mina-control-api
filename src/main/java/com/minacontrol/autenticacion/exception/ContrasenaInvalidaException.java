package com.minacontrol.autenticacion.exception;

public class ContrasenaInvalidaException extends RuntimeException {
    public ContrasenaInvalidaException() {
        super();
    }

    public ContrasenaInvalidaException(String message) {
        super(message);
    }

    public ContrasenaInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContrasenaInvalidaException(Throwable cause) {
        super(cause);
    }
}
