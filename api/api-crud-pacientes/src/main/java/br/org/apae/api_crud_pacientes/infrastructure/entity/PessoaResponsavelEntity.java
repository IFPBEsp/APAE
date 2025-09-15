package br.org.apae.api_crud_pacientes.infrastructure.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "pessoa_responsavel")
public class PessoaResponsavelEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "nome", nullable = false)
  private String nome;

  @Column(name = "onde_procurar", nullable = false)
  private String ondeProcurar;

  @Column(name = "vivo", nullable = false)
  private boolean vivo;

  @Column(name = "profissao", nullable = false)
  private String profissao;

  @Column(name = "rg", nullable = false)
  private String rg;

  @Column(name = "cpf", nullable = false)
  private String cpf;

  @Column(name = "emergencia", nullable = false)
  private String emergencia;

  public enum TipoResponsavel {
    MAE,
    PAI,
    RESPONSAVEL_LEGAL,
    OUTRO
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_responsavel")
  private TipoResponsavel tipoResponsavel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pessoa_id")
  private PessoaEntity pessoa;

  public PessoaResponsavelEntity() {}

  public PessoaResponsavelEntity(
      UUID id,
      String nome,
      String ondeProcurar,
      boolean vivo,
      String profissao,
      String rg,
      String cpf,
      String emergencia,
      TipoResponsavel tipoResponsavel,
      PessoaEntity pessoa) {
    this.id = id;
    this.nome = nome;
    this.ondeProcurar = ondeProcurar;
    this.vivo = vivo;
    this.profissao = profissao;
    this.rg = rg;
    this.cpf = cpf;
    this.emergencia = emergencia;
    this.tipoResponsavel = tipoResponsavel;
    this.pessoa = pessoa;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getOndeProcurar() {
    return ondeProcurar;
  }

  public void setOndeProcurar(String ondeProcurar) {
    this.ondeProcurar = ondeProcurar;
  }

  public boolean isVivo() {
    return vivo;
  }

  public void setVivo(boolean vivo) {
    this.vivo = vivo;
  }

  public String getProfissao() {
    return profissao;
  }

  public void setProfissao(String profissao) {
    this.profissao = profissao;
  }

  public String getRg() {
    return rg;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getEmergencia() {
    return emergencia;
  }

  public void setEmergencia(String emergencia) {
    this.emergencia = emergencia;
  }

  public TipoResponsavel getTipoResponsavel() {
    return tipoResponsavel;
  }

  public void setTipoResponsavel(TipoResponsavel tipoResponsavel) {
    this.tipoResponsavel = tipoResponsavel;
  }

  public PessoaEntity getPessoa() {
    return pessoa;
  }

  public void setPessoa(PessoaEntity pessoa) {
    this.pessoa = pessoa;
  }
}
