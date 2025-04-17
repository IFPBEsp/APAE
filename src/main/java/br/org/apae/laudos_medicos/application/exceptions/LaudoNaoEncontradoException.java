package br.org.apae.laudos_medicos.application.exceptions;

public class LaudoNaoEncontradoException extends RuntimeException{
    public LaudoNaoEncontradoException(String message) {
        super(message);
    }
}
