package br.org.apae.api.profissional.da.saude.domain.exceptions;

public class DadosInvalidosException extends RuntimeException{
    public DadosInvalidosException(String message) {
        super(message);
    }
}