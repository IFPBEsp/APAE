package br.org.apae.profissional_da_saude.api.advice;

import br.org.apae.profissional_da_saude.domain.exception.DadosInvalidosException;
import br.org.apae.profissional_da_saude.domain.exception.EntidadeNaoEncontradaException;
import br.org.apae.profissional_da_saude.domain.exception.ValidacaoNegocioException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import br.org.apae.profissional_da_saude.application.service.exceptions.AgendamentoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ValidacaoNegocioException.class)
    public ResponseEntity<Object> handleValidacaoNegocio(ValidacaoNegocioException ex) {
        return buildErrorResponse(ex.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<Object> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex) {
        return buildErrorResponse(ex.getMessage(), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<Object> handleDadosInvalidos(DadosInvalidosException ex) {
        return buildErrorResponse(ex.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        return buildErrorResponse("Erro de validação", fieldErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return buildErrorResponse("Erro interno no servidor", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildErrorResponse(String mensagem, Object detalhes, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put(ErrorResponseAttributes.MENSAGEM, mensagem);
        if (detalhes != null) {
            body.put(ErrorResponseAttributes.DETALHES, detalhes);
        }
        body.put(ErrorResponseAttributes.STATUS, status.value());
        body.put(ErrorResponseAttributes.TIMESTAMP, LocalDateTime.now());
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(AgendamentoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(AgendamentoNaoEncontradoException ex) {
        return buildErrorResponse(ex.getMessage(), null, HttpStatus.NOT_FOUND);
    }

}
