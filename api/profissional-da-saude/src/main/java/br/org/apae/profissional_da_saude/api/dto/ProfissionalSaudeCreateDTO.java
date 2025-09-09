package br.org.apae.profissional_da_saude.api.dto;

import br.org.apae.profissional_da_saude.api.validation.RegexPatterns;
import br.org.apae.profissional_da_saude.api.validation.ValidationMessages;
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
    @Pattern( regexp = RegexPatterns.TELEFONE, message = ValidationMessages.TELEFONE_INVALIDO )
    private String telefone;

    @NotBlank
    @Pattern( regexp = RegexPatterns.DOC_PROFISSIONAL, message = ValidationMessages.DOC_PROFISSIONAL_INVALIDO )
    private String docProfissional;

    @Email(message = ValidationMessages.EMAIL_INVALIDO)
    @NotBlank
    @Size(max = 254)
    private String email;

    @NotBlank
    @Pattern( regexp = RegexPatterns.NOME, message = ValidationMessages.NOME_INVALIDO )
    private String nome;

    @NotBlank
    @Pattern( regexp = RegexPatterns.RG, message = ValidationMessages.RG_INVALIDO )
    private String rg;

    @NotBlank
    @Pattern( regexp = RegexPatterns.ESTADO, message = ValidationMessages.ESTADO_INVALIDO )
    private String estado;

    @NotBlank
    @Pattern( regexp = RegexPatterns.CIDADE, message = ValidationMessages.CIDADE_INVALIDA )
    private String cidade;

    @NotBlank
    @Pattern( regexp = RegexPatterns.BAIRRO, message = ValidationMessages.BAIRRO_INVALIDO )
    private String bairro;

    @NotBlank
    @Pattern( regexp = RegexPatterns.RUA, message = ValidationMessages.RUA_INVALIDA )
    private String rua;

    @NotBlank
    @Pattern( regexp = RegexPatterns.NUMERO, message = ValidationMessages.NUMERO_INVALIDO )
    private String numero;

    @NotBlank
    @Pattern( regexp = RegexPatterns.CEP, message = ValidationMessages.CEP_INVALIDO )
    private String cep;

    @Pattern( regexp = RegexPatterns.COMPLEMENTO, message = ValidationMessages.COMPLEMENTO_INVALIDO )
    private String complemento;
}
