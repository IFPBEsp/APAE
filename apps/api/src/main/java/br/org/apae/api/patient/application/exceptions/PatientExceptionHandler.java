package br.org.apae.api.patient.application.exceptions;

import br.org.apae.api.common.exceptions.types.ErrorResponse;
import br.org.apae.api.patient.domain.exceptions.AnnualRegistryConflictException;
import br.org.apae.api.patient.domain.exceptions.DisorderConflictException;
import br.org.apae.api.patient.domain.exceptions.DisorderMismatchException;
import br.org.apae.api.patient.domain.exceptions.DisorderNotFoundException;
import br.org.apae.api.patient.domain.exceptions.GuardianNotFoundException;
import br.org.apae.api.patient.domain.exceptions.InvalidDataException;
import br.org.apae.api.patient.domain.exceptions.ParentMismatchException;
import br.org.apae.api.patient.domain.exceptions.ParentNotFoundException;
import br.org.apae.api.patient.domain.exceptions.PatientConflictException;
import br.org.apae.api.patient.domain.exceptions.PatientNotFoundException;
import br.org.apae.api.patient.domain.exceptions.RegistryNotFoundException;
import br.org.apae.api.patient.domain.exceptions.RegistryOwnershipException;
import br.org.apae.api.patient.domain.exceptions.VaccineMismatchException;
import br.org.apae.api.patient.domain.exceptions.VaccineNotFoundException;
import br.org.apae.api.patient.domain.exceptions.VaccineConflictException;
import br.org.apae.api.patient.domain.exceptions.VaccineInUseException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
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
  
    @ExceptionHandler(VaccineConflictException.class)
  public ResponseEntity<ErrorResponse> handleVaccineConflict(
          VaccineConflictException ex,
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

  @ExceptionHandler(VaccineInUseException.class)
  public ResponseEntity<ErrorResponse> handleVaccineInUse(
          VaccineInUseException ex,
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
}
