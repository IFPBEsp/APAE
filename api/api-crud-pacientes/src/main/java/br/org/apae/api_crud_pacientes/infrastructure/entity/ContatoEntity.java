package br.org.apae.api_crud_pacientes.infrastructure.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "contato")
public class ContatoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "endereco_ativo")
    private String enderecoAtivo;

    @Column(name = "comprovante_residencia")
    private String comprovanteResidencia;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "cep")
    private String cep;

    @Column(name = "naturalidade")
    private String naturalidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id")
    private PessoaEntity pessoa;

    public ContatoEntity() {}

    public ContatoEntity(UUID id, String enderecoAtivo, String comprovanteResidencia, String endereco, String bairro, String cidade, String estado, String cep, String naturalidade, PessoaEntity pessoa) {
        this.id = id;
        this.enderecoAtivo = enderecoAtivo;
        this.comprovanteResidencia = comprovanteResidencia;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.naturalidade = naturalidade;
        this.pessoa = pessoa;
    }

    public ContatoEntity(String endereco, String cidade, String estado, PessoaEntity pessoa) {
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.pessoa = pessoa;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEnderecoAtivo() { return enderecoAtivo; }
    public void setEnderecoAtivo(String enderecoAtivo) { this.enderecoAtivo = enderecoAtivo; }

    public String getComprovanteResidencia() { return comprovanteResidencia; }
    public void setComprovanteResidencia(String comprovanteResidencia) { this.comprovanteResidencia = comprovanteResidencia; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getNaturalidade() { return naturalidade; }
    public void setNaturalidade(String naturalidade) { this.naturalidade = naturalidade; }

    public PessoaEntity getPessoa() { return pessoa; }
    public void setPessoa(PessoaEntity pessoa) { this.pessoa = pessoa; }
}