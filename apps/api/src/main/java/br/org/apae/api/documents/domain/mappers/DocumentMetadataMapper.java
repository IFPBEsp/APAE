package br.org.apae.api.documents.domain.mappers;

import java.time.Year;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import br.org.apae.api.documents.domain.builders.DocumentReferenceBuilder;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;

public class DocumentMetadataMapper {
        private DocumentMetadataMapper() {
        }

        public static Map<String, String> from(UUID id,
                        DocumentCategory category,
                        DocumentType type,
                        String owner,
                        Year year) {
                return Map.of(
                                "owner", owner,
                                "id", id.toString(),
                                "category", category.toString(),
                                "type", type.toString(),
                                "year", year.toString());
        }

        public static Map<String, String> from(DocumentDTO dto) {
                return from(
                                dto.id(),
                                dto.category(),
                                dto.type(),
                                dto.owner(),
                                dto.year());
        }

        private static String minioMetadataKey(String key) {
                return String.format("X-Amz-Meta-%s",
                                Pattern.compile("^.")
                                                .matcher(key)
                                                .replaceFirst(m -> m.group().toUpperCase()));
        }

        public static DocumentDTO from(Map<String, String> metadata) {
                UUID id = UUID.fromString(
                                metadata.get(minioMetadataKey("id")));

                DocumentCategory category = DocumentCategory.fromValue(
                                metadata.get(minioMetadataKey("category")));

                DocumentType type = DocumentType.fromValue(
                                metadata.get(minioMetadataKey("type")));
                String owner = metadata.get(minioMetadataKey("owner"));
                Year year = Year.parse(
                                metadata.get(minioMetadataKey("year")));
                String name = DocumentReferenceBuilder
                                .buildDocumentName(category, type, year, id);

                return new DocumentDTO(
                                id,
                                name,
                                category,
                                type,
                                owner,
                                year);
        }
}
