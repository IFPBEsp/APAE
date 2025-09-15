package br.org.apae.profissional_da_saude.domain.exception;

public class DadosInvalidosException extends RuntimeException{
    public DadosInvalidosException(String message) {
        super(message);
    }
}
