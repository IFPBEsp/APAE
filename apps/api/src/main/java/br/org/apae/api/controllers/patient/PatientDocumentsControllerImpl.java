package br.org.apae.api.controllers.patient;

import br.org.apae.api.common.dto.patient.response.documents.DocumentWithUrlResponseDTO;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.GetPresignedDocumentUrlArgsDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;
import br.org.apae.api.patient.application.interfaces.PatientDocumentsApplicationService;
import br.org.apae.api.patient.interfaces.controllers.PatientDocumentsController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@RestController
public class PatientDocumentsControllerImpl implements PatientDocumentsController{

    private final DocumentApplicationService documentService;

    public PatientDocumentsControllerImpl(DocumentApplicationService documentService) {
        this.documentService = documentService;
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

    private List<DocumentWithUrlResponseDTO> findDocumentsByCategory(UUID ownerId, DocumentCategory category) {
        try {
            Iterable<DocumentDTO> documents = this.documentService.listDocuments(
                    ListDocumentsArgsDTO.builder()
                            .owner(ownerId.toString())
                            .category(category)
                            .build()
            );

            return StreamSupport.stream(documents.spliterator(), false)
                    .map(this::generatePresignedUrl)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar documentos", e);
        }
    }

    @Override
    public ResponseEntity<List<DocumentWithUrlResponseDTO>> findMedicalDocuments(UUID id) {
        return ResponseEntity.ok(findDocumentsByCategory(id, DocumentCategory.MEDICAL));
    }

    @Override
    public ResponseEntity<List<DocumentWithUrlResponseDTO>> findPersonalDocuments(UUID id) {
        return ResponseEntity.ok(findDocumentsByCategory(id, DocumentCategory.PERSONAL));
    }

    @Override
    public ResponseEntity<List<DocumentWithUrlResponseDTO>> findSchoolDocuments(UUID id) {
        return ResponseEntity.ok(findDocumentsByCategory(id, DocumentCategory.SCHOOL));
    }
}
