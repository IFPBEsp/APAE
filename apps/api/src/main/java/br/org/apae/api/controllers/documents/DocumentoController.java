package br.org.apae.api.controllers.documents;

import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.GetPresignedDocumentUrlArgsDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/paciente")
public class DocumentoController {

    private final DocumentApplicationService documentService;

    public DocumentoController(DocumentApplicationService documentService) {
        this.documentService = documentService;
    }

    record DocumentWithUrlDTO(
            UUID id,
            String name,
            DocumentCategory category,
            DocumentType type,
            String owner,
            Year year,
            String url
    ) {}

    private DocumentWithUrlDTO generatePresignedUrl(DocumentDTO dto) {
        try {
            String url = this.documentService.getPresignedDocumentUrl(
                    GetPresignedDocumentUrlArgsDTO.builder()
                            .name(dto.name())
                            .owner(dto.owner())
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );

            return new DocumentWithUrlDTO(
                    dto.id(), dto.name(), dto.category(),
                    dto.type(), dto.owner(), dto.year(), url
            );
        } catch (Exception e) {
            System.err.println("Falha ao gerar URL para documento: " + dto.name() + " - " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/{pacienteId}/documentos")
    public ResponseEntity<List<DocumentWithUrlDTO>> listarDocumentosDoPaciente(

            @PathVariable("pacienteId") String pacienteId,

            @RequestParam(value = "category", required = false) DocumentCategory category,
            @RequestParam(value = "type", required = false) DocumentType type,
            @RequestParam(value = "year", required = false) Year year
    ) throws Exception {

        ListDocumentsArgsDTO.Builder argsBuilder = ListDocumentsArgsDTO.builder()
                .owner(pacienteId)
                .category(category) 
                .type(type) 
                .year(year);

        Iterable<DocumentDTO> documents = this.documentService.listDocuments(argsBuilder.build());

        List<DocumentWithUrlDTO> documentsWithUrls = StreamSupport.stream(documents.spliterator(), false)
                .map(this::generatePresignedUrl) 
                .filter(Objects::nonNull) 
                .toList();
                
        return ResponseEntity.ok(documentsWithUrls);
    }
}