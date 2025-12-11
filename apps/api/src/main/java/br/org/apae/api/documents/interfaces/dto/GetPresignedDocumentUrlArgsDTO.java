
package br.org.apae.api.documents.interfaces.dto;

import java.time.Year;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import br.org.apae.api.documents.domain.builders.DocumentReferenceBuilder;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.exceptions.InsufficientDataException;

public record GetPresignedDocumentUrlArgsDTO(UUID id,
    String name,
    DocumentCategory category,
    DocumentType type,
    String owner,
    Year year,
    int expiry) {

  public static GetPresignedDocumentUrlArgsDTO.Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private UUID id;
    private String name;
    private DocumentCategory category;
    private DocumentType type;
    private String owner;
    private Year year;
    private int expiry;

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
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

    public Builder owner(String owner) {
      this.owner = owner;
      return this;
    }

    public Builder year(Year year) {
      this.year = year;
      return this;
    }

    public Builder expiry(int duration, TimeUnit unit) {
      this.expiry = (int) unit.toSeconds(duration);
      return this;
    }

    public GetPresignedDocumentUrlArgsDTO build() {
      String presentName = Optional.ofNullable(this.name)
          .orElseGet(() -> DocumentReferenceBuilder.buildDocumentName(
              Optional.ofNullable(category).orElseThrow(InsufficientDataException::new),
              Optional.ofNullable(type).orElseThrow(InsufficientDataException::new),
              Optional.ofNullable(year).orElseThrow(InsufficientDataException::new),
              Optional.ofNullable(id).orElseThrow(InsufficientDataException::new)));
      String presentOwner = Optional.ofNullable(this.owner)
          .orElseThrow(InsufficientDataException::new);
      int presentExpiry = Optional.ofNullable(this.expiry)
          .orElseThrow(InsufficientDataException::new);

      return new GetPresignedDocumentUrlArgsDTO(
          id, presentName, category,
          type, presentOwner, year,
          presentExpiry);
    }
  }
}
