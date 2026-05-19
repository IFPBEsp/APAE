package br.org.apae.api.controllers.patient;

import br.org.apae.api.common.dto.patient.response.documents.DocumentWithUrlResponseDTO;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.GetPresignedDocumentUrlArgsDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;
import br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO;
import br.org.apae.api.documents.interfaces.dto.RemoveDocumentArgsDTO;
import br.org.apae.api.patient.interfaces.controllers.PatientDocumentsController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@RestController
public class PatientDocumentsControllerImpl implements PatientDocumentsController {

    private final DocumentApplicationService documentService;

    public PatientDocumentsControllerImpl(DocumentApplicationService documentService) {
        this.documentService = documentService;
    }

    @Override
    public ResponseEntity<DocumentDTO> uploadDocument(UUID id, MultipartFile file, String category, String type, @RequestParam(required = false) Integer year) {
        try {
            DocumentCategory docCategory = DocumentCategory.valueOf(category);
            DocumentType docType = DocumentType.valueOf(type);
            Year docYear = year != null ? Year.of(year) : Year.now();

            PutDocumentArgsDTO args = PutDocumentArgsDTO.builder()
                    .owner(id.toString())
                    .category(docCategory)
                    .type(docType)
                    .year(docYear)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream())
                    .build();

            DocumentDTO documentDTO = this.documentService.putDocument(args);

            return ResponseEntity.status(HttpStatus.CREATED).body(documentDTO);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Categoria ou Tipo de documento inválido: " + category + " / " + type, e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do documento", e);
        }
    }

    @Override
    public ResponseEntity<DocumentWithUrlResponseDTO> replaceDocument(UUID id, UUID documentId, MultipartFile file) {
        try {
            Iterable<DocumentDTO> documents = this.documentService.listDocuments(
                    ListDocumentsArgsDTO.builder()
                            .owner(id.toString())
                            .category(DocumentCategory.MEDICAL)
                            .build()
            );

            DocumentDTO target = null;
            for (DocumentDTO document : documents) {
                if (document != null && Objects.equals(document.id(), documentId)) {
                    target = document;
                    break;
                }
            }

            if (target == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento não encontrado");
            }

            DocumentDTO uploadedDocument = this.documentService.putDocument(
                    PutDocumentArgsDTO.builder()
                            .owner(id.toString())
                            .category(target.category())
                            .type(target.type())
                            .year(target.year())
                            .contentType(file.getContentType())
                            .stream(file.getInputStream())
                            .build()
            );

            try {
                this.documentService.removeDocument(
                        RemoveDocumentArgsDTO.builder()
                                .id(target.id())
                                .owner(target.owner())
                                .category(target.category())
                                .type(target.type())
                                .year(target.year())
                                .build()
                );
            } catch (Exception removalError) {
                try {
                    this.documentService.removeDocument(
                            RemoveDocumentArgsDTO.builder()
                                    .id(uploadedDocument.id())
                                    .owner(uploadedDocument.owner())
                                    .category(uploadedDocument.category())
                                    .type(uploadedDocument.type())
                                    .year(uploadedDocument.year())
                                    .build()
                    );
                } catch (Exception rollbackError) {
                    System.err.println("Falha ao desfazer upload após erro de remoção: " + rollbackError.getMessage());
                }

                throw new RuntimeException("Erro ao substituir o documento", removalError);
            }

            return ResponseEntity.ok(generatePresignedUrl(uploadedDocument));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao substituir o documento", e);
        }
    }

    private DocumentWithUrlResponseDTO generatePresignedUrl(DocumentDTO dto) {
        try {
            String url = this.documentService.getPresignedDocumentUrl(
                    GetPresignedDocumentUrlArgsDTO.builder()
                            .name(dto.name())
                            .owner(dto.owner())
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );

            return new DocumentWithUrlResponseDTO(
                    dto.id(), dto.name(), dto.category(),
                    dto.type(), dto.owner(), dto.year(), url
            );
        } catch (Exception e) {
            System.err.println("Falha ao gerar URL para documento: " + dto.name() + " - " + e.getMessage());
            return null;
        }
    }

    private List<DocumentWithUrlResponseDTO> findDocumentsByCategory(UUID ownerId, DocumentCategory category, Year year) {
        try {
            Iterable<DocumentDTO> documents = this.documentService.listDocuments(
                    ListDocumentsArgsDTO.builder()
                            .owner(ownerId.toString())
                            .category(category)
                            .year(year)
                            .build()
            );

            return StreamSupport.stream(documents.spliterator(), false)
                    .map(this::generatePresignedUrl)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar documentos: " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<List<DocumentWithUrlResponseDTO>> findMedicalDocuments(UUID id, @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(findDocumentsByCategory(id, DocumentCategory.MEDICAL, year != null ? Year.of(year) : null));
    }

    @Override
    public ResponseEntity<List<DocumentWithUrlResponseDTO>> findPersonalDocuments(UUID id, @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(findDocumentsByCategory(id, DocumentCategory.PERSONAL, year != null ? Year.of(year) : null));
    }

    @Override
    public ResponseEntity<List<DocumentWithUrlResponseDTO>> findSchoolDocuments(UUID id, @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(findDocumentsByCategory(id, DocumentCategory.SCHOOL, year != null ? Year.of(year) : null));
    }

    @Override
    public ResponseEntity<DocumentWithUrlResponseDTO> findDocumentByName(UUID id, String documentName) {
        try {

            String url = this.documentService.getPresignedDocumentUrl(
                    GetPresignedDocumentUrlArgsDTO.builder()
                            .name(documentName)
                            .owner(id.toString())
                            .category(DocumentCategory.ABSENCE)
                            .year(Year.now())
                            .type(DocumentType.ATTACHMENTANY)
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );

            return ResponseEntity.ok(
                    new DocumentWithUrlResponseDTO(
                            null,
                            documentName,
                            DocumentCategory.ABSENCE,
                            DocumentType.ATTACHMENTANY,
                            id.toString(),
                            Year.now(),
                            url
                    )
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar URL do documento", e);
        }
    }
}
