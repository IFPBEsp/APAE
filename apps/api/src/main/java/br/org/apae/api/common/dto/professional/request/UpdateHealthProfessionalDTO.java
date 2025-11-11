package br.org.apae.api.common.dto.professional.request;

import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.address.UpdateAddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record UpdateHealthProfessionalDTO(
        @NotBlank(message = "O setor de saúde é obrigatório.") String healthSector,
        @NotBlank(message = "O número de telefone é obrigatório.") String phoneNumber,
        @NotBlank(message = "O documento profissional é obrigatório.") String professionalDocument,
        @Email(message = "O e-mail informado é inválido.") @NotBlank(message = "O e-mail é obrigatório.") String email,
        @NotBlank(message = "O nome é obrigatório.") String name,
        @NotBlank(message = "O documento de identidade é obrigatório.") String identityDocument,
        @NotNull(message = "O endereço é obrigatório.") @Valid UpdateAddressDTO address,
        List<@Valid UpdateAvailabilityDTO> availabilities
) { }
