package br.org.apae.api.common.exceptions.handler;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.HttpMediaTypeNotSupportedException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import br.org.apae.api.common.exceptions.types.ErrorResponse;
import br.org.apae.api.common.exceptions.types.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  private static final String VALIDATION_ERROR = "Erro de validação";


  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {

    List<ValidationErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new ValidationErrorResponse.FieldError(
                    error.getField(),
                    error.getDefaultMessage()
            ))
            .collect(Collectors.toList());

    ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            VALIDATION_ERROR,
            request.getRequestURI(),
            fieldErrors
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

   @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpServletRequest request) {
        
        List<ValidationErrorResponse.FieldError> fieldErrors = ex.getParameterValidationResults()
                .stream()
                .flatMap(result -> result.getResolvableErrors()
                .stream()
                .map(error -> new ValidationErrorResponse.FieldError(
                                result.getMethodParameter().getParameterName(),
                                error.getDefaultMessage()
                )))
                .collect(Collectors.toList());
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                VALIDATION_ERROR,
                request.getRequestURI(),
                fieldErrors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
 }


  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
          IllegalArgumentException ex,
          HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
          ConstraintViolationException ex,
          HttpServletRequest request) {

    List<ValidationErrorResponse.FieldError> fieldErrors = ex.getConstraintViolations()
            .stream()
            .map(violation -> {
                String propertyPath = violation.getPropertyPath().toString();
                String field = propertyPath.contains(".")
                        ? propertyPath.substring(propertyPath.lastIndexOf('.') + 1)
                        : propertyPath;
                return new ValidationErrorResponse.FieldError(field, violation.getMessage());
            })
            .collect(Collectors.toList());

    ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            VALIDATION_ERROR,
            request.getRequestURI(),
            fieldErrors
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(
          MaxUploadSizeExceededException ex,
          HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "O arquivo excede o limite máximo permitido de 10MB",
            request.getRequestURI());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
}

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
        public ResponseEntity<ErrorResponse> handleMediaTypeNotSupportedException(
                HttpMediaTypeNotSupportedException ex,
                HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(),
                "Tipo de mídia não suportado.",
                request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorResponse> handleResponseStatusException(
          ResponseStatusException ex,
          HttpServletRequest request) {

    HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
    if (status == null) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    if (status.is5xxServerError()) {
      String correlationId = UUID.randomUUID().toString();

      logger.error("Erro de servidor propagado como ResponseStatusException. status={} path={} correlationId={}",
              status.value(), request.getRequestURI(), correlationId, ex);

      ErrorResponse errorResponse = new ErrorResponse(
              status.value(),
              status.getReasonPhrase(),
              "Ocorreu um erro interno. Informe o código " + correlationId + " ao suporte.",
              request.getRequestURI());

      return new ResponseEntity<>(errorResponse, status);
    }

    String message = (ex.getReason() != null && !ex.getReason().isBlank())
            ? ex.getReason()
            : status.getReasonPhrase();

    logger.warn("Regra de negócio violada. status={} path={} message={}",
            status.value(), request.getRequestURI(), message);

    ErrorResponse errorResponse = new ErrorResponse(
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI());

    return new ResponseEntity<>(errorResponse, status);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {

         String correlationId = UUID.randomUUID().toString();
    
        logger.error("Erro interno. CorrelationId={}", correlationId, ex);
    
        ErrorResponse errorResponse = new ErrorResponse(

            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "Ocorreu um erro interno. Informe o código " + correlationId + " ao suporte.",
            request.getRequestURI());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}