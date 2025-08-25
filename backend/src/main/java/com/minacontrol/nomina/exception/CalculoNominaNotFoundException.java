package com.minacontrol.nomina.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CalculoNominaNotFoundException extends RuntimeException {
    public CalculoNominaNotFoundException(String message) {
        super(message);
    }
}
