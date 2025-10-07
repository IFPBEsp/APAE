package br.org.apae.api.professional.exceptions;

public class InvalidDataException extends RuntimeException {

    /**
     * Constructs an InvalidDataException with the specified detail message.
     * @param message the detail message.
     */
    public InvalidDataException(String message) {
        super(message);
    }
}