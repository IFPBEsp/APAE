package br.org.apae.documentos_digitalizados.domain;

public enum DocumentType {
  // -- DOCUMENTOS PESSOAIS --
  CPF(DocumentCategory.PESSOAL, "CPF"),
  RG(DocumentCategory.PESSOAL, "RG"),
  IDENTIDADE(DocumentCategory.PESSOAL, "IDENTIDADE"),
  COMPROVANTE_RESIDENCIA(DocumentCategory.PESSOAL, "COMPROVANTE_RESIDENCIA"),
  CERTIDAO_NASCIMENTO(DocumentCategory.PESSOAL, "CERTIDAO_NASCIMENTO"),

  // -- DOCUMENTOS MÉDICOS --
  LAUDO(DocumentCategory.MEDICO, "LAUDO"),
  EXAME(DocumentCategory.MEDICO, "EXAME"),
  ENCAMINHAMENTO(DocumentCategory.MEDICO, "ENCAMINHAMENTO"),

  // -- DOCUMENTOS ESCOLARES --
  HISTORICO_ESCOLAR(DocumentCategory.ESCOLAR, "HISTORICO_ESCOLAR"),;

  private final DocumentCategory category;
  private final String type;

  DocumentType(DocumentCategory category, String type) {
    this.category = category;
    this.type = type;
  }

  public DocumentCategory getCategory() {
    return this.category;
  }
  
  public String getType() {
    return this.type;
  }
}
