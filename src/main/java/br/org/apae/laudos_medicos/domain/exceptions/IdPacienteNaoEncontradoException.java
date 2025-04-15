package br.org.apae.laudos_medicos.domain.exceptions;

public class IdPacienteNaoEncontradoException extends Exception {
    public IdPacienteNaoEncontradoException() {
        super("O id paciente precisa ser preenchida.");
    }
}
