package br.org.apae.api.professional.application.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Year;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;

class ProfessionalPhotoMetadataBackfillTest {

    private final HealthProfessionalRepository repository = mock(HealthProfessionalRepository.class);
    private final DocumentApplicationService documentService = mock(DocumentApplicationService.class);
    private final ProfessionalPhotoMetadataBackfill backfill =
            new ProfessionalPhotoMetadataBackfill(repository, documentService);

    @Test
    void fillsPhotoNameAndYearFromMatchingDocument() throws Exception {
        HealthProfessional professional = legacyProfessional();
        UUID photoId = UUID.fromString(professional.getProfilePhoto());
        when(repository.findAll()).thenReturn(List.of(professional));
        when(documentService.listDocuments(any())).thenReturn(List.of(new DocumentDTO(
                photoId, "legacy-photo.webp", DocumentCategory.PROFESSIONAL,
                DocumentType.PHOTO, professional.getId().toString(), Year.of(2024))));

        backfill.backfill();

        assertEquals("legacy-photo.webp", professional.getProfilePhotoName());
        assertEquals(2024, professional.getProfilePhotoYear());
    }

    @Test
    void keepsPhotoMetadataEmptyWhenMatchingDocumentIsNotFound() throws Exception {
        HealthProfessional professional = legacyProfessional();
        when(repository.findAll()).thenReturn(List.of(professional));
        when(documentService.listDocuments(any())).thenReturn(List.of());

        backfill.backfill();

        assertNull(professional.getProfilePhotoName());
        assertNull(professional.getProfilePhotoYear());
    }

    private HealthProfessional legacyProfessional() {
        HealthProfessional professional = new HealthProfessional(
                UUID.randomUUID(), null, null, null, null, null, null, null);
        professional.setProfilePhoto(UUID.randomUUID().toString());
        return professional;
    }
}
