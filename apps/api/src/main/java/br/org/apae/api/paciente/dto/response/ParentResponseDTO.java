package br.org.apae.api.paciente.dto.response;

import br.org.apae.api.paciente.domain.model.Parent;

import java.util.UUID;


public record ParentResponseDTO(
        UUID id,
        String name,
        String rg,
        String cpf,
        String profession,
        String kinship
) {
    public ParentResponseDTO(Parent parent) {
        this(
                parent.getId(),
                parent.getName(),
                parent.getRg(),
                parent.getCpf(),
                parent.getProfession(),
                parent.getKinship()
        );
    }
}
