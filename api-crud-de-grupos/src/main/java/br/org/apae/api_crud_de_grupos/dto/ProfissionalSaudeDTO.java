package br.org.apae.api_crud_de_grupos.dto;

import java.util.UUID;

public class ProfissionalSaudeDTO {
    private UUID id;
    private String nome;
    private String especialidade;
    private String contato;

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

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((especialidade == null) ? 0 : especialidade.hashCode());
        result = prime * result + ((contato == null) ? 0 : contato.hashCode());
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
        ProfissionalSaudeDTO other = (ProfissionalSaudeDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (especialidade == null) {
            if (other.especialidade != null)
                return false;
        } else if (!especialidade.equals(other.especialidade))
            return false;
        if (contato == null) {
            if (other.contato != null)
                return false;
        } else if (!contato.equals(other.contato))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ProfissionalSaudeDTO [id=" + id + ", nome=" + nome + ", especialidade=" + especialidade + ", contato="
                + contato + "]";
    }

}
