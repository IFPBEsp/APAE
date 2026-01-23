package br.org.apae.api.appointment.exceptions;

import br.org.apae.api.appointment.domain.exceptions.AppointmentAlreadyCancelledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.org.apae.api.appointment.domain.exceptions.AppointmentNotFoundException;
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


  @ExceptionHandler(AppointmentAlreadyCancelledException.class)
  public ResponseEntity<ErrorResponse> handleAppointmentAlreadyCancelled(AppointmentAlreadyCancelledException ex,
                                                                          HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }





}
