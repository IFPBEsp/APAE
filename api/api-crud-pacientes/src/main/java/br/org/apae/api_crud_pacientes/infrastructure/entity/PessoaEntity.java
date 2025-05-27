package br.org.apae.api_crud_pacientes.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pessoas")
public class PessoaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "nome_completo", nullable = false)
  private String nomeCompleto;

  @Column(name = "data_nascimento", nullable = false)
  private LocalDate dataNascimento;

  @Column(name = "num_registro_nasc", nullable = false)
  private String numRegistroNasc;

  @Column(name = "FLS", nullable = false)
  private String fls;

  @Column(name = "livro", nullable = false)
  private String livro;

  @Column(name = "cartorio", nullable = false)
  private String cartorio;

  @Column(name = "CPF", nullable = false)
  private String cpf;

  @Column(name = "RG", nullable = false)
  private String rg;

  @Column(name = "data_emissão_rg", nullable = false)
  private LocalDate data_emissao_rg;

  @Column(name = "orgao_emissor_rg", nullable = false)
  private String orgao_emissor_rg;

  @Column(name = "CNS", nullable = false)
  private String cns;

  @Column(name = "NIS", nullable = false)
  private String nis;

  @Column(name = "data_cadastramento", nullable = false)
  private LocalDate data_cadastramento;

  @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
  private List<PessoaResponsavelEntity> responsaveis;

  @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
  private List<CadastroAnualEntity> cadastrosAnuais;

  @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
  private List<VacinaEntity> vacinacoes;

  @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
  private List<TipoDeficienciaEntity> deficiencias;

  @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
  private List<TipoAtendimentoEntity> tiposAtendimentos;

  @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL)
  private ContatoEntity contato;

  public PessoaEntity() {}

  public PessoaEntity(
      UUID id,
      String nome_completo,
      LocalDate data_nascimento,
      String num_registro_nasc,
      String fls,
      String livro,
      String cartorio,
      String cpf,
      String rg,
      LocalDate data_emissao_rg,
      String orgao_emissor_rg,
      String cns,
      String nis,
      LocalDate data_cadastramento,
      List<PessoaResponsavelEntity> responsaveis,
      List<CadastroAnualEntity> cadastrosAnuais,
      List<VacinaEntity> vacinacoes,
      List<TipoDeficienciaEntity> deficiencias,
      List<TipoAtendimentoEntity> tiposAtendimentos,
      ContatoEntity contato) {
    this.id = id;
    this.nomeCompleto = nome_completo;
    this.dataNascimento = data_nascimento;
    this.numRegistroNasc = num_registro_nasc;
    this.fls = fls;
    this.livro = livro;
    this.cartorio = cartorio;
    this.cpf = cpf;
    this.rg = rg;
    this.data_emissao_rg = data_emissao_rg;
    this.orgao_emissor_rg = orgao_emissor_rg;
    this.cns = cns;
    this.nis = nis;
    this.data_cadastramento = data_cadastramento;
    this.responsaveis = responsaveis;
    this.cadastrosAnuais = cadastrosAnuais;
    this.vacinacoes = vacinacoes;
    this.deficiencias = deficiencias;
    this.tiposAtendimentos = tiposAtendimentos;
    this.contato = contato;
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
    return data_emissao_rg;
  }

  public void setDataEmissaoRg(LocalDate data_emissao_rg) {
    this.data_emissao_rg = data_emissao_rg;
  }

  public String getOrgaoEmissorRg() {
    return orgao_emissor_rg;
  }

  public void setOrgaoEmissorRg(String orgao_emissor_rg) {
    this.orgao_emissor_rg = orgao_emissor_rg;
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
    return data_cadastramento;
  }

  public void setDataCadastramento(LocalDate data_cadastramento) {
    this.data_cadastramento = data_cadastramento;
  }

  public List<PessoaResponsavelEntity> getResponsaveis() {
    return responsaveis;
  }

  public void setResponsaveis(List<PessoaResponsavelEntity> responsaveis) {
    this.responsaveis = responsaveis;
  }

  public List<CadastroAnualEntity> getCadastrosAnuais() {
    return cadastrosAnuais;
  }

  public void setCadastrosAnuais(List<CadastroAnualEntity> cadastrosAnuais) {
    this.cadastrosAnuais = cadastrosAnuais;
  }

  public List<VacinaEntity> getVacinacoes() {
    return vacinacoes;
  }

  public void setVacinacoes(List<VacinaEntity> vacinacoes) {
    this.vacinacoes = vacinacoes;
  }

  public List<TipoDeficienciaEntity> getDeficiencias() {
    return deficiencias;
  }

  public void setDeficiencias(List<TipoDeficienciaEntity> deficiencias) {
    this.deficiencias = deficiencias;
  }

  public List<TipoAtendimentoEntity> getTiposAtendimentos() {
    return tiposAtendimentos;
  }

  public void setTiposAtendimentos(List<TipoAtendimentoEntity> tiposAtendimentos) {
    this.tiposAtendimentos = tiposAtendimentos;
  }

  public ContatoEntity getContato() {
    return contato;
  }

  public void setContato(ContatoEntity contato) {
    this.contato = contato;
  }
}
