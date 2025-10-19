package br.org.apae.api.common.dto.patient.request.guardian;

import br.org.apae.api.common.dto.address.UpdateAddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateGuardianDTO(
    @NotBlank(message = "O campo nome é obrigatório.") String name,

    @NotBlank(message = "O campo contato é obrigatório.") String contact,

    @NotBlank(message = "O campo parentesco é obrigatório.") String kinship,

    @NotNull(message = "O endereço do responsável é obrigatório.") @Valid UpdateAddressDTO address) {
}
