package br.org.apae.profissional_da_saude.application.service.exceptions;

public class FaltaDuplicadaException extends RuntimeException {

    public FaltaDuplicadaException() {
        super("Já existe uma falta registrada");
    }
}
