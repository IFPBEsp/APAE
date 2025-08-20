package br.org.apae.profissional_da_saude.domain.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class ProfissionalSaude {
    // Atributos
    private  UUID id;
    private  String areaDaSaude;
    private  String telefone;
    private  String docProfissional;
    private  String email;
    private  String nome;

    // Inserção de novos atributos
    private  String rg;
    private  String estado;
    private  String cidade;
    private  String bairro;
    private  String rua;
    private  String numero;
    private  String cep;
    private  String complemento;

    public ProfissionalSaude(UUID id, String areaDaSaude, String telefone, String docProfissional, String email, String nome,
                             String rg, String estado, String cidade, String bairro, String rua, String numero, String cep, String complemento) {
        this.id = id;
        this.areaDaSaude = areaDaSaude;
        this.telefone = telefone;
        this.docProfissional = docProfissional;
        this.email = email;
        this.nome = nome;
        this.rg = rg;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
        this.complemento = complemento;
    }

    public ProfissionalSaude(@NotBlank @Size(min = 3, max = 100) String areaDaSaude,
                             @NotBlank @Pattern(regexp = "^\\(\\d{2}\\) \\d{5}-\\d{4}$", message = "Telefone inválido. Formato esperado: (xx) xxxxx-xxxx") String telefone,
                             @NotBlank @Pattern(regexp = "^(CRM|COREN|CREFITO|CRFa|CRP|CRESS)([-/][A-Z0-9]{1,2}|\\s\\d{2})?\\s?\\d{1,6}$|^[A-Za-z0-9./-]{3,100}$", message = "Documento profissional inválido") String docProfissional, @Email(message = "E-mail inválido")
                             @NotBlank @Size(max = 254) String email,
                             @NotBlank @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]{3,100}$", message = "Nome inválido") String nome) {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAreaDaSaude() {
        return areaDaSaude;
    }

    public void setAreaDaSaude(String areaDaSaude) {
        this.areaDaSaude = areaDaSaude;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDocProfissional() {
        return docProfissional;
    }

    public void setDocProfissional(String docProfissional) {
        this.docProfissional = docProfissional;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
