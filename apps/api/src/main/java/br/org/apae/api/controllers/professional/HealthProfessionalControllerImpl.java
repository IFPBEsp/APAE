package br.org.apae.api.controllers.professional;

import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.documents.interfaces.exceptions.InsufficientDataException;
import br.org.apae.api.documents.interfaces.exceptions.InvalidResponseException;
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

    public HealthProfessionalControllerImpl(
            HealthProfessionalApplicationService service,
            HealthProfessionalRepository repository,
            ProfessionalDocumentsService documentsService) {
        this.service = service;
        this.repository = repository;
        this.documentsService = documentsService;
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
}
