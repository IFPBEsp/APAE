package br.org.apae.profissional_da_saude.domain.model.enums;

public enum Turno {

    MANHA("manha"),
    TARDE("tarde");

    private final String valor;

    Turno(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}