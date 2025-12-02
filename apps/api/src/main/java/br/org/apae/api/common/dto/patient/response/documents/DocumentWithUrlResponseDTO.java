package br.org.apae.api.common.dto.patient.response.documents;

import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;

import java.time.Year;
import java.util.UUID;

public record DocumentWithUrlResponseDTO(
        UUID id,
        String name,
        DocumentCategory category,
        DocumentType type,
        String owner,
        Year year,
        String url
) {
}
