package com.minacontrol.logistica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EstadoDespachoInvalidoException extends RuntimeException {
    public EstadoDespachoInvalidoException(String message) {
        super(message);
    }
}
