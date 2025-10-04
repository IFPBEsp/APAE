package br.org.apae.api.documents.domain.enums;

public enum DocumentCategory {
    MEDICAL("MEDICO"),
    PERSONAL("PESSOAL");

    private String value;

    private DocumentCategory(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
