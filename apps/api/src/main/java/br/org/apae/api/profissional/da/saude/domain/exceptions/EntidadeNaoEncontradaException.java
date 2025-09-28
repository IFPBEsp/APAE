package br.org.apae.api.profissional.da.saude.domain.exceptions;

public class EntidadeNaoEncontradaException extends RuntimeException{
    public EntidadeNaoEncontradaException(String message) {
        super(message);
    }
}