package br.org.apae.documentos_pessoais_digitalizados.domain.models;

public enum PersonalDocumentType {
    CPF("cpf"),
    IDENTIDADE("identidade"),
    COMPROVANTE_RESIDENCIA("comprovante_residencia"),
    CERTIDAO_NASCIMENTO("certidao_nascimento"),
    RG("rg");

    private final String prefix;

    PersonalDocumentType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
