package br.org.apae.api.appointment.exceptions;

import br.org.apae.api.appointment.domain.exceptions.AppointmentAlreadyCancelledException;
import br.org.apae.api.appointment.domain.exceptions.AppointmentNotFoundException;
import br.org.apae.api.appointment.domain.exceptions.ProfessionalUnavailableException;
import br.org.apae.api.common.exceptions.types.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppointmentExceptionHandler {

  @ExceptionHandler(AppointmentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAppointmentNotFound(
          AppointmentNotFoundException ex,
          HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AppointmentAlreadyCancelledException.class)
  public ResponseEntity<ErrorResponse> handleAppointmentAlreadyCancelled(
          AppointmentAlreadyCancelledException ex,
          HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ProfessionalUnavailableException.class)
  public ResponseEntity<ErrorResponse> handleProfessionalUnavailable(
          ProfessionalUnavailableException ex,
          HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpServletRequest request) {

    String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(FieldError::getDefaultMessage)
            .orElse("Validation error");

    ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            message,
            request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
          HttpMessageNotReadableException ex,
          HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
}
