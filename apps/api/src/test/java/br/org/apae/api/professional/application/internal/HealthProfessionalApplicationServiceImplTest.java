package br.org.apae.api.professional.application.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Year;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;

import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.GetPresignedDocumentUrlArgsDTO;
import br.org.apae.api.documents.interfaces.dto.RemoveDocumentArgsDTO;
import br.org.apae.api.professional.application.mappers.HealthProfessionalMapper;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;

class HealthProfessionalApplicationServiceImplTest {

    private final HealthProfessionalRepository repository = mock(HealthProfessionalRepository.class);
    private final HealthProfessionalMapper mapper = mock(HealthProfessionalMapper.class);
    private final DocumentApplicationService documentService = mock(DocumentApplicationService.class);
    private HealthProfessionalApplicationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new HealthProfessionalApplicationServiceImpl(
                repository,
                mapper,
                mock(ProfessionalDocumentsService.class),
                mock(ServiceAreaApplicationService.class),
                documentService);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void findProfessionalByIdReturnsNullPhotoUrlWhenPhotoNameOrYearIsMissing(boolean missingName) {
        HealthProfessional professional = professionalWithPhoto();
        if (missingName) {
            professional.setProfilePhotoName(null);
        } else {
            professional.setProfilePhotoYear(null);
        }
        when(repository.findById(professional.getId())).thenReturn(Optional.of(professional));
        when(mapper.toResponseDTO(professional)).thenReturn(responseFor(professional));

        HealthProfessionalResponseDTO response = service.findProfessionalById(professional.getId());

        assertNull(response.profilePhotoUrl());
        verify(documentService, never()).getPresignedDocumentUrl(any());
    }

    @Test
    void findProfessionalByIdReturnsPhotoUrlWhenPhotoMetadataIsPresent() throws Exception {
        HealthProfessional professional = professionalWithPhoto();
        when(repository.findById(professional.getId())).thenReturn(Optional.of(professional));
        when(mapper.toResponseDTO(professional)).thenReturn(responseFor(professional));
        when(documentService.getPresignedDocumentUrl(any())).thenReturn("https://minio/photo-url");

        HealthProfessionalResponseDTO response = service.findProfessionalById(professional.getId());

        assertEquals("https://minio/photo-url", response.profilePhotoUrl());
        ArgumentCaptor<GetPresignedDocumentUrlArgsDTO> args = ArgumentCaptor.forClass(GetPresignedDocumentUrlArgsDTO.class);
        verify(documentService).getPresignedDocumentUrl(args.capture());
        assertEquals(UUID.fromString(professional.getProfilePhoto()), args.getValue().id());
        assertEquals(professional.getProfilePhotoName(), args.getValue().name());
        assertEquals(Year.of(professional.getProfilePhotoYear()), args.getValue().year());
    }

    @Test
    void uploadProfessionalPhotoStoresPhotoIdNameAndYear() throws Exception {
        HealthProfessional professional = professionalWithPhoto();
        UUID previousPhotoId = UUID.fromString(professional.getProfilePhoto());
        UUID documentId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "photo.png", "image/png", "image".getBytes());
        when(repository.findById(professional.getId())).thenReturn(Optional.of(professional));
        when(documentService.putDocument(any())).thenReturn(new DocumentDTO(
                documentId, "stored-photo.png", DocumentCategory.PROFESSIONAL,
                DocumentType.PHOTO, professional.getId().toString(), Year.of(2026)));

        service.uploadProfessionalPhoto(professional.getId(), file);

        assertEquals(documentId.toString(), professional.getProfilePhoto());
        assertEquals("stored-photo.png", professional.getProfilePhotoName());
        assertEquals(2026, professional.getProfilePhotoYear());
        verify(repository).save(professional);
        ArgumentCaptor<RemoveDocumentArgsDTO> args = ArgumentCaptor.forClass(RemoveDocumentArgsDTO.class);
        verify(documentService).removeDocument(args.capture());
        assertEquals(previousPhotoId, args.getValue().id());
        assertEquals("photo.png", args.getValue().name());
        assertEquals(Year.of(2025), args.getValue().year());
        assertEquals(DocumentCategory.PROFESSIONAL, args.getValue().category());
        assertEquals(DocumentType.PHOTO, args.getValue().type());
        assertEquals(professional.getId().toString(), args.getValue().owner());
    }

    private HealthProfessional professionalWithPhoto() {
        HealthProfessional professional = new HealthProfessional(
                UUID.randomUUID(), null, null, null, null, null, null, null);
        professional.setProfilePhoto(UUID.randomUUID().toString());
        professional.setProfilePhotoName("photo.png");
        professional.setProfilePhotoYear(2025);
        return professional;
    }

    private HealthProfessionalResponseDTO responseFor(HealthProfessional professional) {
        return new HealthProfessionalResponseDTO(
                professional.getId(), null, null, null, null, null, null, true,
                null, null, null, professional.getProfilePhoto());
    }
}
