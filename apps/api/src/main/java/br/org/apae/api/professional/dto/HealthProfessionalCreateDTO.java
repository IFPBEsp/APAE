package br.org.apae.api.professional.dto;

import br.org.apae.api.common.dto.AddressDTO;
import br.org.apae.api.professional.validations.RegexPatterns;
import br.org.apae.api.professional.validations.ValidationMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record HealthProfessionalCreateDTO(
        @NotBlank
        @Size(min = 3, max = 100)
        String healthSector,

        @NotBlank
        @Pattern( regexp = RegexPatterns.PHONE_NUMBER, message = ValidationMessages.INVALID_PHONE )
        String phoneNumber,

        @NotBlank
        @Pattern( regexp = RegexPatterns.PROFESSIONAL_DOCUMENT, message = ValidationMessages.INVALID_PROFESSIONAL_DOCUMENT )
        String professionalDocument,

        @Email(message = ValidationMessages.INVALID_EMAIL)
        @NotBlank
        @Size(max = 254)
        String email,

        @NotBlank
        @Pattern( regexp = RegexPatterns.NAME, message = ValidationMessages.INVALID_NAME )
        String name,

        @NotBlank
        @Pattern( regexp = RegexPatterns.IDENTITY_DOCUMENT_RG, message = ValidationMessages.INVALID_IDENTITY_DOCUMENT )
        String identityDocument,

        @NotNull(message = "Address is mandatory")
        @Valid
        AddressDTO address
) {}