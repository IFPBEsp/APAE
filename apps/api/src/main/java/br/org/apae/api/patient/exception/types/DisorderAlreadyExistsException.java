package br.org.apae.api.patient.exception.types;

public class DisorderAlreadyExistsException extends RuntimeException {

    public DisorderAlreadyExistsException(String name) {
        super("Já existe um transtorno com o nome '" + name + "'.");
    }
}