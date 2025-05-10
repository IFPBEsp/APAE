package br.org.apae.documentos_digitalizados.global;

import br.org.apae.documentos_digitalizados.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildErrorResponse(Exception ex, HttpStatus status, String errorCode, WebRequest request) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .errorCode(errorCode)
                .build();
    }

    @ExceptionHandler(CriacaoBucketException.class)
    public ResponseEntity<ErrorResponse> handleCriacaoBucket(CriacaoBucketException ex, WebRequest request) {
        return new ResponseEntity<>(
                buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, "BUCKET_CREATION_FAILED", request),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(DeletarBucketException.class)
    public ResponseEntity<ErrorResponse> handleDeletarBucket(DeletarBucketException ex, WebRequest request) {
        return new ResponseEntity<>(
                buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, "BUCKET_DELETION_FAILED", request),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(DeletarObjetosException.class)
    public ResponseEntity<ErrorResponse> handleDeletarObjetos(DeletarObjetosException ex, WebRequest request) {
        return new ResponseEntity<>(
                buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, "OBJECTS_DELETION_FAILED", request),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(DiretorioException.class)
    public ResponseEntity<ErrorResponse> handleDiretorio(DiretorioException ex, WebRequest request) {
        return new ResponseEntity<>(
                buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, "DIRECTORY_ERROR", request),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(ExisteBucketException.class)
    public ResponseEntity<ErrorResponse> handleExisteBucket(ExisteBucketException ex, WebRequest request) {
        return new ResponseEntity<>(
                buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "BUCKET_ALREADY_EXISTS", request),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ListagemBucketException.class)
    public ResponseEntity<ErrorResponse> handleListagemBucket(ListagemBucketException ex, WebRequest request) {
        return new ResponseEntity<>(
                buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, "BUCKET_LISTING_FAILED", request),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, "UNEXPECTED_ERROR", request),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
