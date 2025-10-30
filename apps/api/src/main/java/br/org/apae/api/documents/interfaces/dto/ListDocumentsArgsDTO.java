package br.org.apae.api.documents.interfaces.dto;

import java.time.Year;
import java.util.Optional;

import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.exceptions.InsufficientDataException;

public record ListDocumentsArgsDTO(
        DocumentCategory category,
        DocumentType type,
        String owner,
        Year year) {

    public static ListDocumentsArgsDTO.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DocumentCategory category;
        private DocumentType type;
        private String owner;
        private Year year;

        public Builder category(DocumentCategory category) {
            this.category = category;
            return this;
        }

        public Builder type(DocumentType type) {
            this.type = type;
            return this;
        }

        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public Builder year(Year year) {
            this.year = year;
            return this;
        }

        public ListDocumentsArgsDTO build() {
            Optional.ofNullable(owner)
                    .orElseThrow(InsufficientDataException::new);
            Optional.ofNullable(year)
                    .ifPresent((year) -> Optional.ofNullable(category)
                            .orElseThrow(InsufficientDataException::new));
            Optional.ofNullable(type)
                    .ifPresent((type) -> Optional.ofNullable(year)
                            .orElseThrow(InsufficientDataException::new));

            return new ListDocumentsArgsDTO(
                    category,
                    type,
                    owner,
                    year);
        }
    }
}
