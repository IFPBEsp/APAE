package br.org.apae.laudos_medicos.domain.exceptions;

public class IdMedicoNaoEncontradoException extends RuntimeException {
    public IdMedicoNaoEncontradoException() {
        super("O id médico precisa ser preenchido.");
    }
}
