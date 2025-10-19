package br.org.apae.api.common.dto.patient.response.guardian;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.patient.domain.model.Guardian;

import java.util.Optional;
import java.util.UUID;

/**
 * DTO para representar os dados de resposta de um Responsável.
 */
public record GuardianResponseDTO(
        UUID id,
        String name,
        String contact,
        String kinship,
        AddressResponseDTO address) {
    /**
     * Construtor que converte uma entidade Guardian para o DTO de resposta.
     * 
     * @param guardian A entidade Guardian.
     */
    public GuardianResponseDTO(Guardian guardian) {
        this(
                guardian.getId(),
                guardian.getName(),
                guardian.getContact(),
                guardian.getKinship(),
                Optional.ofNullable(guardian.getAddress())
                        .map(AddressResponseDTO::new)
                        .orElse(null));
    }
}
