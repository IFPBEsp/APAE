package br.org.apae.api.professional.application.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(name = "app.professional-photo-metadata-backfill.enabled", havingValue = "true")
class ProfessionalPhotoMetadataBackfill implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessionalPhotoMetadataBackfill.class);

    private final HealthProfessionalRepository repository;
    private final DocumentApplicationService documentService;

    ProfessionalPhotoMetadataBackfill(HealthProfessionalRepository repository,
            DocumentApplicationService documentService) {
        this.repository = repository;
        this.documentService = documentService;
    }

    @Transactional
    public void run(ApplicationArguments args) {
        backfill();
    }

    void backfill() {
        repository.findAll().stream()
                .filter(this::hasLegacyProfilePhoto)
                .forEach(this::backfillPhotoMetadata);
    }

    private boolean hasLegacyProfilePhoto(HealthProfessional professional) {
        return professional.getProfilePhoto() != null
                && (professional.getProfilePhotoName() == null || professional.getProfilePhotoYear() == null);
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
