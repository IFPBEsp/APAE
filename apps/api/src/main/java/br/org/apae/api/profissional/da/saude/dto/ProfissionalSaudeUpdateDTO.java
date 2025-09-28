package br.org.apae.api.profissional.da.saude.dto;

import br.org.apae.api.common.dto.EnderecoDTO;
import br.org.apae.api.profissional.da.saude.validations.RegexPatterns;
import br.org.apae.api.profissional.da.saude.validations.ValidationMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class ProfissionalSaudeUpdateDTO {

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

    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private EnderecoDTO endereco;
}