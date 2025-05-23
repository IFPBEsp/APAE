package br.org.apae.api_crud_pacientes.domain.model;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "pessoa_responsavel")
public class PessoaResponsavel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "onde_procurar", nullable = false)
    private String onde_procurar;

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

    public enum tipo_responsavel {
        MAE,
        PAI,
        RESPONSAVEL_LEGAL,
        OUTRO
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_responsavel")
    private tipo_responsavel tipoResponsavel;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    public String getOnde_procurar() {
        return onde_procurar;
    }

    public void setOnde_procurar(String onde_procurar) {
        this.onde_procurar = onde_procurar;
    }

    public tipo_responsavel getTipoResponsavel() {
        return tipoResponsavel;
    }

    public void setTipoResponsavel(tipo_responsavel tipoResponsavel) {
        this.tipoResponsavel = tipoResponsavel;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOnde_Procurar() {
        return onde_procurar;
    }

    public void setOnde_Procurar(String onde_procurar) {
        this.onde_procurar = onde_procurar;
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

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((onde_procurar == null) ? 0 : onde_procurar.hashCode());
        result = prime * result + (vivo ? 1231 : 1237);
        result = prime * result + ((profissao == null) ? 0 : profissao.hashCode());
        result = prime * result + ((rg == null) ? 0 : rg.hashCode());
        result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
        result = prime * result + ((emergencia == null) ? 0 : emergencia.hashCode());
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
        PessoaResponsavel other = (PessoaResponsavel) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (onde_procurar == null) {
            if (other.onde_procurar != null)
                return false;
        } else if (!onde_procurar.equals(other.onde_procurar))
            return false;
        if (vivo != other.vivo)
            return false;
        if (profissao == null) {
            if (other.profissao != null)
                return false;
        } else if (!profissao.equals(other.profissao))
            return false;
        if (rg == null) {
            if (other.rg != null)
                return false;
        } else if (!rg.equals(other.rg))
            return false;
        if (cpf == null) {
            if (other.cpf != null)
                return false;
        } else if (!cpf.equals(other.cpf))
            return false;
        if (emergencia == null) {
            if (other.emergencia != null)
                return false;
        } else if (!emergencia.equals(other.emergencia))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Pessoa_Responsavel [id=" + id + ", onde_procurar=" + onde_procurar + ", vivo=" + vivo + ", profissao="
                + profissao + ", rg=" + rg + ", cpf=" + cpf + ", emergencia=" + emergencia + "]";
    }

}
