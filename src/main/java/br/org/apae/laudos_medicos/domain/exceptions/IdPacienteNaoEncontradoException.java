package br.org.apae.laudos_medicos.domain.exceptions;

public class IdPacienteNaoEncontradoException extends RuntimeException {
    public IdPacienteNaoEncontradoException() {
        super("O id paciente precisa ser preenchida.");
    }
}
