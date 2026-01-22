package br.org.apae.api.professional.domain.model.enums;

public enum Day {
    SEGUNDA("segunda"),
    TERCA("terca"),
    QUARTA("quarta"),
    QUINTA("quinta"),
    SEXTA("sexta"),
    SABADO("sabado"),
    DOMINGO("domingo");

    private final String valor;

    Day(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}