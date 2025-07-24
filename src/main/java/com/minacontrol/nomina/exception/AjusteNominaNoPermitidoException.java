package com.minacontrol.nomina.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AjusteNominaNoPermitidoException extends RuntimeException {
    public AjusteNominaNoPermitidoException(String message) {
        super(message);
    }
}
