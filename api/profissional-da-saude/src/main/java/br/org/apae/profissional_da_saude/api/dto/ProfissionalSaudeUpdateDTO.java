package br.org.apae.profissional_da_saude.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProfissionalSaudeUpdateDTO {
    @NotBlank
    @Size(min = 3, max = 100)
    private String areaDaSaude;

    @NotBlank
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{5}-\\d{4}$", message = "Telefone inválido. Formato esperado: (xx) xxxxx-xxxx")
    private String telefone;

    @NotBlank
    @Pattern(regexp = "^(CRM|COREN|CREFITO|CRFa|CRP|CRESS)([-/][A-Z0-9]{1,2}|\\s\\d{2})?\\s?\\d{1,6}$|^[A-Za-z0-9./-]{3,100}$", message = "Documento profissional inválido")
    private String docProfissional;

    @Email(message = "E-mail inválido")
    @NotBlank
    @Size(max = 254)
    private String email;

    @NotBlank
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]{3,100}$", message = "Nome inválido. Não pode conter números e deve ter entre 3 e 100 caracteres")
    private String nome;
}
