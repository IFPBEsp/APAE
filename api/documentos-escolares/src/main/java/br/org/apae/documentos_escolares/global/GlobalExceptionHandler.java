package br.org.apae.documentos_escolares.global;

import br.org.apae.documentos_escolares.domain.exception.DocumentoDuplicadoException;
import br.org.apae.documentos_escolares.domain.exception.DocumentoEscolarNaoEncontradoException;
import br.org.apae.documentos_escolares.domain.exception.ErroAoDeletarDocumentoException;
import br.org.apae.documentos_escolares.domain.exception.ErroAoSalvarDocumentoException;
import br.org.apae.documentos_escolares.domain.exception.ArquivoVazioException;
import br.org.apae.documentos_escolares.domain.exception.BucketNaoExisteException;
import br.org.apae.documentos_escolares.domain.exception.DocumentoEscolarException;
import br.org.apae.documentos_escolares.domain.exception.UploadDocumentoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildResponseEntity(HttpStatus status, String message) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ArquivoVazioException.class)
    public ResponseEntity<?> handleArquivoVazio(ArquivoVazioException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BucketNaoExisteException.class)
    public ResponseEntity<?> handleBucketNaoExiste(BucketNaoExisteException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DocumentoDuplicadoException.class)
    public ResponseEntity<?> handleDocumentoDuplicado(DocumentoDuplicadoException ex) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DocumentoEscolarException.class)
    public ResponseEntity<?> handleDocumentoEscolarException(DocumentoEscolarException ex) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(DocumentoEscolarNaoEncontradoException.class)
    public ResponseEntity<?> handleDocumentoNaoEncontrado(DocumentoEscolarNaoEncontradoException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ErroAoDeletarDocumentoException.class)
    public ResponseEntity<?> handleErroAoDeletar(ErroAoDeletarDocumentoException ex) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(ErroAoSalvarDocumentoException.class)
    public ResponseEntity<?> handleErroAoSalvar(ErroAoSalvarDocumentoException ex) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(UploadDocumentoException.class)
    public ResponseEntity<?> handleUploadDocumentoException(UploadDocumentoException ex) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado: " + ex.getMessage());
    }
}
