package br.org.apae.api.servicetype.application.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.org.apae.api.common.exceptions.types.ErrorResponse;
import br.org.apae.api.professional.domain.exceptions.ServiceTypeConflictException;
import br.org.apae.api.professional.domain.exceptions.ServiceTypeNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServiceTypeExceptionHandler {
  @ExceptionHandler(ServiceTypeConflictException.class)
  public ResponseEntity<ErrorResponse> handleServiceTypeConflictException(
      ServiceTypeConflictException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ServiceTypeNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleServiceTypeNotFound(
      ServiceTypeNotFoundException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }
}
