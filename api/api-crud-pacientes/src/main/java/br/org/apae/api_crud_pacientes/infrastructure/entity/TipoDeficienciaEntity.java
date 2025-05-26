package br.org.apae.api_crud_pacientes.infrastructure.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tipo_deficiencia")
public class TipoDeficienciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id")
    private PessoaEntity pessoa;

    public TipoDeficienciaEntity() {}

    public TipoDeficienciaEntity(UUID id, String descricao, PessoaEntity pessoa) {
        this.id = id;
        this.descricao = descricao;
        this.pessoa = pessoa;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public PessoaEntity getPessoa() { return pessoa; }
    public void setPessoa(PessoaEntity pessoa) { this.pessoa = pessoa; }
}
