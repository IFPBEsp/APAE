package br.org.apae.documentos_pessoais_digitalizados.domain.model;

public enum PersonalDocumentType {
    CPF("cpf"),
    IDENTIDADE("identidade"),
    COMPROVANTE_RESIDENCIA("comprovante_residencia"),
    CERTIDAO_NASCIMENTO("certidao_nascimento");

    private final String prefixo;

    PersonalDocumentType(String prefixo) {
        this.prefixo = prefixo;
    }
}
