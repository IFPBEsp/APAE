package br.org.apae.api.patient.application.exceptions;

import br.org.apae.api.common.exceptions.types.ErrorResponse;
import br.org.apae.api.patient.domain.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PatientExceptionHandler {

  @ExceptionHandler(InvalidDataException.class)
  public ResponseEntity<ErrorResponse> handleInvalidData(
          InvalidDataException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PatientNotFoundException.class)
  public ResponseEntity<ErrorResponse> handlePatientNotFound(
          PatientNotFoundException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(PatientConflictException.class)
  public ResponseEntity<ErrorResponse> handlePatientConflict(
          PatientConflictException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(GuardianNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleGuardianNotFound(
          GuardianNotFoundException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ParentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleParentNotFound(
          ParentNotFoundException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ParentMismatchException.class)
  public ResponseEntity<ErrorResponse> handleParentMismatch(
          ParentMismatchException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(VaccineNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleVaccineNotFound(
          VaccineNotFoundException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(VaccineMismatchException.class)
  public ResponseEntity<ErrorResponse> handleVaccineMismatch(
          VaccineMismatchException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DisorderConflictException.class)
  public ResponseEntity<ErrorResponse> handleDisorderConflict(
          DisorderConflictException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DisorderNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleDisorderNotFound(
          DisorderNotFoundException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DisorderMismatchException.class)
  public ResponseEntity<ErrorResponse> handleDisorderMismatch(
          DisorderMismatchException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AnnualRegistryConflictException.class)
  public ResponseEntity<ErrorResponse> handleAnnualRegistryConflict(
          AnnualRegistryConflictException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(RegistryNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleRegistryNotFound(
          RegistryNotFoundException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(RegistryOwnershipException.class)
  public ResponseEntity<ErrorResponse> handleRegistryOwnership(
          RegistryOwnershipException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleArgumentNotValidException(
          MethodArgumentNotValidException ex,
          HttpServletRequest request
  ) {
    ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(VaccineConflictException.class)
  public ResponseEntity<ProblemDetail> handleVaccineConflictException(VaccineConflictException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    problemDetail.setTitle("Conflito de Vacina");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
  }

  @ExceptionHandler(VaccineInUseException.class)
  public ResponseEntity<ProblemDetail> handleVaccineInUseException(VaccineInUseException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    problemDetail.setTitle("Vacina em Uso");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
  }
}
