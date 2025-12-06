package br.org.apae.api.professional.domain.model.enums;

public enum Shift {
    MANHA("manha"),
    TARDE("tarde");

    private final String valor;

    Shift(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}