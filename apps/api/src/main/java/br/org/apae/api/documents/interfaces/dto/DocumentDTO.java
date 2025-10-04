
package br.org.apae.api.documents.interfaces.dto;

import java.time.Year;

import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;

public record DocumentDTO(
        String name,
        DocumentCategory category,
        DocumentType type,
        String owner,
        Year year) {
}
