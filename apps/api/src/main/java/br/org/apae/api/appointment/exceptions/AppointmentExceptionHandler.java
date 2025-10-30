package br.org.apae.api.appointment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.org.apae.api.appointment.domain.exceptions.AppointmentNotFoundException;
import br.org.apae.api.appointment.domain.exceptions.ConsultationHistoryBadRequestException;
import br.org.apae.api.appointment.domain.exceptions.ConsultationHistoryConflictException;
import br.org.apae.api.appointment.domain.exceptions.ConsultationHistoryNotFoundException;
import br.org.apae.api.common.exceptions.types.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AppointmentExceptionHandler {
  @ExceptionHandler(AppointmentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAppointmentNotFoundException(AppointmentNotFoundException ex,
      HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConsultationHistoryBadRequestException.class)
  public ResponseEntity<ErrorResponse> handleConsultationHistoryBadRequestException(
      ConsultationHistoryBadRequestException ex,
      HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConsultationHistoryConflictException.class)
  public ResponseEntity<ErrorResponse> handleConsultationHistoryConflictException(
      ConsultationHistoryConflictException ex,
      HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ConsultationHistoryNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleConsultationHistoryNotFoundException(
      ConsultationHistoryNotFoundException ex,
      HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }
}
