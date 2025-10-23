package br.org.apae.api.documents.domain.enums;

public enum DocumentCategory {
    APAE("APAE"),
    MEDICAL("MEDICO"),
    PERSONAL("PESSOAL"),
    SCHOOL("ESCOLAR");

    private String value;

    private DocumentCategory(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
