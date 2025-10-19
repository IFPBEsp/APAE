package br.org.apae.api.common.dto.patient.request.parent;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateParentDTO(
        @NotNull() UUID id,
        @NotBlank(message = "O campo nome é obrigatório.") String name,
        String rg,
        String cpf,
        String profession,
        @NotNull(message = "O campo 'vivo' é obrigatório") Boolean isAlive,
        @NotBlank(message = "O campo parentesco é obrigatório.") String kinship) {
}
