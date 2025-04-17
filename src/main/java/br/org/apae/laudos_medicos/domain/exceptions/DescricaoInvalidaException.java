package br.org.apae.laudos_medicos.domain.exceptions;

public class DescricaoInvalidaException extends RuntimeException {
    public DescricaoInvalidaException() {
        super("Descrição inválida. A descrição não pode estar vazia ou em branco.");
    }
}
