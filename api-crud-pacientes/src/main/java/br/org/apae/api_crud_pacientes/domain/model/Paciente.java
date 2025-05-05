package br.org.apae.api_crud_pacientes.domain.model;

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

    //Dados pessoais
    @Column(name = "nome_completo", nullable = false)
    private String nome_completo;

    @Column(name = "idade", nullable = false)
    private int idade;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate data_nascimento;

    @Column(name = "num_registro_nasc", nullable = false)
    private String num_registro_nasc;

    @Column(name = "FLS", nullable = false)
    private String fls;

    @Column(name = "LIV", nullable = false)
    private String liv;

    @Column(name = "cartorio", nullable = false)
    private String cartorio;

    @Column(name = "CPF", nullable = false)
    private String cpf;

    @Column(name = "RG", nullable = false)
    private String rg;

    @Column(name = "data_emissão_rg", nullable = false)
    private LocalDate data_emissao_rg;

    @Column(name = "orgao_emissor_rg", nullable = false)
    private String orgao_emissor_rg;

    @Column(name = "naturalidade")
    private String naturalidade;

    @Column(name = "CNS", nullable = false)
    private String cns;

    @Column(name = "NIS", nullable = false)
    private String nis;

    @Column(name = "contato", nullable = false)
    private String contato;

    @Column(name = "possui_bcp", nullable = false)
    private boolean possui_bcp;

    @Column(name = "renda_familiar", nullable = false)
    private String renda_familiar;

    //Endereço
    @Column(name = "endereco", nullable = false)
    private String endereco;

    @Column(name = "bairro", nullable = false)
    private String bairro;

    @Column(name = "cidade", nullable = false)
    private String cidade;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "CEP", nullable = false)
    private String cep;

    //Ficha médica

    @ElementCollection
    @CollectionTable(name = "historico_vacinas", joinColumns = @JoinColumn(name = "paciente_id"))
    @Column(name = "historico_vacinas")
    private List<String> historico_vacinas;

    @ElementCollection
    @CollectionTable(name = "historico_doencas", joinColumns = @JoinColumn(name = "paciente_id"))
    @Column(name = "historico_doencas")
    private List<String> historico_doencas;

    @ElementCollection
    @CollectionTable(name = "historico_alergias", joinColumns = @JoinColumn(name = "paciente_id"))
    @Column(name = "historico_alergias")
    private List<String> historico_alergias;

    @ElementCollection
    @CollectionTable(name = "medicacoes_continuas", joinColumns = @JoinColumn(name = "paciente_id"))
    @Column(name = "medicacoes_continuas")
    private List<String> medicacoes_continuas;

    //o que seria esse tipo de atendimento?
    @Column(name = "tipo_atendimento", nullable = false)
    private String tipo_atendimento;

    //Informações do responsável(Pai)
    @Column(name = "nome_pai", nullable = false)
    private String nome_pai;

    @Column(name = "pai_vivo", nullable = false)
    private boolean pai_vivo;

    @Column(name = "profissao_pai", nullable = false)
    private String profissao_pai;

    @Column(name = "rg_pai", nullable = false)
    private String rg_pai;

    @Column(name = "cpf_pai", nullable = false)
    private String cpf_pai;

    //Informações do responsável(Mae)
    @Column(name = "nome_mae", nullable = false)
    private String nome_mae;

    @Column(name = "mae_vivo", nullable = false)
    private boolean mae_vivo;

    @Column(name = "profissao_mae", nullable = false)
    private String profissao_mae;

    @Column(name = "rg_mae", nullable = false)
    private String rg_mae;

    @Column(name = "cpf_mae", nullable = false)
    private String cpf_mae;

    /*Informações extras
        Rever essa informação;
        Tentar deixar as informações do responsável mais genéricas;
        Tentar fazer uma opção de adicionar mais de um responsável;
    */
    @Column(name = "outros_responsaveis", nullable = false)
    private String outros_responsaveis;

    @Column(name = "emergencia_quem_procurar", nullable = false)
    private String emergencia_quem_procurar;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate data_cadastro;

    /*
    Na ficha que recebemos só tem um campo para o contato, então deve ser ajustado esse atributo

    @ElementCollection
    @CollectionTable(name = "contatos_paciente", joinColumns = @JoinColumn(name = "paciente_id"))
    @Column(name = "contato")
    private List<String> contatos;
     */

    public UUID getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }

    /*
    public List<String> getContatos() {
        return contatos;
    }

    public void setContatos(List<String> contatos) {
        this.contatos = contatos;
    }
    */

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
        result = prime * result + ((contato == null) ? 0 : contato.hashCode());
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
        if (contato == null) {
            if (other.contato != null)
                return false;
        } else if (!contato.equals(other.contato))
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
        return "Paciente [id=" + id + ", nome_completo=" + nome_completo + ", contato=" + contato
                + ", data_nascimento=" + data_nascimento + "]";
    }


    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getNum_registro_nasc() {
        return num_registro_nasc;
    }

    public void setNum_registro_nasc(String num_registro_nasc) {
        this.num_registro_nasc = num_registro_nasc;
    }

    public String getFls() {
        return fls;
    }

    public void setFls(String fls) {
        this.fls = fls;
    }

    public String getLiv() {
        return liv;
    }

    public void setLiv(String liv) {
        this.liv = liv;
    }

    public String getCartorio() {
        return cartorio;
    }

    public void setCartorio(String cartorio) {
        this.cartorio = cartorio;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getData_emissao_rg() {
        return data_emissao_rg;
    }

    public void setData_emissao_rg(LocalDate data_emissao_rg) {
        this.data_emissao_rg = data_emissao_rg;
    }

    public String getOrgao_emissor_rg() {
        return orgao_emissor_rg;
    }

    public void setOrgao_emissor_rg(String orgao_emissor_rg) {
        this.orgao_emissor_rg = orgao_emissor_rg;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getCns() {
        return cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public boolean isPossui_bcp() {
        return possui_bcp;
    }

    public void setPossui_bcp(boolean possui_bcp) {
        this.possui_bcp = possui_bcp;
    }

    public String getRenda_familiar() {
        return renda_familiar;
    }

    public void setRenda_familiar(String renda_familiar) {
        this.renda_familiar = renda_familiar;
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

    public List<String> getHistorico_vacinas() {
        return historico_vacinas;
    }

    public void setHistorico_vacinas(List<String> historico_vacinas) {
        this.historico_vacinas = historico_vacinas;
    }

    public List<String> getHistorico_doencas() {
        return historico_doencas;
    }

    public void setHistorico_doencas(List<String> historico_doencas) {
        this.historico_doencas = historico_doencas;
    }

    public List<String> getHistorico_alergias() {
        return historico_alergias;
    }

    public void setHistorico_alergias(List<String> historico_alergias) {
        this.historico_alergias = historico_alergias;
    }

    public List<String> getMedicacoes_continuas() {
        return medicacoes_continuas;
    }

    public void setMedicacoes_continuas(List<String> medicacoes_continuas) {
        this.medicacoes_continuas = medicacoes_continuas;
    }

    public String getTipo_atendimento() {
        return tipo_atendimento;
    }

    public void setTipo_atendimento(String tipo_atendimento) {
        this.tipo_atendimento = tipo_atendimento;
    }

    public String getNome_pai() {
        return nome_pai;
    }

    public void setNome_pai(String nome_pai) {
        this.nome_pai = nome_pai;
    }

    public boolean isPai_vivo() {
        return pai_vivo;
    }

    public void setPai_vivo(boolean pai_vivo) {
        this.pai_vivo = pai_vivo;
    }

    public String getProfissao_pai() {
        return profissao_pai;
    }

    public void setProfissao_pai(String profissao_pai) {
        this.profissao_pai = profissao_pai;
    }

    public String getRg_pai() {
        return rg_pai;
    }

    public void setRg_pai(String rg_pai) {
        this.rg_pai = rg_pai;
    }

    public String getCpf_pai() {
        return cpf_pai;
    }

    public void setCpf_pai(String cpf_pai) {
        this.cpf_pai = cpf_pai;
    }

    public String getNome_mae() {
        return nome_mae;
    }

    public void setNome_mae(String nome_mae) {
        this.nome_mae = nome_mae;
    }

    public boolean isMae_vivo() {
        return mae_vivo;
    }

    public void setMae_vivo(boolean mae_vivo) {
        this.mae_vivo = mae_vivo;
    }

    public String getProfissao_mae() {
        return profissao_mae;
    }

    public void setProfissao_mae(String profissao_mae) {
        this.profissao_mae = profissao_mae;
    }

    public String getRg_mae() {
        return rg_mae;
    }

    public void setRg_mae(String rg_mae) {
        this.rg_mae = rg_mae;
    }

    public String getCpf_mae() {
        return cpf_mae;
    }

    public void setCpf_mae(String cpf_mae) {
        this.cpf_mae = cpf_mae;
    }

    public String getOutros_responsaveis() {
        return outros_responsaveis;
    }

    public void setOutros_responsaveis(String outros_responsaveis) {
        this.outros_responsaveis = outros_responsaveis;
    }

    public String getEmergencia_quem_procurar() {
        return emergencia_quem_procurar;
    }

    public void setEmergencia_quem_procurar(String emergencia_quem_procurar) {
        this.emergencia_quem_procurar = emergencia_quem_procurar;
    }

    public LocalDate getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(LocalDate data_cadastro) {
        this.data_cadastro = data_cadastro;
    }
}
