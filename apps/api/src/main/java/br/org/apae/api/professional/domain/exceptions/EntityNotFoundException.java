package br.org.apae.api.professional.domain.exceptions;

public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructs an EntityNotFoundException with the specified detail message.
     * @param message the detail message.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}