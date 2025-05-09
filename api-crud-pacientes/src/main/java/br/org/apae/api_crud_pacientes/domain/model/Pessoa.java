package br.org.apae.api_crud_pacientes.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "pessoas")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome_completo", nullable = false)
    private String nome_completo;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate data_nascimento;

    @Column(name = "num_registro_nasc", nullable = false)
    private String num_registro_nasc;

    @Column(name = "FLS", nullable = false)
    private String fls;

    @Column(name = "livro", nullable = false)
    private String livro;

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

    @Column(name = "CNS", nullable = false)
    private String cns;

    @Column(name = "NIS", nullable = false)
    private String nis;

    @Column(name = "data_cadastramento", nullable = false)
    private LocalDate data_cadastramento;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<PessoaResponsavel> responsaveis;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<CadastroAnual> cadastrosAnuais;

    public UUID getId() {
        return id;
    }

    public String getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }

    public LocalDate getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(LocalDate data_nascimento) {
        this.data_nascimento = data_nascimento;
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

    public String getLivro() {
        return livro;
    }

    public void setLivro(String livro) {
        this.livro = livro;
    }

    public String getCartorio() {
        return cartorio;
    }

    public void setCartorio(String cartorio) {
        this.cartorio = cartorio;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public LocalDate getData_cadastramento() {
        return data_cadastramento;
    }

    public void setData_cadastramento(LocalDate data_cadastramento) {
        this.data_cadastramento = data_cadastramento;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(id, pessoa.id)
                && Objects.equals(nome_completo, pessoa.nome_completo)
                && Objects.equals(data_nascimento, pessoa.data_nascimento)
                && Objects.equals(num_registro_nasc, pessoa.num_registro_nasc)
                && Objects.equals(cpf, pessoa.cpf)
                && Objects.equals(rg, pessoa.rg)
                && Objects.equals(cns, pessoa.cns)
                && Objects.equals(nis, pessoa.nis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome_completo, data_nascimento, num_registro_nasc, cpf, rg, cns, nis);
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                ", nome_completo='" + nome_completo + '\'' +
                ", data_nascimento=" + data_nascimento +
                ", num_registro_nasc='" + num_registro_nasc + '\'' +
                ", cpf='" + cpf + '\'' +
                ", rg='" + rg + '\'' +
                ", cns='" + cns + '\'' +
                ", nis='" + nis + '\'' +
                '}';
    }
}