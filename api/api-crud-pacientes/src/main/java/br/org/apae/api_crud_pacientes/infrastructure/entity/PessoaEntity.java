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
  private LocalDate dataEmissaoRg;

  @Column(name = "orgao_emissor_rg", nullable = false)
  private String orgaoEmissorRg;

  @Column(name = "CNS", nullable = false)
  private String cns;

  @Column(name = "NIS", nullable = false)
  private String nis;

  @Column(name = "data_cadastramento", nullable = false)
  private LocalDate dataCadastramento;

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

  @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ContatoEntity> contatos;

  public PessoaEntity() {}

  public PessoaEntity(
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
      List<PessoaResponsavelEntity> responsaveis,
      List<CadastroAnualEntity> cadastrosAnuais,
      List<VacinaEntity> vacinacoes,
      List<TipoDeficienciaEntity> deficiencias,
      List<TipoAtendimentoEntity> tiposAtendimentos,
      List<ContatoEntity> contatos) {
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
    this.contatos = contatos;
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

  public List<ContatoEntity> getContatos() {
    return contatos;
  }

  public void setContatos(List<ContatoEntity> contatos) {
    this.contatos = contatos;
  }
}
