package br.org.apae.api_crud_pacientes.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "vacinas")
public class VacinaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "dataAplicacao", nullable = false)
    private LocalDate dataAplicacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id")
    private PessoaEntity pessoa;

    public VacinaEntity() {}

    public VacinaEntity(UUID id, String nome, LocalDate dataAplicacao, PessoaEntity pessoa) {
        this.id = id;
        this.nome = nome;
        this.dataAplicacao = dataAplicacao;
        this.pessoa = pessoa;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataAplicacao() { return dataAplicacao; }
    public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }

    public PessoaEntity getPessoa() { return pessoa; }
    public void setPessoa(PessoaEntity pessoa) { this.pessoa = pessoa; }

    @Override
    public String toString() {
        return "Vacina{" +
                "nome='" + nome + '\'' +
                ", dataAplicacao=" + dataAplicacao +
                ", pessoa=" + pessoa +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VacinaEntity vacina = (VacinaEntity) o;
        return Objects.equals(id, vacina.id) && Objects.equals(nome, vacina.nome) && Objects.equals(dataAplicacao, vacina.dataAplicacao) && Objects.equals(pessoa, vacina.pessoa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, dataAplicacao, pessoa);
    }
}
