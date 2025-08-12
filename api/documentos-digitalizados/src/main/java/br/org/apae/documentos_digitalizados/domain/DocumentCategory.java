package br.org.apae.documentos_digitalizados.domain;

public enum DocumentCategory {
  MEDICO("documento-medico"),
  PESSOAL("documento-pessoal"),
  ESCOLAR("documento-escolar");

  private String category;

  DocumentCategory(String category) {
    this.category = category;
  }

  public String getCategory() {
    return this.category;
  }
}
