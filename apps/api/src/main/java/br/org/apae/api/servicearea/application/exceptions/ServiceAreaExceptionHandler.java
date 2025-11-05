package br.org.apae.api.servicearea.application.exceptions;

import br.org.apae.api.common.exceptions.types.ErrorResponse;
import br.org.apae.api.servicearea.domain.exceptions.ServiceAreaConflictException;
import br.org.apae.api.servicearea.domain.exceptions.ServiceAreaNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServiceAreaExceptionHandler {

    @ExceptionHandler(ServiceAreaConflictException.class)
    public ResponseEntity<ErrorResponse> handleServiceAreaConflictException(
            ServiceAreaConflictException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ServiceAreaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleServiceAreaNotFound(ServiceAreaNotFoundException ex,
            HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}

