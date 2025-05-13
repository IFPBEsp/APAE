package br.org.apae.documentos_medicos.global.exceptionhandler;

import br.org.apae.documentos_medicos.application.exceptions.BucketNotFoundException;
import br.org.apae.documentos_medicos.application.exceptions.DocumentNotFoundException;
import br.org.apae.documentos_medicos.application.exceptions.MedicalDocumentServiceException;
import br.org.apae.documentos_medicos.infrastructure.storage.exceptions.MedicalDocumentStorageException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<StandardError> handleDocumentNotFound(
            DocumentNotFoundException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Documento não encontrado",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BucketNotFoundException.class)
    public ResponseEntity<StandardError> handleBucketNotFound(
            BucketNotFoundException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Bucket não encontrado",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MedicalDocumentServiceException.class)
    public ResponseEntity<StandardError> handleServiceException(
            MedicalDocumentServiceException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Erro no serviço de documentos médicos",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MedicalDocumentStorageException.class)
    public ResponseEntity<StandardError> handleStorageException(
            MedicalDocumentStorageException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Erro de armazenamento",
                e.getMessage(),
                request.getRequestURI()
        );
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
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }
}
