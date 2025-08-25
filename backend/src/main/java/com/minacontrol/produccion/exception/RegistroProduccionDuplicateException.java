package com.minacontrol.produccion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RegistroProduccionDuplicateException extends RuntimeException {
    public RegistroProduccionDuplicateException(String message) {
        super(message);
    }
}
