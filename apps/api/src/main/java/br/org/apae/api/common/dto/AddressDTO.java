package br.org.apae.api.common.dto;

import br.org.apae.api.professional.validations.RegexPatterns;
import br.org.apae.api.professional.validations.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AddressDTO {

    @NotBlank
    @Pattern(regexp = RegexPatterns.STATE_PROVINCE, message = ValidationMessages.INVALID_STATE)
    private String state;

    @NotBlank
    @Pattern(regexp = RegexPatterns.CITY, message = ValidationMessages.INVALID_CITY)
    private String city;

    @NotBlank
    @Pattern(regexp = RegexPatterns.NEIGHBORHOOD, message = ValidationMessages.INVALID_NEIGHBORHOOD)
    private String neighborhood;

    @NotBlank
    @Pattern(regexp = RegexPatterns.STREET, message = ValidationMessages.INVALID_STREET)
    private String street;

    @NotBlank
    @Pattern(regexp = RegexPatterns.NUMBER, message = ValidationMessages.INVALID_NUMBER)
    private String number;

    @NotBlank
    @Pattern(regexp = RegexPatterns.ZIP_CODE_CEP, message = ValidationMessages.INVALID_ZIP_CODE)
    private String cep;

    @Pattern(regexp = RegexPatterns.COMPLEMENT, message = ValidationMessages.INVALID_COMPLEMENT)
    private String complement;

}