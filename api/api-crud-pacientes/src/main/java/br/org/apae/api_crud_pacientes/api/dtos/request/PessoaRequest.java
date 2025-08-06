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

  private ContatoRequest contatoRequest;
  private List<VacinaRequest> vacinacoesRequests;
  private List<TipoDeficienciaRequest> deficienciasRequests;
  private List<TipoAtendimentoRequest> atendimentosRequests;
  private List<PessoaResponsavelRequest> responsaveisRequests;
  private List<CadastroAnualRequest> cadastrosAnuaisRequests;

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public void setNomeCompleto(String nomeCompleto) {
    this.nomeCompleto = nomeCompleto;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getNumRegistroNasc() {
    return numRegistroNasc;
  }

  public void setNumRegistroNasc(String numRegistroNasc) {
    this.numRegistroNasc = numRegistroNasc;
  }

  public String getFls() {
    return fls;
  }

  public void setFls(String fls) {
    this.fls = fls;
  }

  public String getLivro() {
    return livro;
  }

  public void setLivro(String livro) {
    this.livro = livro;
  }

  public String getCartorio() {
    return cartorio;
  }

  public void setCartorio(String cartorio) {
    this.cartorio = cartorio;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getRg() {
    return rg;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public LocalDate getDataEmissaoRg() {
    return dataEmissaoRg;
  }

  public void setDataEmissaoRg(LocalDate dataEmissaoRg) {
    this.dataEmissaoRg = dataEmissaoRg;
  }

  public String getOrgaoEmissorRg() {
    return orgaoEmissorRg;
  }

  public void setOrgaoEmissorRg(String orgaoEmissorRg) {
    this.orgaoEmissorRg = orgaoEmissorRg;
  }

  public String getCns() {
    return cns;
  }

  public void setCns(String cns) {
    this.cns = cns;
  }

  public String getNis() {
    return nis;
  }

  public void setNis(String nis) {
    this.nis = nis;
  }

  public LocalDate getDataCadastramento() {
    return dataCadastramento;
  }

  public void setDataCadastramento(LocalDate dataCadastramento) {
    this.dataCadastramento = dataCadastramento;
  }

  public ContatoRequest getContatoRequest() {
    return contatoRequest;
  }

  public void setContatoRequest(ContatoRequest contatoRequest) {
    this.contatoRequest = contatoRequest;
  }

  public List<VacinaRequest> getVacinacoesRequests() {
    return vacinacoesRequests;
  }

  public void setVacinacoesRequests(List<VacinaRequest> vacinacoesRequests) {
    this.vacinacoesRequests = vacinacoesRequests;
  }

  public List<TipoDeficienciaRequest> getDeficienciasRequests() {
    return deficienciasRequests;
  }

  public void setDeficienciasRequests(List<TipoDeficienciaRequest> deficienciasRequests) {
    this.deficienciasRequests = deficienciasRequests;
  }

  public List<TipoAtendimentoRequest> getAtendimentosRequests() {
    return atendimentosRequests;
  }

  public void setAtendimentosRequests(List<TipoAtendimentoRequest> atendimentosRequests) {
    this.atendimentosRequests = atendimentosRequests;
  }

  public List<PessoaResponsavelRequest> getResponsaveisRequests() {
    return responsaveisRequests;
  }

  public void setResponsaveisRequests(List<PessoaResponsavelRequest> responsaveisRequests) {
    this.responsaveisRequests = responsaveisRequests;
  }

  public List<CadastroAnualRequest> getCadastrosAnuaisRequests() {
    return cadastrosAnuaisRequests;
  }

  public void setCadastrosAnuaisRequests(List<CadastroAnualRequest> cadastrosAnuaisRequests) {
    this.cadastrosAnuaisRequests = cadastrosAnuaisRequests;
  }
}
