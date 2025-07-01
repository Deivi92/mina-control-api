package com.minacontrol.autenticacion.exception;

public class UsuarioYaExisteException extends RuntimeException {
    public UsuarioYaExisteException() {
        super();
    }

    public UsuarioYaExisteException(String message) {
        super(message);
    }

    public UsuarioYaExisteException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsuarioYaExisteException(Throwable cause) {
        super(cause);
    }
}
