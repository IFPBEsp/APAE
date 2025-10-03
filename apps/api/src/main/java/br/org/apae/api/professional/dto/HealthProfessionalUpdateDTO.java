package br.org.apae.api.professional.dto;

import br.org.apae.api.common.dto.AddressDTO;
import br.org.apae.api.professional.validations.RegexPatterns;
import br.org.apae.api.professional.validations.ValidationMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record HealthProfessionalUpdateDTO(
        @Size(min = 3, max = 100)
        String healthSector,

        @Pattern( regexp = RegexPatterns.PHONE_NUMBER, message = ValidationMessages.INVALID_PHONE )
        String phoneNumber,

        @Pattern( regexp = RegexPatterns.PROFESSIONAL_DOCUMENT, message = ValidationMessages.INVALID_PROFESSIONAL_DOCUMENT )
        String professionalDocument,

        @Email(message = ValidationMessages.INVALID_EMAIL)
        @Size(max = 254)
        String email,

        @Pattern( regexp = RegexPatterns.NAME, message = ValidationMessages.INVALID_NAME )
        String name,

        @Pattern( regexp = RegexPatterns.IDENTITY_DOCUMENT_RG, message = ValidationMessages.INVALID_IDENTITY_DOCUMENT )
        String identityDocument,

        @Valid
        AddressDTO address
) {}