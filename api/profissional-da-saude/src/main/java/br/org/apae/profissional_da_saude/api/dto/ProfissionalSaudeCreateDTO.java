package br.org.apae.profissional_da_saude.api.dto;

import br.org.apae.profissional_da_saude.api.validation.RegexPatterns;
import br.org.apae.profissional_da_saude.api.validation.ValidationMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @NotNull
    @Valid
    private List<DisponibilidadeDTO> disponibilidades;
    @NotBlank
    @Pattern( regexp = RegexPatterns.RG, message = ValidationMessages.RG_INVALIDO )
    private String rg;

    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private EnderecoDTO endereco;
}
