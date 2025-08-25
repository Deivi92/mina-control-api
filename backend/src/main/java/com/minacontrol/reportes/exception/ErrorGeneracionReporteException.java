package com.minacontrol.reportes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ErrorGeneracionReporteException extends RuntimeException {
    public ErrorGeneracionReporteException(String message, Throwable cause) {
        super(message, cause);
    }
}
