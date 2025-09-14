package br.org.apae.profissional_da_saude.domain.model.enums;

public enum DiaSemana {

    SEGUNDA("segunda"),
    TERCA("terca"),
    QUARTA("quarta"),
    QUINTA("quinta"),
    SEXTA("sexta"),
    SABADO("sabado"),
    DOMINGO("domingo");

    private final String valor;

    DiaSemana(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
