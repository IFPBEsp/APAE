package br.org.apae.api.common.dto.professional.request;

import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.servicetype.request.CreateServiceTypeDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateHealthProfessionalDTO(
    @NotNull(message = "Área de atendimento é obrigatório.") @Valid CreateServiceTypeDTO serviceArea,

    @NotBlank(message = "O número de telefone é obrigatório.") String phoneNumber,

    @Size(min = 3, max = 50) String professionalDocument,

    @Email(message = "O e-mail informado é inválido.") @NotBlank(message = "O e-mail é obrigatório.") String email,

    @NotBlank(message = "O nome é obrigatório.") @Size(min = 3, max = 100) String name,

    @NotBlank(message = "O documento de identidade é obrigatório.") String identityDocument,

    @NotNull(message = "O endereço é obrigatório.") @Valid CreateAddressDTO address,

    @Valid List<CreateAvailabilityDTO> availabilities,
    
    String profilePhoto
) {
}
