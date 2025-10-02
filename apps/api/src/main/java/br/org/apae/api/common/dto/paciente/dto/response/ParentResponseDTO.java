package br.org.apae.api.common.dto.paciente.dto.response;

import br.org.apae.api.patient.domain.model.Parent;

import java.util.UUID;


public record ParentResponseDTO(
        UUID id,
        String name,
        String rg,
        String cpf,
        String profession,
        String kinship,
        Boolean isAlive
) {
    public ParentResponseDTO(Parent parent) {
        this(
                parent.getId(),
                parent.getName(),
                parent.getRg(),
                parent.getCpf(),
                parent.getProfession(),
                parent.getKinship(),
                parent.isAlive()
        );
    }
}
