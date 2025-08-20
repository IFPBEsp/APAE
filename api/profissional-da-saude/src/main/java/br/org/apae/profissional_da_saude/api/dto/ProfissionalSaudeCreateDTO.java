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
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]{3,100}$", message = "Nome inválido")
    private String nome;

    @NotBlank
    @Pattern(regexp = "^\\d{1,2}\\.?\\d{3}\\.?\\d{3}-?\\d{1}$", message = "RG inválido")
    private String rg;

    @NotBlank
    @Pattern(regexp = "(^[A-Z]{2}$)", message = "Estado inválido. Formato esperado: XX")
    private String estado;

    @NotBlank
    @Pattern(regexp = "(^[A-Za-zÀ-ÿ\\s]+$)", message = "Cidade inválida. Não pode conter números")
    private String cidade;

    @NotBlank
    @Pattern(regexp = "(^[A-Za-zÀ-ÿ\\s]+$)", message = "Bairro inválido")
    private String bairro;

    @NotBlank
    @Pattern(regexp = "(^[A-Za-zÀ-ÿ0-9\\s]+$)", message = "Rua inválida")
    private String rua;

    @NotBlank
    @Pattern(regexp = "(^\\d+[A-Za-z]?$)", message = "Número inválido")
    private String numero;

    @NotBlank
    @Pattern(regexp = "(^\\d{5}-\\d{3}$)", message = "CEP inválido. Formato esperado: XXXXX-XXX")
    private String cep;

    @Pattern(regexp = "(^[A-Za-zÀ-ÿ0-9\\s\\-\\/\\.]*$)") // Pode ser vazio também
    private String complemento;
}
