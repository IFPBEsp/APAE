package br.org.apae.api_crud_pacientes.api.dtos.request;

import java.time.LocalDate;
import java.util.List;

public class PessoaRequest {
  private String nomeCompleto;
  private LocalDate dataNascimento;
  private String numRegistroNasc;
  private String fls;
  private String livro;
  private String cartorio;
  private String cpf;
  private String rg;
  private LocalDate dataEmissaoRg;
  private String orgaoEmissorRg;
  private String cns;
  private String nis;
  private LocalDate dataCadastramento;

  private List<ContatoRequest> contatoRequest;
  private List<VacinaRequest> vacinacoesRequests;
  private List<TipoDeficienciaRequest> deficienciasRequests;
  private List<TipoAtendimentoRequest> atendimentosRequests;
  private List<PessoaResponsavelRequest> responsaveisRequests;
  private List<CadastroAnualRequest> cadastrosAnuaisRequests;

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public String getNumRegistroNasc() {
    return numRegistroNasc;
  }

  public String getFls() {
    return fls;
  }

  public String getLivro() {
    return livro;
  }

  public String getCartorio() {
    return cartorio;
  }

  public String getCpf() {
    return cpf;
  }

  public String getRg() {
    return rg;
  }

  public LocalDate getDataEmissaoRg() {
    return dataEmissaoRg;
  }

  public String getOrgaoEmissorRg() {
    return orgaoEmissorRg;
  }

  public String getCns() {
    return cns;
  }

  public String getNis() {
    return nis;
  }

  public LocalDate getDataCadastramento() {
    return dataCadastramento;
  }

  public List<ContatoRequest> getContatoRequest() {
    return contatoRequest;
  }

  public List<VacinaRequest> getVacinacoesRequests() {
    return vacinacoesRequests;
  }

  public List<TipoDeficienciaRequest> getDeficienciasRequests() {
    return deficienciasRequests;
  }

  public List<TipoAtendimentoRequest> getAtendimentosRequests() {
    return atendimentosRequests;
  }

  public List<PessoaResponsavelRequest> getResponsaveisRequests() {
    return responsaveisRequests;
  }

  public List<CadastroAnualRequest> getCadastrosAnuaisRequests() {
    return cadastrosAnuaisRequests;
  }

}
