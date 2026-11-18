
package br.org.apae.api.patient.application.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Year;
import java.util.List;
import java.util.UUID;

import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.GetPresignedDocumentUrlArgsDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientDocumentsServiceTest {

    @Mock
    private DocumentApplicationService documentService;

    @InjectMocks
    private PatientDocumentsService patientDocumentsService;

    private UUID patientId;

    @BeforeEach
    void setUp() {
        patientId = UUID.randomUUID();
    }

    @Test
    void shouldReturnNullWhenPatientHasNoPhoto() throws Exception {
        when(documentService.listDocuments(any(ListDocumentsArgsDTO.class)))
                .thenReturn(List.of());

        String result = patientDocumentsService.getPatientPhoto(patientId);

        assertNull(result);

        verify(documentService).listDocuments(any(ListDocumentsArgsDTO.class));
        verify(documentService, never())
                .getPresignedDocumentUrl(any(GetPresignedDocumentUrlArgsDTO.class));
    }

    
    @Test
    void shouldReturnPhotoUrlWhenPatientHasPhoto() throws Exception {
        DocumentDTO photo = new DocumentDTO(
                UUID.randomUUID(),
                "foto.png",
                DocumentCategory.PERSONAL,
                DocumentType.PHOTO,
                patientId.toString(),
                Year.now()
        );

        when(documentService.listDocuments(any(ListDocumentsArgsDTO.class)))
                .thenReturn(List.of(photo));

        when(documentService.getPresignedDocumentUrl(
                any(GetPresignedDocumentUrlArgsDTO.class)))
                .thenReturn("https://storage.example.com/foto.png");

        String result = patientDocumentsService.getPatientPhoto(patientId);

        assertEquals("https://storage.example.com/foto.png", result);

        verify(documentService).getPresignedDocumentUrl(
                org.mockito.ArgumentMatchers.argThat(args ->
                        args.owner().equals(patientId.toString())
                                && args.name().equals("foto.png")
                                && args.expiry() == 3600
                )
        );
    }


    @Test
    void shouldReturnNullWhenSearchingPhotoFails() throws Exception {
        when(documentService.listDocuments(any(ListDocumentsArgsDTO.class)))
                .thenThrow(new RuntimeException("Erro na busca"));

        String result = patientDocumentsService.getPatientPhoto(patientId);

        assertNull(result);

        verify(documentService, never())
                .getPresignedDocumentUrl(any(GetPresignedDocumentUrlArgsDTO.class));
    }

    @Test
    void shouldReturnNullWhenGeneratingPhotoUrlFails() throws Exception {
        DocumentDTO photo = new DocumentDTO(
                UUID.randomUUID(),
                "foto.png",
                DocumentCategory.PERSONAL,
                DocumentType.PHOTO,
                patientId.toString(),
                Year.now()
        );

        when(documentService.listDocuments(any(ListDocumentsArgsDTO.class)))
                .thenReturn(List.of(photo));

        when(documentService.getPresignedDocumentUrl(
                any(GetPresignedDocumentUrlArgsDTO.class)))
                .thenThrow(new RuntimeException("Erro ao gerar URL"));

        String result = patientDocumentsService.getPatientPhoto(patientId);

        assertNull(result);

        verify(documentService).getPresignedDocumentUrl(
                any(GetPresignedDocumentUrlArgsDTO.class));
    }
}
