package br.org.apae.api.documents.domain.enums;

public enum DocumentCategory {
    APAE("APAE"),
    MEDICAL("MEDICO"),
    PERSONAL("PESSOAL"),
    PROFESSIONAL("PROFISSIONAL"),
    SCHOOL("ESCOLAR");

    private String value;

    private DocumentCategory(String value) {
        this.value = value;
    }

    public static DocumentCategory fromValue(String value) {
        for (DocumentCategory c : values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
