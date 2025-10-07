package br.org.apae.api.professional.exceptions;

public class BusinessValidationException extends RuntimeException {

    /**
     * Constructs a BusinessValidationException with the specified detail message.
     * @param message the detail message.
     */
    public BusinessValidationException(String message){
        super(message);
    }
}