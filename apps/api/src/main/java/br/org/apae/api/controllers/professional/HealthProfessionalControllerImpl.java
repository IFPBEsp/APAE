package br.org.apae.api.controllers.professional;

import br.org.apae.api.common.dto.patient.response.documents.DocumentWithUrlResponseDTO;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.request.documents.UpdateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.GetPresignedDocumentUrlArgsDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalApplicationService;
import br.org.apae.api.professional.interfaces.controllers.HealthProfessionalController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@RestController
public class HealthProfessionalControllerImpl implements HealthProfessionalController {

    private final HealthProfessionalApplicationService service;
    private final DocumentApplicationService documentService;

    public HealthProfessionalControllerImpl(
            HealthProfessionalApplicationService service,
            DocumentApplicationService documentService
    ) {
        this.service = service;
        this.documentService = documentService;
    }

    @Override
    public ResponseEntity<HealthProfessionalResponseDTO> createHealthProfessional(
            CreateHealthProfessionalDTO dto,
            CreateProfessionalDocumentsDTO documentsDTO,
            MultipartFile profilePhoto
    ) {
        HealthProfessionalResponseDTO createdProfessional =
                this.service.createProfessional(dto, documentsDTO, profilePhoto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfessional);
    }

    @Override
    public ResponseEntity<Page<HealthProfessionalResponseDTO>> getAllHealthProfessional(Boolean ativo, Pageable pageable) {
        return ResponseEntity.ok(this.service.findAllProfessionals(ativo, pageable));
    }

    @Override
    public ResponseEntity<Void> inactivateHealthProfessional(UUID id) {
        service.inactivateProfessional(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> activateHealthProfessional(UUID id) {
        service.activateProfessional(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> reactivateHealthProfessional(UUID id) {
        service.reactivateProfessional(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<HealthProfessionalResponseDTO> findByIdHealthProfessional(UUID id) {
        HealthProfessionalResponseDTO dto = service.findProfessionalById(id);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<HealthProfessionalResponseDTO> updateHealthProfessional(UUID id, UpdateHealthProfessionalDTO dto) {
        return ResponseEntity.ok(this.service.updateProfessional(id, dto));
    }

    @Override
    public ResponseEntity<List<DocumentWithUrlResponseDTO>> getProfessionalDocuments(UUID id) {
        try {
            Iterable<DocumentDTO> documents = this.documentService.listDocuments(
                    ListDocumentsArgsDTO.builder()
                            .owner(id.toString())
                            .category(DocumentCategory.PROFESSIONAL)
                            .build()
            );

            List<DocumentWithUrlResponseDTO> response = StreamSupport
                    .stream(documents.spliterator(), false)
                    .map(this::generatePresignedUrl)
                    .filter(Objects::nonNull)
                    .toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar documentos do profissional", e);
        }
    }

    private DocumentWithUrlResponseDTO generatePresignedUrl(DocumentDTO dto) {
        try {
            String url = this.documentService.getPresignedDocumentUrl(
                    GetPresignedDocumentUrlArgsDTO.builder()
                            .name(dto.name())
                            .owner(dto.owner())
                            .category(dto.category())
                            .type(dto.type())
                            .year(dto.year())
                            .id(dto.id())
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );

            return new DocumentWithUrlResponseDTO(
                    dto.id(),
                    dto.name(),
                    dto.category(),
                    dto.type(),
                    dto.owner(),
                    dto.year(),
                    url
            );
        } catch (Exception e) {
            System.err.println("Falha ao gerar URL para documento: " + dto.name() + " - " + e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<Void> updateProfessionalDocuments(
            UUID id,
            UpdateProfessionalDocumentsDTO documentsDTO
    ) {
        service.updateProfessionalDocuments(id, documentsDTO);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> removeProfessionalDocument(UUID id, UUID documentId) {
        service.removeProfessionalDocument(id, documentId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> uploadProfessionalPhoto(
            UUID id,
            MultipartFile file
    ) {
        service.uploadProfessionalPhoto(id, file);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<String>> getAvailableTimes(UUID id, String date) {
        LocalDate parsedDate = LocalDate.parse(date);

        List<LocalTime> times = service.getAvailableTimes(id, parsedDate);

        List<String> response = times.stream()
                .map(t -> t.toString().substring(0, 5))
                .toList();

        return ResponseEntity.ok(response);
    }
}
