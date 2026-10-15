package br.org.apae.api.professional.application.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ProfessionalProfilePhotoBackfillTest {
    @Test
    void backfill_storesMetadataForLegacyPhotoAndDoesNotOverwriteCompletedRecordsOnNextRun() throws Exception {
        HealthProfessionalRepository repository = Mockito.mock(HealthProfessionalRepository.class);
        DocumentApplicationService documentService = Mockito.mock(DocumentApplicationService.class);
        ProfessionalProfilePhotoBackfill backfill = new ProfessionalProfilePhotoBackfill();
        UUID professionalId = UUID.randomUUID();
        UUID photoId = UUID.randomUUID();
        HealthProfessional professional = new HealthProfessional(null, null, null);
        professional.setId(professionalId);
        professional.setProfilePhoto(photoId.toString());
        DocumentDTO photo = new DocumentDTO(photoId, "DOCUMENTO_PROFESSIONAL/2025/FOTO/" + photoId,
                DocumentCategory.PROFESSIONAL, DocumentType.PHOTO, professionalId.toString(), Year.of(2025));
        when(repository.findAllMissingProfilePhotoMetadata()).thenReturn(List.of(professional), List.of());
        when(documentService.listDocuments(any())).thenReturn(List.of(photo));

        backfill.backfill(repository, documentService);
        backfill.backfill(repository, documentService);

        assertEquals(photo.name(), professional.getProfilePhotoName());
        assertEquals(2025, professional.getProfilePhotoYear());
        verify(repository).save(professional);
        verify(documentService).listDocuments(any());
    }
}
