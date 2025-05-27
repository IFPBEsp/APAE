package br.org.apae.api_crud_pacientes.api.dtos.response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PessoaResponse {

  private UUID id;
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

  private ContatoResponse contatoResponse;
  private List<VacinaResponse> vacinasResponses;
  private List<TipoDeficienciaResponse> deficienciasResponses;
  private List<TipoAtendimentoResponse> atendimentosResponses;
  private List<PessoaResponsavelResponse> responsaveisResponses;
  private List<CadastroAnualResponse> cadastrosAnuaisResponses;

  public PessoaResponse() {}

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public void setNomeCompleto(String nomeCompleto) {
    this.nomeCompleto = nomeCompleto;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate datNascimento) {
    this.dataNascimento = datNascimento;
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

  public ContatoResponse getContatoResponse() {
    return contatoResponse;
  }

  public void setContatoResponse(ContatoResponse contatoResponse) {
    this.contatoResponse = contatoResponse;
  }

  public List<VacinaResponse> getVacinasResponses() {
    return vacinasResponses;
  }

  public void setVacinasResponses(List<VacinaResponse> vacinasResponses) {
    this.vacinasResponses = vacinasResponses;
  }

  public List<TipoDeficienciaResponse> getDeficienciasResponses() {
    return deficienciasResponses;
  }

  public void setDeficienciasResponses(List<TipoDeficienciaResponse> deficienciasResponses) {
    this.deficienciasResponses = deficienciasResponses;
  }

  public List<TipoAtendimentoResponse> getAtendimentosResponses() {
    return atendimentosResponses;
  }

  public void setAtendimentosResponses(List<TipoAtendimentoResponse> atendimentosResponses) {
    this.atendimentosResponses = atendimentosResponses;
  }

  public List<PessoaResponsavelResponse> getResponsaveisResponses() {
    return responsaveisResponses;
  }

  public void setResponsaveisResponses(List<PessoaResponsavelResponse> responsaveisResponses) {
    this.responsaveisResponses = responsaveisResponses;
  }

  public List<CadastroAnualResponse> getCadastrosAnuaisResponses() {
    return cadastrosAnuaisResponses;
  }

  public void setCadastrosAnuaisResponses(List<CadastroAnualResponse> cadastrosAnuaisResponses) {
    this.cadastrosAnuaisResponses = cadastrosAnuaisResponses;
  }
}
