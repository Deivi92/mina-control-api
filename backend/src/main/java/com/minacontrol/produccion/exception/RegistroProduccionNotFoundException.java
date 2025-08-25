package com.minacontrol.produccion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RegistroProduccionNotFoundException extends RuntimeException {
    public RegistroProduccionNotFoundException(String message) {
        super(message);
    }
}
