package br.org.apae.api.controllers.patient;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.exceptions.handler.GlobalExceptionHandler;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.helpers.AuthTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientDocumentsControllerImpl.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({
        SpringDataWebConfiguration.class,
        SecurityConfiguration.class,
        GlobalExceptionHandler.class
})
@Tag("patient")
@Tag("unit")
@Tag("controller")
@Tag("documents")
public class PatientDocumentsControllerImplTest {

    @TestConfiguration
    @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
    static class ContextConfiguration {}

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DocumentApplicationService documentService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    private static final String BASE_URL = "/patients/{id}/documents";

    @BeforeEach
    void setupAuth() {
        AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);
    }

    private DocumentDTO document(UUID patientId, DocumentCategory category) {
        return new DocumentDTO(
                UUID.randomUUID(),
                "document.pdf",
                category,
                DocumentType.REFERRAL,
                patientId.toString(),
                Year.now());
    }

    static Stream<Arguments> documentsEndpoints() {
        return Stream.of(
                Arguments.of("/patients/{id}/documents/medicals", DocumentCategory.MEDICAL),
                Arguments.of("/patients/{id}/documents/personals", DocumentCategory.PERSONAL),
                Arguments.of("/patients/{id}/documents/schools", DocumentCategory.SCHOOL));
    }

    @Test
    @DisplayName("Deve fazer upload do documento com sucesso (201)")
    void shouldUploadDocumentSuccessfully() throws Exception {
        UUID patientId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile(
                "file", "document.pdf", "application/pdf", "content".getBytes());

        mockMvc.perform(
                        multipart(BASE_URL, patientId)
                                .file(file)
                                .param("category", "MEDICAL")
                                .param("type", "REFERRAL")
                                .header("Authorization", AuthTestHelper.bearerToken())
                                .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve falhar ao tentar fazer upload com categoria inválida")
    void shouldFailWhenCategoryIsInvalid() throws Exception {
        UUID patientId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile(
                "file", "document.pdf", "application/pdf", "content".getBytes());

        mockMvc.perform(
                        multipart(BASE_URL, patientId)
                                .file(file)
                                .param("category", "INVALID")
                                .param("type", "REFERRAL")
                                .header("Authorization", AuthTestHelper.bearerToken())
                                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Deve retornar erro quando o serviço falhar durante o upload")
    void shouldReturnErrorWhenServiceFailsDuringUpload() throws Exception {
        UUID patientId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile(
                "file", "document.pdf", "application/pdf", "content".getBytes());

        doThrow(new RuntimeException("Service error"))
                .when(documentService)
                .putDocument(any());

        mockMvc.perform(
                        multipart(BASE_URL, patientId)
                                .file(file)
                                .param("category", "MEDICAL")
                                .param("type", "REFERRAL")
                                .header("Authorization", AuthTestHelper.bearerToken())
                                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @ParameterizedTest(name = "Deve retornar documentos com sucesso para o endpoint {0}")
    @MethodSource("documentsEndpoints")
    void shouldReturnDocumentsSuccessfully(String endpoint, DocumentCategory category) throws Exception {
        UUID patientId = UUID.randomUUID();

        when(documentService.listDocuments(any())).thenReturn(List.of(document(patientId, category)));
        when(documentService.getPresignedDocumentUrl(any())).thenReturn("http://presigned-url");

        mockMvc.perform(get(endpoint, patientId)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].url").value("http://presigned-url"));
    }

    @ParameterizedTest(name = "Deve retornar erro quando o paciente não for encontrado para o endpoint {0}")
    @MethodSource("documentsEndpoints")
    void shouldReturnErrorWhenPatientNotFound(String endpoint, DocumentCategory category) throws Exception {
        UUID patientId = UUID.randomUUID();

        when(documentService.listDocuments(any())).thenThrow(new RuntimeException("Patient not found"));

        mockMvc.perform(get(endpoint, patientId)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isInternalServerError());
    }

    @ParameterizedTest(name = "Deve cobrir a exceção quando a geração da URL pre-assinada falhar para o endpoint {0}")
    @MethodSource("documentsEndpoints")
    void shouldCoverCatchWhenPresignedUrlFails(String endpoint, DocumentCategory category) throws Exception {
        UUID patientId = UUID.randomUUID();

        when(documentService.listDocuments(any())).thenReturn(List.of(document(patientId, category)));
        when(documentService.getPresignedDocumentUrl(any())).thenThrow(new RuntimeException("MinIO unavailable"));

        mockMvc.perform(get(endpoint, patientId)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Deve substituir um documento com sucesso (200)")
    void shouldReplaceDocumentSuccessfully() throws Exception {
        UUID patientId = UUID.randomUUID();
        UUID documentId = UUID.randomUUID();
        DocumentDTO existingDoc = new DocumentDTO(
                documentId, "old-document.pdf", DocumentCategory.MEDICAL,
                DocumentType.REFERRAL, patientId.toString(), Year.now());
        DocumentDTO replacedDoc = new DocumentDTO(
                UUID.randomUUID(), "new-document.pdf", DocumentCategory.MEDICAL,
                DocumentType.REFERRAL, patientId.toString(), Year.now());

        when(documentService.listDocuments(any())).thenReturn(List.of(existingDoc));
        when(documentService.putDocument(any())).thenReturn(replacedDoc);
        doNothing().when(documentService).removeDocument(any());
        when(documentService.getPresignedDocumentUrl(any())).thenReturn("http://presigned-url");

        MockMultipartFile file = new MockMultipartFile(
                "file", "new-document.pdf", "application/pdf", "content".getBytes());

        mockMvc.perform(multipart(BASE_URL + "/{documentId}", patientId, documentId)
                        .file(file)
                        .with(request -> { request.setMethod("PATCH"); return request; })
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("http://presigned-url"));
    }

    @Test
    @DisplayName("Deve retornar 500 ao substituir documento inexistente (ResponseStatusException capturada pelo GlobalExceptionHandler)")
    void shouldReturnErrorWhenReplacingNonExistentDocument() throws Exception {
        UUID patientId = UUID.randomUUID();
        UUID documentId = UUID.randomUUID();

        when(documentService.listDocuments(any())).thenReturn(List.of());

        MockMultipartFile file = new MockMultipartFile(
                "file", "new-document.pdf", "application/pdf", "content".getBytes());

        mockMvc.perform(multipart(BASE_URL + "/{documentId}", patientId, documentId)
                        .file(file)
                        .with(request -> { request.setMethod("PATCH"); return request; })
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Deve buscar documento pelo nome com sucesso (200)")
    void shouldFindDocumentByNameSuccessfully() throws Exception {
        UUID patientId = UUID.randomUUID();
        String documentName = "laudo-medico.pdf";

        when(documentService.getPresignedDocumentUrl(any())).thenReturn("http://presigned-url");

        mockMvc.perform(get(BASE_URL + "/download", patientId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .param("documentName", documentName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(documentName))
                .andExpect(jsonPath("$.url").value("http://presigned-url"));
    }

    @Test
    @DisplayName("Deve retornar erro ao buscar documento quando o serviço falhar")
    void shouldReturnErrorWhenFindByNameServiceFails() throws Exception {
        UUID patientId = UUID.randomUUID();

        when(documentService.getPresignedDocumentUrl(any()))
                .thenThrow(new RuntimeException("MinIO unavailable"));

        mockMvc.perform(get(BASE_URL + "/download", patientId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .param("documentName", "laudo.pdf"))
                .andExpect(status().isInternalServerError());
    }
}
