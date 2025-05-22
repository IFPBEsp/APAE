package br.org.apae.documentos_pessoais_digitalizados.global;

import br.org.apae.documentos_pessoais_digitalizados.application.exceptions.BucketNotFoundException;
import br.org.apae.documentos_pessoais_digitalizados.application.exceptions.DocumentNotFoundException;
import br.org.apae.documentos_pessoais_digitalizados.application.exceptions.FileIsEmptyException;
import br.org.apae.documentos_pessoais_digitalizados.application.exceptions.PersonalDocumentServiceException;
import br.org.apae.documentos_pessoais_digitalizados.infrastructure.storage.exceptions.PersonalDocumentStorageException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<StandardError> handleMissingPart(
            MissingServletRequestPartException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Parte do pedido ausente",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(FileIsEmptyException.class)
    public ResponseEntity<StandardError> handleFileIsEmpty(
            FileIsEmptyException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Arquivo vazio",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<StandardError> handleNumberFormat(
            NumberFormatException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Formato de número inválido",
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

    @ExceptionHandler(PersonalDocumentServiceException.class)
    public ResponseEntity<StandardError> handleServiceException(
            PersonalDocumentServiceException e, HttpServletRequest request) {

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

    @ExceptionHandler(PersonalDocumentStorageException.class)
    public ResponseEntity<StandardError> handleStorageException(
            PersonalDocumentStorageException e, HttpServletRequest request) {

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
