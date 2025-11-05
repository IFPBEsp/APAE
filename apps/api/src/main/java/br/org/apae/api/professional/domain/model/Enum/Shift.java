package br.org.apae.api.professional.domain.model.Enum;

public enum Shift {
    MORNING("manha"),
    AFTERNOON("tarde");

    private final String valor;

    Shift(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
