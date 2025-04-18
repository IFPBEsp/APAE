package br.org.apae.api_crud_pacientes.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    private enum genero {
        MASCULINO,
        FEMININO,
        OUTRO
    };

    @Column()
    private String contato;

    @Column()
    private String endereco;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ficha_medica_id", referencedColumnName = "id")
    private FichaMedica fichaMedica;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, unique = true)
    private String cpf;

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

    public FichaMedica getFichaMedica() {
        return fichaMedica;
    }

    public void setFichaMedica(FichaMedica fichaMedica) {
        this.fichaMedica = fichaMedica;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
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
        result = prime * result + ((nomeCompleto == null) ? 0 : nomeCompleto.hashCode());
        result = prime * result + ((contato == null) ? 0 : contato.hashCode());
        result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
        result = prime * result + ((fichaMedica == null) ? 0 : fichaMedica.hashCode());
        result = prime * result + ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
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
        Paciente other = (Paciente) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nomeCompleto == null) {
            if (other.nomeCompleto != null)
                return false;
        } else if (!nomeCompleto.equals(other.nomeCompleto))
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
        if (fichaMedica == null) {
            if (other.fichaMedica != null)
                return false;
        } else if (!fichaMedica.equals(other.fichaMedica))
            return false;
        if (dataNascimento == null) {
            if (other.dataNascimento != null)
                return false;
        } else if (!dataNascimento.equals(other.dataNascimento))
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
        return "Paciente [id=" + id + ", nomeCompleto=" + nomeCompleto + ", contato=" + contato + ", endereco="
                + endereco + ", fichaMedica=" + fichaMedica + ", dataNascimento=" + dataNascimento + ", cpf=" + cpf
                + "]";
    }

}
