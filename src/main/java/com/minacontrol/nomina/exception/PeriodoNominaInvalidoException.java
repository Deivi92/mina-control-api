package com.minacontrol.nomina.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PeriodoNominaInvalidoException extends RuntimeException {
    public PeriodoNominaInvalidoException(String message) {
        super(message);
    }
}
