package br.org.apae.api.professional.application.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.org.apae.api.professional.domain.exceptions.AvailabilityConflictException;
import br.org.apae.api.professional.domain.exceptions.AvailabilityNotFoundException;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class AvailabilityExceptionHandler {
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AvailabilityNotFoundException.class)
    public void handleAvailabilityNotFound() {}

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HealthProfessionalNotFoundException.class)
    public void handleProfessionalNotFound() {}

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AvailabilityConflictException.class)
    public void handleAvailabilityConflict() {}
}
