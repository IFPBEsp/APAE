package br.org.apae.api_crud_pacientes.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome_completo", nullable = false)
    private String nome_completo;

    @Column()
    private List<String> contatos;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate data_nascimento;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }

    public List<String> getContatos() {
        return contatos;
    }

    public void setContatos(List<String> contatos) {
        this.contatos = contatos;
    }

    public LocalDate getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(LocalDate data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nome_completo == null) ? 0 : nome_completo.hashCode());
        result = prime * result + ((contatos == null) ? 0 : contatos.hashCode());
        result = prime * result + ((data_nascimento == null) ? 0 : data_nascimento.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Paciente other = (Paciente) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nome_completo == null) {
            if (other.nome_completo != null)
                return false;
        } else if (!nome_completo.equals(other.nome_completo))
            return false;
        if (contatos == null) {
            if (other.contatos != null)
                return false;
        } else if (!contatos.equals(other.contatos))
            return false;
        if (data_nascimento == null) {
            if (other.data_nascimento != null)
                return false;
        } else if (!data_nascimento.equals(other.data_nascimento))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Paciente [id=" + id + ", nome_completo=" + nome_completo + ", contatos=" + contatos
                + ", data_nascimento=" + data_nascimento + "]";
    }

}
