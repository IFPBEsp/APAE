package br.org.apae.profissional_da_saude.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class ProfissionalSaudeCreateDTO {

    @NotBlank
    private String areaDaSaude;

    @NotBlank
    private String telefone;

    @NotBlank
    private String docProfissional;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String nome;

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
}
