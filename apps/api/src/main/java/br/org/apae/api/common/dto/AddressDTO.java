package br.org.apae.api.common.dto;

import br.org.apae.api.common.validations.RegexPatterns;
import br.org.apae.api.common.validations.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressDTO(
        @NotBlank
        @Pattern(regexp = RegexPatterns.STATE_PROVINCE, message = ValidationMessages.INVALID_STATE)
        String state,

        @NotBlank
        @Pattern(regexp = RegexPatterns.CITY, message = ValidationMessages.INVALID_CITY)
        String city,

        @NotBlank
        @Pattern(regexp = RegexPatterns.NEIGHBORHOOD, message = ValidationMessages.INVALID_NEIGHBORHOOD)
        String neighborhood,

        @NotBlank
        @Pattern(regexp = RegexPatterns.STREET, message = ValidationMessages.INVALID_STREET)
        String street,

        @NotBlank
        @Pattern(regexp = RegexPatterns.NUMBER, message = ValidationMessages.INVALID_NUMBER)
        String number,

        @NotBlank
        @Pattern(regexp = RegexPatterns.ZIP_CODE_CEP, message = ValidationMessages.INVALID_ZIP_CODE)
        String cep,

        @Pattern(regexp = RegexPatterns.COMPLEMENT, message = ValidationMessages.INVALID_COMPLEMENT)
        String complement
) {}