package br.org.apae.documentos_digitalizados.global;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.org.apae.documentos_digitalizados.application.exceptions.DocumentNotFoundException;
import br.org.apae.documentos_digitalizados.application.exceptions.DocumentServiceException;
import br.org.apae.documentos_digitalizados.application.exceptions.FileIsEmptyException;
import br.org.apae.documentos_digitalizados.infrastructure.storage.exceptions.MinIOHandleException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<StandardError> handleIllegalArgumentException(
      IllegalArgumentException e, HttpServletRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;
    StandardError err = new StandardError(
        Instant.now(),
        status.value(),
        "Argumento inválido",
        e.getMessage(),
        request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<StandardError> handleGenericException(
      Exception e, HttpServletRequest request) {

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    StandardError err = new StandardError(
        Instant.now(),
        status.value(),
        "Erro inesperado",
        e.getMessage(),
        request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }
  
  @ExceptionHandler(MinIOHandleException.class)
  public ResponseEntity<StandardError> handleMinIOHandleException(
      MinIOHandleException e, HttpServletRequest request) {

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    StandardError err = new StandardError(
        Instant.now(),
        status.value(),
        "Erro ao manipular MinIO",
        e.getMessage(),
        request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(DocumentNotFoundException.class)
  public ResponseEntity<StandardError> handleDocumentNotFoundException(
      DocumentNotFoundException e, HttpServletRequest request) {

    HttpStatus status = HttpStatus.NOT_FOUND;
    StandardError err = new StandardError(
        Instant.now(),
        status.value(),
        "Documento não encontrado",
        e.getMessage(),
        request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(DocumentServiceException.class)
  public ResponseEntity<StandardError> handleDocumentServiceException(
      DocumentServiceException e, HttpServletRequest request) {

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    StandardError err = new StandardError(
        Instant.now(),
        status.value(),
        "Erro no serviço de documentos",
        e.getMessage(),
        request.getRequestURI()
    );
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(FileIsEmptyException.class)
  public ResponseEntity<StandardError> handleFileIsEmptyException(
    FileIsEmptyException e, HttpServletRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;
    StandardError err = new StandardError(
        Instant.now(),
        status.value(),
        "Arquivo vazio",
        "O arquivo enviado está vazio.",
        "/upload"
    );
    return ResponseEntity.status(status).body(err);
  }
}
