package br.org.apae.laudos_medicos.domain.exceptions;

public class IdMedicoNaoEncontradoException extends Exception {
    public IdMedicoNaoEncontradoException() {
        super("O id médico precisa ser preenchido.");
    }
}
