package br.org.apae.api.professional.application.internal;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;
import br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;

@Component
@ConditionalOnProperty(name = "app.professional-photo-metadata-backfill.enabled", havingValue = "true")
class ProfessionalPhotoMetadataBackfill implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessionalPhotoMetadataBackfill.class);

    private final HealthProfessionalRepository repository;
    private final DocumentApplicationService documentService;
    private final Path legacyUploadsDirectory;

    ProfessionalPhotoMetadataBackfill(HealthProfessionalRepository repository,
            DocumentApplicationService documentService,
            @Value("${app.professional-photo-metadata-backfill.legacy-uploads-directory:uploads}")
            String legacyUploadsDirectory) {
        this.repository = repository;
        this.documentService = documentService;
        this.legacyUploadsDirectory = Path.of(legacyUploadsDirectory);
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
            migrateFilesystemPhoto(professional);
        } catch (Exception exception) {
            LOGGER.warn("Não foi possível migrar metadados da foto do profissional {}",
                    professional.getId(), exception);
        }
    }

    private void migrateFilesystemPhoto(HealthProfessional professional) throws Exception {
        if (!professional.getProfilePhoto().startsWith("/uploads/")) {
            return;
        }

        Path legacyPhoto = legacyUploadsDirectory.resolve(
                Path.of(professional.getProfilePhoto()).getFileName()).normalize();
        if (!legacyPhoto.startsWith(legacyUploadsDirectory) || !Files.isRegularFile(legacyPhoto)) {
            LOGGER.warn("Arquivo legado da foto não encontrado para o profissional {}", professional.getId());
            return;
        }

        try (InputStream stream = Files.newInputStream(legacyPhoto)) {
            String contentType = Files.probeContentType(legacyPhoto);
            DocumentDTO document = documentService.putDocument(
                    PutDocumentArgsDTO.builder()
                            .stream(stream)
                            .category(DocumentCategory.PROFESSIONAL)
                            .type(DocumentType.PHOTO)
                            .contentType(contentType != null ? contentType : "application/octet-stream")
                            .owner(professional.getId().toString())
                            .build());

            professional.setProfilePhoto(document.id().toString());
            professional.setProfilePhotoName(document.name());
            professional.setProfilePhotoYear(document.year().getValue());
        }
    }
}
