package br.org.apae.profissional_da_saude.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProfissionalSaudeUpdateDTO {
    @Size(min = 3, max = 100)
    private String areaDaSaude;
    @Pattern(regexp = "^$|\\d{10,11}", message = "Telefone inválido")
    private String telefone;
    @Size(min = 3,max = 100)
    private String docProfissional;
    @Email
    private String email;
    @Size(min = 3, max = 100)
    private String nome;
}
