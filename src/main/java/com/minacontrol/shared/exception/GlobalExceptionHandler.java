package com.minacontrol.shared.exception;

import com.minacontrol.autenticacion.exception.ContrasenaInvalidaException;
import com.minacontrol.autenticacion.exception.UsuarioNoEncontradoException;
import com.minacontrol.autenticacion.exception.UsuarioYaExisteException;
import com.minacontrol.empleado.exception.EmpleadoNotFoundException;
import com.minacontrol.shared.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmpleadoNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmpleadoNotFound(EmpleadoNotFoundException ex, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsuarioYaExiste(UsuarioYaExisteException ex, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UsuarioNoEncontradoException.class, ContrasenaInvalidaException.class})
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedExceptions(RuntimeException ex, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
