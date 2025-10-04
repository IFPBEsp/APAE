package br.org.apae.api.documents.interfaces.dto;

import java.time.Year;
import java.util.UUID;

import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;

public record DocumentWriteResponseDTO(
        UUID id,
        String name,
        DocumentCategory category,
        DocumentType type,
        String owner,
        Year year) {
}
