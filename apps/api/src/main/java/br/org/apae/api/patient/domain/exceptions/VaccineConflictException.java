package br.org.apae.api.patient.domain.exceptions;

public class VaccineConflictException extends RuntimeException {
    public VaccineConflictException() {
        super("Já existe uma vacina cadastrada com este nome.");
    }

    public VaccineConflictException(String name) {
        super("Já existe uma vacina cadastrada com o nome: " + name);
    }
}