package br.org.apae.api_crud_pacientes.domain.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contato")
public class Contato {
    
    @Column(name = "endereco_ativo", nullable = false)
    private String endereco_ativo;
    
    @Column(name = "comprovante_residencia", nullable = false)
    private String comprovante_residencia;
    
    @Column(name = "endereco", nullable = false)
    private String endereco;
    
    @Column(name = "bairro", nullable = false)
    private String bairro;
    
    @Column(name = "cidade", nullable = false)
    private String cidade;
    
    @Column(name = "estado", nullable = false)
    private String estado;
    
    @Column(name = "cep", nullable = false)
    private String cep;
    
    @Column(name = "naturalidade", nullable = false)
    private String naturalidade;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    public String getEndereco_ativo() {
        return endereco_ativo;
    }

    public void setEndereco_ativo(String endereco_ativo) {
        this.endereco_ativo = endereco_ativo;
    }

    public String getComprovante_residencia() {
        return comprovante_residencia;
    }

    public void setComprovante_residencia(String comprovante_residencia) {
        this.comprovante_residencia = comprovante_residencia;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endereco_ativo == null) ? 0 : endereco_ativo.hashCode());
        result = prime * result + ((comprovante_residencia == null) ? 0 : comprovante_residencia.hashCode());
        result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
        result = prime * result + ((bairro == null) ? 0 : bairro.hashCode());
        result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
        result = prime * result + ((estado == null) ? 0 : estado.hashCode());
        result = prime * result + ((cep == null) ? 0 : cep.hashCode());
        result = prime * result + ((naturalidade == null) ? 0 : naturalidade.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Contato other = (Contato) obj;
        if (endereco_ativo == null) {
            if (other.endereco_ativo != null)
                return false;
        } else if (!endereco_ativo.equals(other.endereco_ativo))
            return false;
        if (comprovante_residencia == null) {
            if (other.comprovante_residencia != null)
                return false;
        } else if (!comprovante_residencia.equals(other.comprovante_residencia))
            return false;
        if (endereco == null) {
            if (other.endereco != null)
                return false;
        } else if (!endereco.equals(other.endereco))
            return false;
        if (bairro == null) {
            if (other.bairro != null)
                return false;
        } else if (!bairro.equals(other.bairro))
            return false;
        if (cidade == null) {
            if (other.cidade != null)
                return false;
        } else if (!cidade.equals(other.cidade))
            return false;
        if (estado == null) {
            if (other.estado != null)
                return false;
        } else if (!estado.equals(other.estado))
            return false;
        if (cep == null) {
            if (other.cep != null)
                return false;
        } else if (!cep.equals(other.cep))
            return false;
        if (naturalidade == null) {
            if (other.naturalidade != null)
                return false;
        } else if (!naturalidade.equals(other.naturalidade))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Contato [endereco_ativo=" + endereco_ativo + ", comprovante_residencia=" + comprovante_residencia
                + ", endereco=" + endereco + ", bairro=" + bairro + ", cidade=" + cidade + ", estado=" + estado
                + ", cep=" + cep + ", naturalidade=" + naturalidade + ", id=" + id + "]";
    }

}