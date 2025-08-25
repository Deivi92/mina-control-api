package com.minacontrol.autenticacion.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException() {
        super();
    }

    public UsuarioNoEncontradoException(String message) {
        super(message);
    }

    public UsuarioNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsuarioNoEncontradoException(Throwable cause) {
        super(cause);
    }
}
