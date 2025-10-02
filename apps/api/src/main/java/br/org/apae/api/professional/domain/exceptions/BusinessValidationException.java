package br.org.apae.api.professional.domain.exceptions;

public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String menssagem){
        super(menssagem);
    }
}