package br.org.apae.api_crud_pacientes.domain.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Pessoa {

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
  private List<PessoaResponsavel> responsaveis;
  private List<CadastroAnual> cadastrosAnuais;
  private List<Vacina> vacinacoes;
  private List<TipoDeficiencia> deficiencias;
  private List<TipoAtendimento> tiposAtendimentos;
  private Contato contato;

  public Pessoa() {}

  public Pessoa(
      UUID id,
      String nomeCompleto,
      LocalDate dataNascimento,
      String numRegistroNasc,
      String fls,
      String livro,
      String cartorio,
      String cpf,
      String rg,
      LocalDate dataEmissaoRg,
      String orgaoEmissorRg,
      String cns,
      String nis,
      LocalDate dataCadastramento,
      List<PessoaResponsavel> responsaveis,
      List<CadastroAnual> cadastrosAnuais,
      List<Vacina> vacinacoes,
      List<TipoDeficiencia> deficiencias,
      List<TipoAtendimento> tiposAtendimentos,
      Contato contato) {
    this.id = id;
    this.nomeCompleto = nomeCompleto;
    this.dataNascimento = dataNascimento;
    this.numRegistroNasc = numRegistroNasc;
    this.fls = fls;
    this.livro = livro;
    this.cartorio = cartorio;
    this.cpf = cpf;
    this.rg = rg;
    this.dataEmissaoRg = dataEmissaoRg;
    this.orgaoEmissorRg = orgaoEmissorRg;
    this.cns = cns;
    this.nis = nis;
    this.dataCadastramento = dataCadastramento;
    this.responsaveis = responsaveis;
    this.cadastrosAnuais = cadastrosAnuais;
    this.vacinacoes = vacinacoes;
    this.deficiencias = deficiencias;
    this.tiposAtendimentos = tiposAtendimentos;
    this.contato = contato;
  }

  public Pessoa(String nomeCompleto, LocalDate dataNascimento, String cpf) {
    this.nomeCompleto = nomeCompleto;
    this.dataNascimento = dataNascimento;
    this.cpf = cpf;
  }

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

  public List<PessoaResponsavel> getResponsaveis() {
    return responsaveis;
  }

  public void setResponsaveis(List<PessoaResponsavel> responsaveis) {
    this.responsaveis = responsaveis;
  }

  public List<CadastroAnual> getCadastrosAnuais() {
    return cadastrosAnuais;
  }

  public void setCadastrosAnuais(List<CadastroAnual> cadastrosAnuais) {
    this.cadastrosAnuais = cadastrosAnuais;
  }

  public List<Vacina> getVacinacoes() {
    return vacinacoes;
  }

  public void setVacinacoes(List<Vacina> vacinacoes) {
    this.vacinacoes = vacinacoes;
  }

  public List<TipoDeficiencia> getDeficiencias() {
    return deficiencias;
  }

  public void setDeficiencias(List<TipoDeficiencia> deficiencias) {
    this.deficiencias = deficiencias;
  }

  public List<TipoAtendimento> getTiposAtendimentos() {
    return tiposAtendimentos;
  }

  public void setTiposAtendimentos(List<TipoAtendimento> tiposAtendimentos) {
    this.tiposAtendimentos = tiposAtendimentos;
  }

  public Contato getContato() {
    return contato;
  }

  public void setContato(Contato contato) {
    this.contato = contato;
  }
}
