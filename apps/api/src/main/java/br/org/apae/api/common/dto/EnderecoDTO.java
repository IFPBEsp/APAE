package br.org.apae.api.common.dto;

import br.org.apae.api.professional.da.saude.validations.RegexPatterns;
import br.org.apae.api.professional.da.saude.validations.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EnderecoDTO {

    @NotBlank
    @Pattern(regexp = RegexPatterns.ESTADO, message = ValidationMessages.ESTADO_INVALIDO)
    private String state;

    @NotBlank
    @Pattern(regexp = RegexPatterns.CIDADE, message = ValidationMessages.CIDADE_INVALIDA)
    private String city;

    @NotBlank
    @Pattern(regexp = RegexPatterns.BAIRRO, message = ValidationMessages.BAIRRO_INVALIDO)
    private String neighborhood;

    @NotBlank
    @Pattern(regexp = RegexPatterns.RUA, message = ValidationMessages.RUA_INVALIDA)
    private String road;

    @NotBlank
    @Pattern(regexp = RegexPatterns.NUMERO, message = ValidationMessages.NUMERO_INVALIDO)
    private String number;

    @NotBlank
    @Pattern(regexp = RegexPatterns.CEP, message = ValidationMessages.CEP_INVALIDO)
    private String cep;

    @Pattern(regexp = RegexPatterns.COMPLEMENTO, message = ValidationMessages.COMPLEMENTO_INVALIDO)
    private String complement;

}
