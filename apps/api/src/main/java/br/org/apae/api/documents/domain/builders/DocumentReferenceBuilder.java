package br.org.apae.api.documents.domain.builders;

import java.time.Year;
import java.util.Optional;
import java.util.UUID;

import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;

public class DocumentReferenceBuilder {
    static private final String REFERENCE_PREFIX = "DOCUMENTO";

    static public String buildDocumentPath(
            DocumentCategory category,
            DocumentType type,
            Year year) {
        return String.format("%s_%s/%s/%s",
                REFERENCE_PREFIX,
                category.toString(),
                year.toString(),
                type.toString());
    }

    static public String buildDocumentName(
            DocumentCategory category,
            DocumentType type,
            Year year,
            UUID id) {
        return String.format("%s/%s",
                buildDocumentPath(category, type, year), id.toString());
    }

    static public String buildDocumentPrefix(DocumentCategory category) {
        return String.format("%s_%s", REFERENCE_PREFIX, category);
    }

    static public String buildDocumentPrefix(DocumentCategory category, Year year) {
        if (year == null)
            return buildDocumentPrefix(category);
        return String.format("%s_%s/%s", REFERENCE_PREFIX, category, year);
    }

    static public String buildDocumentPrefix(DocumentCategory category, Year year, DocumentType type) {
        if (year == null || type == null)
            return buildDocumentPrefix(category, year);
        return String.format("%s_%s/%s/%s", REFERENCE_PREFIX, category, year, type);
    }
}
