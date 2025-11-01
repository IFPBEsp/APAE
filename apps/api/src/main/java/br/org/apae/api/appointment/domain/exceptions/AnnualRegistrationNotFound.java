package br.org.apae.api.appointment.domain.exceptions;

public class AnnualRegistrationNotFound extends RuntimeException {
    private static final String MESSAGE = "Cadastro não encontrado.";

    public AnnualRegistrationNotFound() {
        super(MESSAGE);
    }
}
