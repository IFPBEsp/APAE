package br.org.apae.profissional_da_saude.domain.model;

import br.org.apae.profissional_da_saude.domain.exception.DadosInvalidosException;
import br.org.apae.profissional_da_saude.domain.exception.ValidacaoNegocioException;

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
    if (areaDaSaude == null || areaDaSaude.trim().isEmpty()) {
      throw new ValidacaoNegocioException("Área da saúde é obrigatória.");
    }
    if (telefone == null || telefone.trim().isEmpty()) {
      throw new DadosInvalidosException("Telefone é obrigatório.");
    }
    if (docProfissional == null || docProfissional.trim().isEmpty()) {
      throw new DadosInvalidosException("Documento profissional é obrigatório.");
    }
    if (email == null || email.trim().isEmpty() || !EMAIL_REGEX.matcher(email).matches()) {
      throw new DadosInvalidosException("E-mail inválido.");
    }
    if (nome == null || nome.trim().isEmpty()) {
      throw new DadosInvalidosException("Nome é obrigatório.");
    }
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
