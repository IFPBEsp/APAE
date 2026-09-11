package br.org.apae.api.professional.application.exceptions;

import br.org.apae.api.professional.domain.exceptions.EmailConflictException;
import br.org.apae.api.professional.domain.exceptions.CpfConflictException;
import br.org.apae.api.professional.domain.exceptions.IdentityDocumentConflictException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.org.apae.api.common.exceptions.types.ErrorResponse;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.professional.domain.exceptions.ProfessionalDocumentConflictException;
import br.org.apae.api.professional.domain.exceptions.ProfessionalDocumentNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HealthProfessionalExceptionHandler {
  @ExceptionHandler(ProfessionalDocumentConflictException.class)
  public ResponseEntity<ErrorResponse> handleProfessionalDocumentConflictException(
      ProfessionalDocumentConflictException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(HealthProfessionalNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(HealthProfessionalNotFoundException ex,
      HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EmailConflictException.class)
  public ResponseEntity<ErrorResponse> handleEmailConflictException(
      EmailConflictException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(CpfConflictException.class)
  public ResponseEntity<ErrorResponse> handleCpfConflictException(
      CpfConflictException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(IdentityDocumentConflictException.class)
  public ResponseEntity<ErrorResponse> handleIdentityDocumentConflictException(
      IdentityDocumentConflictException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ProfessionalDocumentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleProfessionalDocumentNotFound(
      ProfessionalDocumentNotFoundException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }
}
