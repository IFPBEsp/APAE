package br.org.apae.api.profissional.da.saude.exceptions;

public class DadosInvalidosException extends RuntimeException{
    public DadosInvalidosException(String message) {
        super(message);
    }
}