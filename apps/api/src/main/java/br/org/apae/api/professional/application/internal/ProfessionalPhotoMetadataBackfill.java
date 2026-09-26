package br.org.apae.api.professional.application.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;

@Component
class ProfessionalPhotoMetadataBackfill {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessionalPhotoMetadataBackfill.class);

    private final HealthProfessionalRepository repository;
    private final DocumentApplicationService documentService;

    ProfessionalPhotoMetadataBackfill(HealthProfessionalRepository repository,
            DocumentApplicationService documentService) {
        this.repository = repository;
        this.documentService = documentService;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void backfill() {
        repository.findAll().stream()
                .filter(this::hasLegacyProfilePhoto)
                .forEach(this::backfillPhotoMetadata);
    }

    private boolean hasLegacyProfilePhoto(HealthProfessional professional) {
        return professional.getProfilePhoto() != null
                && professional.getProfilePhotoName() == null;
    }

    private void backfillPhotoMetadata(HealthProfessional professional) {
        try {
            for (DocumentDTO document : documentService.listDocuments(
                    ListDocumentsArgsDTO.builder()
                            .owner(professional.getId().toString())
                            .category(DocumentCategory.PROFESSIONAL)
                            .build())) {
                if (document.type() == DocumentType.PHOTO
                        && document.id().toString().equals(professional.getProfilePhoto())) {
                    professional.setProfilePhotoName(document.name());
                    professional.setProfilePhotoYear(document.year().getValue());
                    return;
                }
            }
        } catch (Exception exception) {
            LOGGER.warn("Não foi possível migrar metadados da foto do profissional {}",
                    professional.getId(), exception);
        }
    }
}
