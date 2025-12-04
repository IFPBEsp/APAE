package br.org.apae.api.professional.domain.model.Enum;

public enum Day {
    MONDAY("segunda"),
    TUESDAY("terca"),
    WEDNESDAY("quarta"),
    THURSDAY("quinta"),
    FRIDAY("sexta"),
    SATURDAY("sabado"), // Corrigido o typo SARTUDAY
    SUNDAY("domingo");

    private final String valor;

    Day(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}