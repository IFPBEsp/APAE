package br.org.apae.profissional_da_saude.api.dto;

import br.org.apae.profissional_da_saude.api.validation.RegexPatterns;
import br.org.apae.profissional_da_saude.api.validation.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EnderecoDTO {

    @NotBlank
    @Pattern(regexp = RegexPatterns.ESTADO, message = ValidationMessages.ESTADO_INVALIDO)
    private String estado;

    @NotBlank
    @Pattern(regexp = RegexPatterns.CIDADE, message = ValidationMessages.CIDADE_INVALIDA)
    private String cidade;

    @NotBlank
    @Pattern(regexp = RegexPatterns.BAIRRO, message = ValidationMessages.BAIRRO_INVALIDO)
    private String bairro;

    @NotBlank
    @Pattern(regexp = RegexPatterns.RUA, message = ValidationMessages.RUA_INVALIDA)
    private String rua;

    @NotBlank
    @Pattern(regexp = RegexPatterns.NUMERO, message = ValidationMessages.NUMERO_INVALIDO)
    private String numero;

    @NotBlank
    @Pattern(regexp = RegexPatterns.CEP, message = ValidationMessages.CEP_INVALIDO)
    private String cep;

    @Pattern(regexp = RegexPatterns.COMPLEMENTO, message = ValidationMessages.COMPLEMENTO_INVALIDO)
    private String complemento;
}
