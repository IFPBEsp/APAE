package br.org.apae.api.documents.domain.enums;

public enum DocumentType {
    REPORT("LAUDO"),
    CPF("CPF");

    private String value;

    private DocumentType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
