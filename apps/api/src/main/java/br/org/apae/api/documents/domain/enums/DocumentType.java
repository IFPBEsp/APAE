package br.org.apae.api.documents.domain.enums;

public enum DocumentType {
    BIRTH_CERTIFICATE("CERTIDAO_DE_NASCIMENTO"),
    CPF("CPF"),
    EXAMINATION("EXAME"),
    MEDICAL_REPORT("LAUDO"),
    OTHER("OUTRO"),
    PHOTO("FOTO"),
    PROOF_OF_ADDRESS("COMPROVANTE_DE_RESIDENCIA"),
    PROGRESS_REPORT("RELATORIO"),
    REFERRAL("ENCAMINHAMENTO"),
    RG("RG"),
    VOLUNTEER_AGREEMENT("TERMO_DE_VOLUNTARIADO"),
    CURRICULUM("CURRICULO"),
    ATTACHMENTANY("ANEXO_QUALQUER");


    private String value;

    private DocumentType(String value) {
        this.value = value;
    }

    public static DocumentType fromValue(String value) {
        for (DocumentType c : values()) {
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
