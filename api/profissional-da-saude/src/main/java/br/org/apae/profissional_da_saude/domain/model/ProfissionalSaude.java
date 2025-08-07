package br.org.apae.profissional_da_saude.domain.model;

import java.util.UUID;
import java.util.regex.Pattern;

public class ProfissionalSaude {
  private UUID id;
  private final String areaDaSaude;
  private final String telefone;
  private final String docProfissional;
  private final String email;
  private final String nome;


  private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

  public ProfissionalSaude(UUID id, String areaDaSaude, String telefone, String docProfissional, String email,
      String nome) {
    validar(areaDaSaude, telefone, docProfissional, email, nome);
    this.id = id;
    this.areaDaSaude = areaDaSaude;
    this.telefone = telefone;
    this.docProfissional = docProfissional;
    this.email = email;
    this.nome = nome;
  }

  public ProfissionalSaude(String areaDaSaude, String telefone, String docProfissional, String email,
      String nome) {
    validar(areaDaSaude, telefone, docProfissional, email, nome);
    this.areaDaSaude = areaDaSaude;
    this.telefone = telefone;
    this.docProfissional = docProfissional;
    this.email = email;
    this.nome = nome;
  }

  private void validar(String areaDaSaude, String telefone, String docProfissional, String email, String nome) {
    if (isNullOrEmpty(areaDaSaude)) throw new IllegalArgumentException("Área da saúde é obrigatória.");
    if (isNullOrEmpty(telefone)) throw new IllegalArgumentException("Telefone é obrigatório.");
    if (isNullOrEmpty(docProfissional)) throw new IllegalArgumentException("Documento profissional é obrigatório.");
    if (isNullOrEmpty(email) || !EMAIL_REGEX.matcher(email).matches()) {
      throw new IllegalArgumentException("E-mail inválido.");
    }
    if (isNullOrEmpty(nome)) throw new IllegalArgumentException("Nome é obrigatório.");
  }

  private boolean isNullOrEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }

  public UUID getId() {
    return id;
  }

  public String getAreaDaSaude() {
    return areaDaSaude;
  }

  public String getTelefone() {
    return telefone;
  }

  public String getDocProfissional() {
    return docProfissional;
  }

  public String getEmail() {
    return email;
  }

  public String getNome() {
    return nome;
  }
}
