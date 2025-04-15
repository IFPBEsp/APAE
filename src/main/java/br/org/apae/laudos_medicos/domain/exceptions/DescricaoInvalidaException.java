package br.org.apae.laudos_medicos.domain.exceptions;

public class DescricaoInvalidaException extends Exception {
    public DescricaoInvalidaException() {
        super("A descrição do laudo precisa ser preenchida.");
    }
}
