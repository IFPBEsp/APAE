package br.org.apae.api.professional.application.internal;

import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class ProfessionalProfilePhotoBackfill {
    private static final Logger log = LoggerFactory.getLogger(ProfessionalProfilePhotoBackfill.class);

    @Bean
    @ConditionalOnProperty(name = "apae.profile-photo-backfill.enabled", havingValue = "true")
    ApplicationRunner professionalProfilePhotoBackfillRunner(HealthProfessionalRepository repository,
            DocumentApplicationService documentService) {
        return args -> backfill(repository, documentService);
    }

    @Transactional
    void backfill(HealthProfessionalRepository repository, DocumentApplicationService documentService) {
        for (HealthProfessional professional : repository.findAllMissingProfilePhotoMetadata()) {
            try {
                UUID photoId = UUID.fromString(professional.getProfilePhoto());
                DocumentDTO photo = findPhoto(documentService, professional, photoId);
                if (photo == null) {
                    log.warn("Foto legada {} não encontrada para profissional {}", photoId, professional.getId());
                    continue;
                }
                professional.setProfilePhotoMetadata(photo.id().toString(), photo.name(), photo.year());
                repository.save(professional);
            } catch (Exception exception) {
                log.error("Falha ao recuperar metadados da foto do profissional {}", professional.getId(), exception);
            }
        }
    }

    private DocumentDTO findPhoto(DocumentApplicationService documentService, HealthProfessional professional, UUID photoId)
            throws Exception {
        Iterable<DocumentDTO> documents = documentService.listDocuments(ListDocumentsArgsDTO.builder()
                .owner(professional.getId().toString())
                .category(DocumentCategory.PROFESSIONAL)
                .build());
        for (DocumentDTO document : documents) {
            if (document != null && document.type() == DocumentType.PHOTO && photoId.equals(document.id())) {
                return document;
            }
        }
        return null;
    }
}
