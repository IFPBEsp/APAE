package br.org.apae.profissional_da_saude.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfissionalSaudeCreateDTO {

    @NotBlank
    @Size(min = 3, max = 100)
    private String areaDaSaude;

    @NotBlank
    @Pattern(regexp = "^$|\\d{10,11}", message = "Telefone inválido")
    private String telefone;

    @NotBlank
    @Size(min = 3, max = 100)
    private String docProfissional;

    @Email(message = "E-mail inválido")
    @NotBlank
    @Size(max = 254)
    private String email;

    @NotBlank
    @Size(min = 3, max = 100)
    private String nome;

}
