package br.org.apae.laudos_medicos.infrastructure.clients.exceptions;

public class IdMedicoInvalidoException extends RuntimeException {
    public IdMedicoInvalidoException(String message) {
        super(message);
    }
}
