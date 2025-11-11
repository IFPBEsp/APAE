package br.org.apae.api.controllers.professional;

import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.GetDocumentArgsDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;
import br.org.apae.api.documents.interfaces.exceptions.InsufficientDataException;
import br.org.apae.api.documents.interfaces.exceptions.InvalidResponseException;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalApplicationService;
import br.org.apae.api.professional.application.internal.ProfessionalDocumentsService;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import br.org.apae.api.professional.interfaces.controllers.HealthProfessionalController;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class HealthProfessionalControllerImpl implements HealthProfessionalController {
    private static final Logger logger = Logger.getLogger(HealthProfessionalControllerImpl.class.getName());
    private final HealthProfessionalApplicationService service;
    private final HealthProfessionalRepository repository;
    private final ProfessionalDocumentsService documentsService;
    private final DocumentApplicationService documentService;

    public HealthProfessionalControllerImpl(
            HealthProfessionalApplicationService service,
            HealthProfessionalRepository repository,
            ProfessionalDocumentsService documentsService,
            DocumentApplicationService documentService) {
        this.service = service;
        this.repository = repository;
        this.documentsService = documentsService;
        this.documentService = documentService;
    }

    @Override
    public ResponseEntity<HealthProfessionalResponseDTO> createHealthProfessional(
            @RequestBody @Valid CreateHealthProfessionalDTO dto) {
        HealthProfessionalResponseDTO createdProfessional = this.service.createProfessional(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfessional);
    }

    @Override
    public ResponseEntity<Page<HealthProfessionalResponseDTO>> getAllHealthProfessional(Pageable pageable) {
        return ResponseEntity.ok(this.service.findAllProfessionals(pageable));
    }

    @Override
    public ResponseEntity<Void> deleteHealthProfessional(UUID id) {
        this.service.deleteProfessional(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<HealthProfessionalResponseDTO> findByIdHealthProfessional(UUID id) {
        HealthProfessionalResponseDTO dto = service.findProfessionalById(id);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<HealthProfessionalResponseDTO> updateHealthProfessional(
            UUID id,
            @RequestBody @Valid UpdateHealthProfessionalDTO dto) {
        return ResponseEntity.ok(this.service.updateProfessional(id, dto));
    }

    @Override
    public ResponseEntity<Void> uploadDocuments(UUID id, @RequestPart("documents") @Valid CreateProfessionalDocumentsDTO documents) {
        logger.info(String.format("Recebida requisição de upload de documentos para profissional ID: %s", id));

        HealthProfessional professional = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warning(String.format("Profissional não encontrado para upload de documentos - ID: %s", id));
                    return new HealthProfessionalNotFoundException();
                });

        try {
            documentsService.storeProfessionalDocuments(professional, documents);
            logger.info(String.format("Upload de documentos concluído com sucesso para profissional ID: %s", id));
            return ResponseEntity.ok().build();
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | InsufficientDataException
                | InvalidResponseException e) {
            logger.log(Level.SEVERE,
                    String.format("Erro ao fazer upload de documentos para profissional ID: %s - Erro: %s",
                            id, e.getMessage()),
                    e);
            throw new RuntimeException("Erro ao processar upload de documentos: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<?> listDocuments(UUID id, Integer year, String type) {
        // garante existência do profissional
        HealthProfessional professional = repository.findById(id)
                .orElseThrow(HealthProfessionalNotFoundException::new);

        try {
            ListDocumentsArgsDTO.Builder builder = ListDocumentsArgsDTO.builder()
                    .owner(professional.getId().toString());

            if (year != null) {
                builder = builder.category(DocumentCategory.PROFESSIONAL)
                        .year(java.time.Year.of(year));
                if (type != null && !type.isBlank()) {
                    builder = builder.type(DocumentType.valueOf(type.toUpperCase()));
                }
            }

            Iterable<DocumentDTO> docs = documentService.listDocuments(builder.build());
            return ResponseEntity.ok(docs);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao listar documentos do profissional", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadDocument(UUID id, String name) {
        HealthProfessional professional = repository.findById(id)
                .orElseThrow(HealthProfessionalNotFoundException::new);

        try {
            var dto = GetDocumentArgsDTO.builder()
                    .name(name)
                    .owner(professional.getId().toString())
                    .build();

            try (var is = documentService.getDocument(dto)) {
                byte[] bytes = is.readAllBytes();
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=\"" + name + "\"")
                        .body(bytes);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao baixar documento do profissional", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
