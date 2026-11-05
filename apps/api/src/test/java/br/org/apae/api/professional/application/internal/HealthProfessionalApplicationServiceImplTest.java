package br.org.apae.api.professional.application.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.org.apae.api.auth.domain.repository.UserRepository;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.professional.application.mappers.HealthProfessionalMapper;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import java.time.Duration;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class HealthProfessionalApplicationServiceImplTest {
    private final HealthProfessionalRepository repository = Mockito.mock(HealthProfessionalRepository.class);
    private final HealthProfessionalMapper mapper = Mockito.mock(HealthProfessionalMapper.class);
    private final DocumentApplicationService documentService = Mockito.mock(DocumentApplicationService.class);
    private final HealthProfessionalApplicationServiceImpl service = new HealthProfessionalApplicationServiceImpl(
            repository, mapper, Mockito.mock(ProfessionalDocumentsService.class),
            Mockito.mock(ServiceAreaApplicationService.class), Mockito.mock(UserRepository.class), documentService);

    @Test
    void findAllProfessionals_generatesPhotoUrlFromPersistedMetadataWithoutListingDocuments() throws Exception {
        UUID professionalId = UUID.randomUUID();
        UUID photoId = UUID.randomUUID();
        HealthProfessional professional = new HealthProfessional(null, null, null);
        professional.setId(professionalId);
        professional.setProfilePhotoMetadata(photoId.toString(), "DOCUMENTO_PROFESSIONAL/2026/FOTO/" + photoId, Year.of(2026));
        HealthProfessionalResponseDTO response = response(photoId.toString());

        when(repository.findAll((Pageable) any())).thenReturn(new PageImpl<>(List.of(professional), PageRequest.of(0, 10), 1));
        when(mapper.toResponseDTO(professional)).thenReturn(response);
        DocumentDTO photo = new DocumentDTO(photoId, professional.getProfilePhotoName(), DocumentCategory.PROFESSIONAL,
                DocumentType.PHOTO, professionalId.toString(), Year.of(2026));
        when(documentService.generatePresignedUrl(eq(photo), any(Duration.class))).thenReturn(
                new br.org.apae.api.common.dto.patient.response.documents.DocumentWithUrlResponseDTO(
                        photoId, photo.name(), photo.category(), photo.type(), photo.owner(), photo.year(), "https://minio/photo"));

        HealthProfessionalResponseDTO result = service.findAllProfessionals(null, PageRequest.of(0, 10)).getContent().getFirst();

        assertEquals(photoId.toString(), result.profilePhoto());
        assertEquals("https://minio/photo", result.profilePhotoUrl());
        verify(documentService, never()).listDocuments(any());
    }

    @Test
    void findAllProfessionals_returnsNullUrlWhenProfessionalHasNoPhotoMetadata() throws Exception {
        HealthProfessional professional = new HealthProfessional(null, null, null);
        professional.setId(UUID.randomUUID());
        HealthProfessionalResponseDTO response = response(null);
        when(repository.findAll((Pageable) any())).thenReturn(new PageImpl<>(List.of(professional)));
        when(mapper.toResponseDTO(professional)).thenReturn(response);

        HealthProfessionalResponseDTO result = service.findAllProfessionals(null, PageRequest.of(0, 10)).getContent().getFirst();

        assertNull(result.profilePhotoUrl());
        verify(documentService, never()).generatePresignedUrl(any(), any());
        verify(documentService, never()).listDocuments(any());
    }

    @Test
    void uploadProfessionalPhoto_persistsDocumentMetadataOnlyAfterUploadSucceeds() throws Exception {
        UUID professionalId = UUID.randomUUID();
        UUID photoId = UUID.randomUUID();
        HealthProfessional professional = new HealthProfessional(null, null, null);
        professional.setId(professionalId);
        DocumentDTO storedPhoto = new DocumentDTO(photoId, "DOCUMENTO_PROFESSIONAL/2026/FOTO/" + photoId,
                DocumentCategory.PROFESSIONAL, DocumentType.PHOTO, professionalId.toString(), Year.of(2026));
        when(repository.findById(professionalId)).thenReturn(java.util.Optional.of(professional));
        when(documentService.putDocument(any())).thenReturn(storedPhoto);

        service.uploadProfessionalPhoto(professionalId,
                new MockMultipartFile("file", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "image".getBytes()));

        assertEquals(photoId.toString(), professional.getProfilePhoto());
        assertEquals(storedPhoto.name(), professional.getProfilePhotoName());
        assertEquals(2026, professional.getProfilePhotoYear());
        verify(repository).save(professional);
    }

    private HealthProfessionalResponseDTO response(String photoId) {
        return new HealthProfessionalResponseDTO(null, null, "Profissional", null, null, null, null,
                null, null, true, null, null, List.of(), photoId, null);
    }
}
