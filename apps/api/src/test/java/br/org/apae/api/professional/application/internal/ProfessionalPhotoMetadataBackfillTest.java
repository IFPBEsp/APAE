package br.org.apae.api.professional.application.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Year;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;

import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;

class ProfessionalPhotoMetadataBackfillTest {

    private final HealthProfessionalRepository repository = mock(HealthProfessionalRepository.class);
    private final DocumentApplicationService documentService = mock(DocumentApplicationService.class);

    @TempDir
    Path uploadsDirectory;

    private ProfessionalPhotoMetadataBackfill backfill() {
        return new ProfessionalPhotoMetadataBackfill(repository, documentService, uploadsDirectory.toString());
    }

    @Test
    void fillsPhotoNameAndYearFromMatchingDocument() throws Exception {
        HealthProfessional professional = legacyProfessional();
        UUID photoId = UUID.fromString(professional.getProfilePhoto());
        when(repository.findAll()).thenReturn(List.of(professional));
        when(documentService.listDocuments(any())).thenReturn(List.of(new DocumentDTO(
                photoId, "legacy-photo.webp", DocumentCategory.PROFESSIONAL,
                DocumentType.PHOTO, professional.getId().toString(), Year.of(2024))));

        backfill().backfill();

        assertEquals("legacy-photo.webp", professional.getProfilePhotoName());
        assertEquals(2024, professional.getProfilePhotoYear());
    }

    @Test
    void keepsPhotoMetadataEmptyWhenMatchingDocumentIsNotFound() throws Exception {
        HealthProfessional professional = legacyProfessional();
        when(repository.findAll()).thenReturn(List.of(professional));
        when(documentService.listDocuments(any())).thenReturn(List.of());

        backfill().backfill();

        assertNull(professional.getProfilePhotoName());
        assertNull(professional.getProfilePhotoYear());
    }

    @Test
    void migratesFilesystemBackedPhotoToMinio() throws Exception {
        HealthProfessional professional = legacyProfessional();
        professional.setProfilePhoto("/uploads/legacy-photo.png");
        Files.writeString(uploadsDirectory.resolve("legacy-photo.png"), "image");
        UUID documentId = UUID.randomUUID();

        when(repository.findAll()).thenReturn(List.of(professional));
        when(documentService.listDocuments(any())).thenReturn(List.of());
        when(documentService.putDocument(any())).thenReturn(new DocumentDTO(
                documentId, "stored-photo.png", DocumentCategory.PROFESSIONAL,
                DocumentType.PHOTO, professional.getId().toString(), Year.of(2026)));

        backfill().backfill();

        assertEquals(documentId.toString(), professional.getProfilePhoto());
        assertEquals("stored-photo.png", professional.getProfilePhotoName());
        assertEquals(2026, professional.getProfilePhotoYear());
        ArgumentCaptor<PutDocumentArgsDTO> args = ArgumentCaptor.forClass(PutDocumentArgsDTO.class);
        verify(documentService).putDocument(args.capture());
        assertEquals(DocumentCategory.PROFESSIONAL, args.getValue().category());
        assertEquals(DocumentType.PHOTO, args.getValue().type());
        assertEquals(professional.getId().toString(), args.getValue().owner());
    }

    private HealthProfessional legacyProfessional() {
        HealthProfessional professional = new HealthProfessional(
                UUID.randomUUID(), null, null, null, null, null, null, null);
        professional.setProfilePhoto(UUID.randomUUID().toString());
        return professional;
    }
}
