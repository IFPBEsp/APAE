package br.org.apae.laudos_medicos.infrastructure.clients.exceptions;

public class IdPacienteInvalidoException extends RuntimeException {
    public IdPacienteInvalidoException(String message) {
        super(message);
    }
}
