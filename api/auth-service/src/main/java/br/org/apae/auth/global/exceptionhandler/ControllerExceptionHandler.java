package br.org.apae.auth.global.exceptionhandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.org.apae.auth.application.service.exceptions.IncorrectLoginException;
import br.org.apae.auth.infrastructure.util.exceptions.ExternalServiceException;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<StandardError> handleIllegalArgumentException(
                        IllegalArgumentException e,
                        HttpServletRequest request) {

                HttpStatus status = HttpStatus.BAD_REQUEST;
                StandardError err = new StandardError(
                                Instant.now(),
                                status.value(),
                                "Argumento inválido",
                                e.getMessage(),
                                request.getRequestURI());
                return ResponseEntity.status(status).body(err);
        }

        @ExceptionHandler(ExternalServiceException.class)
        public ResponseEntity<StandardError> handleGenericException(
                ExternalServiceException e,
                HttpServletRequest request) {

                HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
                StandardError err = new StandardError(
                                Instant.now(),
                                status.value(),
                                "Erro de serviço externo",
                                e.getMessage(),
                                request.getRequestURI());
                return ResponseEntity.status(status).body(err);
        }

        @ExceptionHandler(IncorrectLoginException.class)
        public ResponseEntity<StandardError> handleIncorrectLoginException(
                        IncorrectLoginException e, HttpServletRequest request) {

                HttpStatus status = HttpStatus.UNAUTHORIZED;
                StandardError err = new StandardError(
                                Instant.now(),
                                status.value(),
                                "Login incorreto",
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
}
