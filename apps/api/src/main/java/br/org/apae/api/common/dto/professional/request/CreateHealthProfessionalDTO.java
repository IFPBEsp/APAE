package br.org.apae.api.common.dto.professional.request;

import br.org.apae.api.common.dto.address.CreateAddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record CreateHealthProfessionalDTO(
    @NotBlank(message = "O setor de saúde é obrigatório.")
    @Size(min = 3, max = 100)
    String healthSector,

    @NotBlank(message = "O número de telefone é obrigatório.")
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{4,5}-\\d{4}$", message = "Telefone inválido")
    String phoneNumber,

    @NotBlank(message = "O documento profissional é obrigatório.")
    @Pattern(regexp = "^[A-Z]{2,8}\\s?\\d{1,6}(?:[/-][A-Z0-9]{1,5})?$", message = "Documento profissional inválido")
    @Size(min = 3, max = 50)
    String professionalDocument,

    @Email(message = "O e-mail informado é inválido.")
    @NotBlank(message = "O e-mail é obrigatório.")
    String email,

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100)
    String name,

    @NotBlank(message = "O documento de identidade é obrigatório.")
    @Pattern(
            regexp = "^(\\d{7,9}|\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$",
            message = "Documento inválido"
    )
    String identityDocument,

    @NotNull(message = "O endereço é obrigatório.") @Valid CreateAddressDTO address) {
}
