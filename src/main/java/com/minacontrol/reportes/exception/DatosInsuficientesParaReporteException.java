package com.minacontrol.reportes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DatosInsuficientesParaReporteException extends RuntimeException {
    public DatosInsuficientesParaReporteException(String message) {
        super(message);
    }
}
