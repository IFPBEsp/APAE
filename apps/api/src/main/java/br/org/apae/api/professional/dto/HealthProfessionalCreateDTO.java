package br.org.apae.api.professional.dto;

import br.org.apae.api.common.dto.EnderecoDTO;
import br.org.apae.api.professional.da.saude.validations.RegexPatterns;
import br.org.apae.api.professional.da.saude.validations.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthProfessionalCreateDTO {

    @NotBlank
    @Size(min = 3, max = 100)
    private String healthSector;

    @NotBlank
    @Pattern( regexp = RegexPatterns.TELEFONE, message = ValidationMessages.TELEFONE_INVALIDO )
    private String telephone;

    @NotBlank
    @Pattern( regexp = RegexPatterns.DOC_PROFISSIONAL, message = ValidationMessages.DOC_PROFISSIONAL_INVALIDO )
    private String docProfessional;

    @Email(message = ValidationMessages.EMAIL_INVALIDO)
    @NotBlank
    @Size(max = 254)
    private String email;

    @NotBlank
    @Pattern( regexp = RegexPatterns.NOME, message = ValidationMessages.NOME_INVALIDO )
    private String name;

    @NotBlank
    @Pattern( regexp = RegexPatterns.RG, message = ValidationMessages.RG_INVALIDO )
    private String generalRegistry;

    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private EnderecoDTO address;
}