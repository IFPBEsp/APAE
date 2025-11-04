package br.org.apae.api.professional.domain.model.Enum;

public enum Day {

    MONDAY("segunda"),
    TUESDAY("terca"),
    WEDNESDAY("quarta"),
    THURSDAY("quinta"),
    FRIDAY("sexta"),
    SARTUDAY("sabado"),
    SUNDAY("domingo");

    private final String valor;

    Day(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}

