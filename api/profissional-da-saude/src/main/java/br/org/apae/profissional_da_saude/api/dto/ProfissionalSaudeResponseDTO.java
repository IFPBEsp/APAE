package br.org.apae.profissional_da_saude.api.dto;

import java.util.UUID;

public class ProfissionalSaudeResponseDTO {

    private UUID id;
    private String areaDaSaude;
    private String telefone;
    private String docProfissional;
    private String email;
    private String nome;

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
}
