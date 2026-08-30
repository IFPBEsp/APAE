package br.org.apae.api.common.dto.professional.request;

import br.org.apae.api.common.dto.address.UpdateAddressDTO;
import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.validations.CPF;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record UpdateHealthProfessionalDTO(
    @NotNull(message = "Área de atendimento é obrigatório.")
    @Valid UpdateServiceAreaDTO serviceArea,

    @NotBlank(message = "O número de telefone é obrigatório.")
    String phoneNumber,

    @Size(min = 3, max = 50)
    String professionalDocument,

    @Email(message = "O e-mail informado é inválido.")
    @NotBlank(message = "O e-mail é obrigatório.")
    String email,

    @CPF(message = "CPF inválido")
    @NotBlank(message = "O CPF é obrigatório.")
    String cpf,

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100)
    String name,

    @NotBlank(message = "O documento de identidade é obrigatório.")
    String identityDocument,

    @NotNull(message = "Endereço é obrigatório.")
    @Valid UpdateAddressDTO address,

    @Valid List<CreateAvailabilityDTO> availabilities,

    String profilePhoto
) {
}
