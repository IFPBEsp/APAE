package br.org.apae.api_crud_pacientes.domain.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "vacinas")
public class Vacina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "dataAplicacao", nullable = false)
    private LocalDate dataAplicacao;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

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
        Vacina vacina = (Vacina) o;
        return Objects.equals(id, vacina.id) && Objects.equals(nome, vacina.nome) && Objects.equals(dataAplicacao, vacina.dataAplicacao) && Objects.equals(pessoa, vacina.pessoa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, dataAplicacao, pessoa);
    }
}
