package br.org.apae.api.documents.interfaces.dto;

import java.io.InputStream;
import java.time.Year;
import java.util.Optional;

import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;

import br.org.apae.api.documents.interfaces.exceptions.InsufficientDataException;

public record PutDocumentArgsDTO(
        InputStream stream,
        DocumentCategory category,
        DocumentType type,
        String contentType,
        String owner,
        Year year) {

    public static PutDocumentArgsDTO.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private InputStream stream;
        private DocumentCategory category;
        private DocumentType type;
        private String contentType;
        private String owner;
        private Year year;

        public Builder stream(InputStream stream) {
            this.stream = stream;
            return this;
        }

        public Builder category(DocumentCategory category) {
            this.category = category;
            return this;
        }

        public Builder type(DocumentType type) {
            this.type = type;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
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

        public PutDocumentArgsDTO build() {
            return new PutDocumentArgsDTO(
                    Optional.ofNullable(stream).orElseThrow(InsufficientDataException::new),
                    Optional.ofNullable(category).orElseThrow(InsufficientDataException::new),
                    Optional.ofNullable(type).orElseThrow(InsufficientDataException::new),
                    Optional.ofNullable(contentType).orElseThrow(InsufficientDataException::new),
                    Optional.ofNullable(owner).orElseThrow(InsufficientDataException::new),
                    Optional.ofNullable(year).orElse(Year.now()));
        }
    }
}
