package com.minacontrol.produccion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RegistroProduccionValidatedException extends RuntimeException {
    public RegistroProduccionValidatedException(String message) {
        super(message);
    }
}
