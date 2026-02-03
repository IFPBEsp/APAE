
package br.org.apae.api.controllers.patient;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Tags({
    @Tag("controller"),
    @Tag("patient"),
    @Tag("documents")
})
@WebMvcTest(PatientDocumentsControllerImpl.class)
class PatientDocumentsTests {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private DocumentApplicationService documentService;

  @MockitoBean
  private JwtProvider jwtProvider;

  @MockitoBean
  private UserService userService;

  private UUID patientId() {
    return UUID.randomUUID();
  }

  private DocumentDTO document(DocumentCategory category) {
    return new DocumentDTO(
        UUID.randomUUID(),
        "document.pdf",
        category,
        DocumentType.REFERRAL,
        patientId().toString(),
        Year.now());
  }

  static Stream<Arguments> documentsEndpoints() {
    return Stream.of(
        Arguments.of("/patients/{id}/documents/medicals", DocumentCategory.MEDICAL),
        Arguments.of("/patients/{id}/documents/personals", DocumentCategory.PERSONAL),
        Arguments.of("/patients/{id}/documents/schools", DocumentCategory.SCHOOL));
  }

  @Test
  @DisplayName("Should upload document successfully (201)")
  @WithMockUser
  void shouldUploadDocumentSuccessfully() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "document.pdf",
        "application/pdf",
        "content".getBytes());

    mockMvc.perform(
        multipart("/patients/{id}/documents", patientId())
            .file(file)
            .param("category", "MEDICAL")
            .param("type", "REFERRAL")
            .with(csrf()))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("Should fail when category is invalid")
  @WithMockUser
  void shouldFailWhenCategoryIsInvalid() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "document.pdf",
        "application/pdf",
        "content".getBytes());

    mockMvc.perform(
        multipart("/patients/{id}/documents", patientId())
            .file(file)
            .param("category", "INVALID")
            .param("type", "REFERRAL")
            .with(csrf()))
        .andExpect(status().isInternalServerError());
  }

  @Test
  @DisplayName("Should return error when service fails during upload")
  @WithMockUser
  void shouldReturnErrorWhenServiceFails() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "document.pdf",
        "application/pdf",
        "content".getBytes());

    doThrow(new RuntimeException("Service error"))
        .when(documentService)
        .putDocument(any());

    mockMvc.perform(
        multipart("/patients/{id}/documents", patientId())
            .file(file)
            .param("category", "MEDICAL")
            .param("type", "REFERRAL")
            .with(csrf()))
        .andExpect(status().isInternalServerError());
  }

  @ParameterizedTest(name = "Should return documents successfully for endpoint {0}")
  @MethodSource("documentsEndpoints")
  @WithMockUser
  void shouldReturnDocumentsSuccessfully(String endpoint, DocumentCategory category) throws Exception {
    when(documentService.listDocuments(any()))
        .thenReturn(List.of(document(category)));

    when(documentService.getPresignedDocumentUrl(any()))
        .thenReturn("http://presigned-url");

    mockMvc.perform(get(endpoint, patientId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].url").value("http://presigned-url"));
  }

  @ParameterizedTest(name = "Should return error when patient not found for endpoint {0}")
  @MethodSource("documentsEndpoints")
  @WithMockUser
  void shouldReturnErrorWhenPatientNotFound(String endpoint, DocumentCategory category) throws Exception {
    when(documentService.listDocuments(any()))
        .thenThrow(new RuntimeException("Patient not found"));

    mockMvc.perform(get(endpoint, patientId()))
        .andExpect(status().isInternalServerError());
  }

  @ParameterizedTest(name = "Should handle presigned url failure for endpoint {0}")
  @MethodSource("documentsEndpoints")
  @WithMockUser
  void shouldCoverCatchWhenPresignedUrlFails(String endpoint, DocumentCategory category) throws Exception {
    when(documentService.listDocuments(any()))
        .thenReturn(List.of(document(category)));

    when(documentService.getPresignedDocumentUrl(any()))
        .thenThrow(new RuntimeException("MinIO unavailable"));

    mockMvc.perform(get(endpoint, patientId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }
}
