package br.org.apae.profissional_da_saude.application.service.exceptions;

public class PacienteNaoEncontradoException extends RuntimeException {

    public PacienteNaoEncontradoException() {
        super("Já existe uma falta registrada");
    }
}
