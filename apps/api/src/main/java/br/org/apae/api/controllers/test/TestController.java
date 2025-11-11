
package br.org.apae.api.controllers.test;

import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.GetPresignedDocumentUrlArgsDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;

import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

record DocumentWithUrlDTO(
    UUID id,
    String name,
    DocumentCategory category,
    DocumentType type,
    String owner,
    Year year,
    String url) {
}

@RestController
@RequestMapping("/test")
public class TestController {
  private final DocumentApplicationService service;

  public TestController(DocumentApplicationService service) {
    this.service = service;
  }

  private DocumentWithUrlDTO withUrl(DocumentDTO dto) {
    try {
      String url = this.service.getPresignedDocumentUrl(
          GetPresignedDocumentUrlArgsDTO.builder()
              .name(dto.name())
              .owner(dto.owner())
              .expiry(1, TimeUnit.HOURS)
              .build());
      return new DocumentWithUrlDTO(
          dto.id(), dto.name(), dto.category(),
          dto.type(), dto.owner(), dto.year(),
          url);
    } catch (Exception e) {
      return null;
    }
  }

  @GetMapping
  ResponseEntity<List<DocumentWithUrlDTO>> list() throws Exception {
    var documents = this.service.listDocuments(
        ListDocumentsArgsDTO.builder()
            .owner("e973fc06-9cbc-4cf4-b0f7-9628d655b7fd")
            .category(DocumentCategory.PERSONAL).build());

    return ResponseEntity.ok(
        StreamSupport.stream(documents.spliterator(), false)
            .map(this::withUrl)
            .toList());
  }
}
