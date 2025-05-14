package br.org.apae.documentos_medicos.domain.models;

public enum MedcialDocumentType {
    LAUDO("laudo"),
    ENCAMINHAMENTO("encaminhamento"),
    EXAME("exame");

    private final String prefix;

    MedcialDocumentType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }
}
