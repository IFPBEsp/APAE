package br.org.apae.api_crud_de_grupos.dto;

import java.time.LocalDate;
import java.util.UUID;

public class PacienteDTO {
    private UUID id;
    private String nome_completo;

    private enum genero {
        MASCULINO, FEMININO, OUTRO
    }

    private String contato;
    private String endereco;
    private FichaMedicaDTO ficha_medica;
    private LocalDate data_nascimento;
    private String cpf;

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

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public FichaMedicaDTO getFicha_medica() {
        return ficha_medica;
    }

    public void setFicha_medica(FichaMedicaDTO ficha_medica) {
        this.ficha_medica = ficha_medica;
    }

    public LocalDate getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(LocalDate data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nome_completo == null) ? 0 : nome_completo.hashCode());
        result = prime * result + ((contato == null) ? 0 : contato.hashCode());
        result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
        result = prime * result + ((ficha_medica == null) ? 0 : ficha_medica.hashCode());
        result = prime * result + ((data_nascimento == null) ? 0 : data_nascimento.hashCode());
        result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
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
        PacienteDTO other = (PacienteDTO) obj;
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
        if (contato == null) {
            if (other.contato != null)
                return false;
        } else if (!contato.equals(other.contato))
            return false;
        if (endereco == null) {
            if (other.endereco != null)
                return false;
        } else if (!endereco.equals(other.endereco))
            return false;
        if (ficha_medica == null) {
            if (other.ficha_medica != null)
                return false;
        } else if (!ficha_medica.equals(other.ficha_medica))
            return false;
        if (data_nascimento == null) {
            if (other.data_nascimento != null)
                return false;
        } else if (!data_nascimento.equals(other.data_nascimento))
            return false;
        if (cpf == null) {
            if (other.cpf != null)
                return false;
        } else if (!cpf.equals(other.cpf))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PacienteDTO [id=" + id + ", nome_completo=" + nome_completo + ", contato=" + contato + ", endereco="
                + endereco + ", ficha_medica=" + ficha_medica + ", data_nascimento=" + data_nascimento + ", cpf=" + cpf
                + "]";
    }

}
