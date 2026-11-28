package br.org.apae.api.controllers.professional;
import org.springframework.web.multipart.MultipartFile;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.RestAccessDeniedHandler;
import br.org.apae.api.auth.infrastructure.security.RestAuthenticationEntryPoint;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.request.documents.UpdateProfessionalDocumentsDTO;
import br.org.apae.api.mocks.professional.HealthProfessionalMockDto;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalApplicationService;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.common.dto.patient.response.documents.DocumentWithUrlResponseDTO;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.domain.enums.DocumentCategory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Tag("controller")
@Tag("health-professional")
@WebMvcTest(controllers = HealthProfessionalControllerImpl.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({
 SpringDataWebConfiguration.class,
 SecurityConfiguration.class
})
class HealthProfessionalControllerTest {

 private static final String DOCX_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
 private static final String INVALID_DOCUMENT_MESSAGE =
         "Formato de arquivo 'text/plain' não permitido. Formatos permitidos: PDF, JPG, PNG, DOCX, WEBP";
 private static final int OVERSIZED_FILE_BYTES = 10 * 1024 * 1024 + 1;

 @Autowired
 private MockMvc mockMvc;

 @Autowired
 private ObjectMapper objectMapper;

 @MockitoBean
 private HealthProfessionalApplicationService service;

 @MockitoBean
 private JwtProvider jwtProvider;
 
 @MockitoBean
 private UserService userService;
 
 @MockitoBean
 private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
 
 @MockitoBean
 private RestAccessDeniedHandler restAccessDeniedHandler;
 
 @MockitoBean
 private DocumentApplicationService documentApplicationService;

 @BeforeEach
 void setupAuth() {
  AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);
 }

 @AfterEach
 void tearDown() {
  Mockito.reset(service, documentApplicationService, jwtProvider, userService);
 }

 static Stream<Arguments> allowedDocumentFormats() {
  return Stream.of(
          Arguments.of("document.pdf", MediaType.APPLICATION_PDF_VALUE),
          Arguments.of("document.docx", DOCX_MEDIA_TYPE),
          Arguments.of("document.jpg", MediaType.IMAGE_JPEG_VALUE),
          Arguments.of("document.jpeg", MediaType.IMAGE_JPEG_VALUE),
          Arguments.of("document.png", MediaType.IMAGE_PNG_VALUE),
          Arguments.of("document.webp", "image/webp")
  );
 }

 @Nested
 @DisplayName("Cenários de Criação (POST)")
 class Criacao {
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  @DisplayName("Deve criar profissional sem foto e com anexo omitido ou vazio (201)")
  void shouldCreateProfessionalSuccessfully(boolean includeEmptyAttachment) throws Exception {
   Mockito.when(service.createProfessional(
           any(CreateHealthProfessionalDTO.class),
           any(CreateProfessionalDocumentsDTO.class),
           isNull()
   )).thenReturn(HealthProfessionalMockDto.createProfessionalResponse1());

   var request = createProfessionalRequest()
           .file(HealthProfessionalMockDto.volunteerAgreementFile())
           .file(HealthProfessionalMockDto.curriculumFile());
   if (includeEmptyAttachment) {
    request.file(new MockMultipartFile("attachmentAny", new byte[0]));
   }

   mockMvc.perform(request).andExpect(status().isCreated());

   Mockito.verify(service).createProfessional(any(), any(), isNull());
  }

  @Test
  @DisplayName("Deve criar profissional sem RG com sucesso (201)")
  void shouldCreateProfessionalWithoutIdentityDocumentSuccessfully() throws Exception {
   var base = HealthProfessionalMockDto.createHealthProfessionalRequest();
   var request = new CreateHealthProfessionalDTO(
           base.serviceArea(),
           base.phoneNumber(),
           base.professionalDocument(),
           base.email(),
           base.cpf(),
           base.name(),
           null,
           base.address(),
           base.availabilities(),
           base.profilePhoto()
   );
   var response = HealthProfessionalMockDto.createProfessionalResponse1();

   Mockito.when(service.createProfessional(
           any(CreateHealthProfessionalDTO.class),
           any(CreateProfessionalDocumentsDTO.class),
           any(MultipartFile.class)
   )).thenReturn(response);

   var professionalPart = new MockMultipartFile("professional", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));

   mockMvc.perform(multipart("/professionals")
                   .file(professionalPart)
                   .file(HealthProfessionalMockDto.volunteerAgreementFile())
                   .file(HealthProfessionalMockDto.curriculumFile())
                   .file(HealthProfessionalMockDto.attachmentAnyFile())
                   .header("Authorization", AuthTestHelper.bearerToken())
                   .contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isCreated());

   Mockito.verify(service).createProfessional(any(), any(), any());
  }

  @Test
  @DisplayName("Deve criar profissional com RG em branco convertido para null com sucesso (201)")
  void shouldCreateProfessionalWithBlankIdentityDocumentAsNull() throws Exception {
   var base = HealthProfessionalMockDto.createHealthProfessionalRequest();
   var request = new CreateHealthProfessionalDTO(
           base.serviceArea(),
           base.phoneNumber(),
           base.professionalDocument(),
           base.email(),
           base.cpf(),
           base.name(),
           "",
           base.address(),
           base.availabilities(),
           base.profilePhoto()
   );
   var response = HealthProfessionalMockDto.createProfessionalResponse1();

   Mockito.when(service.createProfessional(
           any(CreateHealthProfessionalDTO.class),
           any(CreateProfessionalDocumentsDTO.class),
           any(MultipartFile.class)
   )).thenReturn(response);

   var professionalPart = new MockMultipartFile("professional", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));

   mockMvc.perform(multipart("/professionals")
                   .file(professionalPart)
                   .file(HealthProfessionalMockDto.volunteerAgreementFile())
                   .file(HealthProfessionalMockDto.curriculumFile())
                   .file(HealthProfessionalMockDto.attachmentAnyFile())
                   .header("Authorization", AuthTestHelper.bearerToken())
                   .contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isCreated());

   ArgumentCaptor<CreateHealthProfessionalDTO> captor = ArgumentCaptor.forClass(CreateHealthProfessionalDTO.class);
   Mockito.verify(service).createProfessional(captor.capture(), any(), any());
   assertNull(captor.getValue().identityDocument());
  }

  @Test
  @DisplayName("Deve retornar 400 ao cadastrar profissional com CPF inválido")
  void shouldReturnBadRequestWhenCpfIsInvalid() throws Exception {
   var request = HealthProfessionalMockDto.createHealthProfessionalRequestWithInvalidCpf();

   var professionalPart = new MockMultipartFile("professional", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));

   mockMvc.perform(multipart("/professionals")
                   .file(professionalPart)
                   .file(HealthProfessionalMockDto.volunteerAgreementFile())
                   .file(HealthProfessionalMockDto.curriculumFile())
                   .file(HealthProfessionalMockDto.attachmentAnyFile())
                   .header("Authorization", AuthTestHelper.bearerToken())
                   .contentType(MediaType.MULTIPART_FORM_DATA))
           .andExpect(status().isBadRequest()).andExpect(jsonPath("$.fields[0].field").value("cpf"));

   Mockito.verify(service, Mockito.never()).createProfessional(any(), any(), any());
  }

  @ParameterizedTest
  @CsvSource({"profile.png, image/png", "profile.jpg, image/jpeg", "profile.jpeg, image/jpeg", "profile.webp, image/webp"})
  @DisplayName("Deve criar profissional com foto com sucesso (201)")
  void shouldCreateProfessionalWithPhotoSuccessfully(String filename, String contentType) throws Exception {
   var response = HealthProfessionalMockDto.createProfessionalResponse1();

   Mockito.when(service.createProfessional(
           any(CreateHealthProfessionalDTO.class),
           any(CreateProfessionalDocumentsDTO.class),
           any(MultipartFile.class)
   )).thenReturn(response);

   var photo = new MockMultipartFile("profilePhoto", filename, contentType, new byte[]{1});
   mockMvc.perform(createProfessionalRequest()
                   .file(HealthProfessionalMockDto.volunteerAgreementFile())
                   .file(HealthProfessionalMockDto.curriculumFile())
                   .file(HealthProfessionalMockDto.attachmentAnyFile())
                   .file(photo))
           .andExpect(status().isCreated());

   Mockito.verify(service).createProfessional(any(), any(), Mockito.same(photo));
  }

  @ParameterizedTest
  @MethodSource("br.org.apae.api.controllers.professional.HealthProfessionalControllerTest#allowedDocumentFormats")
  @DisplayName("Deve aceitar os formatos permitidos nos documentos do cadastro (201)")
  void shouldAcceptAllowedDocumentFormats(String filename, String contentType) throws Exception {
   Mockito.when(service.createProfessional(any(), any(), isNull()))
           .thenReturn(HealthProfessionalMockDto.createProfessionalResponse1());

   mockMvc.perform(createProfessionalRequest()
                   .file(new MockMultipartFile("volunteerAgreement", filename, contentType, new byte[]{1}))
                   .file(new MockMultipartFile("curriculum", filename, contentType, new byte[]{1}))
                   .file(new MockMultipartFile("attachmentAny", filename, contentType, new byte[]{1})))
           .andExpect(status().isCreated());
  }

  @ParameterizedTest
  @ValueSource(strings = {"volunteerAgreement", "curriculum", "attachmentAny"})
  @DisplayName("Deve recusar TXT nos documentos do cadastro (400)")
  void shouldRejectInvalidDocumentFormat(String field) throws Exception {
   var invalidFile = new MockMultipartFile(field, "document.txt", MediaType.TEXT_PLAIN_VALUE, new byte[]{1});
   var request = createProfessionalRequest().file(invalidFile);
   if (!field.equals("volunteerAgreement")) {
    request.file(HealthProfessionalMockDto.volunteerAgreementFile());
   }
   if (!field.equals("curriculum")) {
    request.file(HealthProfessionalMockDto.curriculumFile());
   }

   mockMvc.perform(request)
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.fields[0].field").value(field))
           .andExpect(jsonPath("$.fields[0].message").value(INVALID_DOCUMENT_MESSAGE));
   Mockito.verifyNoInteractions(service);
  }

  @ParameterizedTest
  @CsvSource({"profile.pdf, application/pdf", "profile.docx, " + DOCX_MEDIA_TYPE})
  @DisplayName("Deve recusar PDF e DOCX como foto no cadastro (400)")
  void shouldRejectNonImageProfilePhoto(String filename, String contentType) throws Exception {
   var photo = new MockMultipartFile("profilePhoto", filename, contentType, new byte[]{1});

   mockMvc.perform(createProfessionalRequest()
                   .file(HealthProfessionalMockDto.volunteerAgreementFile())
                   .file(HealthProfessionalMockDto.curriculumFile())
                   .file(photo))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.fields[0].field").value("profilePhoto"))
           .andExpect(jsonPath("$.fields[0].message").value(
                   "Formato de arquivo '%s' não permitido. Formatos permitidos: PNG, JPG, JPEG, WEBP".formatted(contentType)));
   Mockito.verifyNoInteractions(service);
  }

  @Test
  @DisplayName("Deve recusar documento acima de 10 MB no cadastro (400)")
  void shouldRejectOversizedDocument() throws Exception {
   var curriculum = new MockMultipartFile("curriculum", "large.pdf", MediaType.APPLICATION_PDF_VALUE,
           new byte[OVERSIZED_FILE_BYTES]);

   mockMvc.perform(createProfessionalRequest()
                   .file(HealthProfessionalMockDto.volunteerAgreementFile())
                   .file(curriculum))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.fields[0].field").value("curriculum"))
           .andExpect(jsonPath("$.fields[0].message").value(containsString("excede o limite máximo")));
   Mockito.verifyNoInteractions(service);
  }

  private MockMultipartHttpServletRequestBuilder createProfessionalRequest() throws Exception {
   var professionalPart = new MockMultipartFile("professional", "", MediaType.APPLICATION_JSON_VALUE,
           objectMapper.writeValueAsBytes(HealthProfessionalMockDto.createHealthProfessionalRequest()));
   var request = multipart("/professionals").file(professionalPart);
   request.header("Authorization", AuthTestHelper.bearerToken());
   return request;
  }
 }

 @Nested
 @DisplayName("Cenários de Busca e Listagem (GET)")
 class BuscasEListagens {
  @Test
  @DisplayName("Deve listar todos os profissionais paginado (200)")
  void shouldReturnAllProfessionals() throws Exception {
   var responses = HealthProfessionalMockDto.createProfessionalResponseList();
   var page = new PageImpl<>(responses, PageRequest.of(0, 10), responses.size());

   Mockito.when(service.findAllProfessionals(isNull(), any(Pageable.class))).thenReturn(page);

   mockMvc.perform(get("/professionals").param("Ativo", "true").header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.content.length()").value(2))
     .andExpect(jsonPath("$.content[0].id").value(HealthProfessionalMockDto.PROFESSIONAL_ID_1.toString()));
  }

  @Test
  @DisplayName("Deve buscar profissional por ID com sucesso (200)")
  void shouldFindProfessionalByIdSuccessfully() throws Exception {
   UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
   var response = HealthProfessionalMockDto.createProfessionalResponse1();

   Mockito.when(service.findProfessionalById(id)).thenReturn(response);

   mockMvc.perform(get("/professionals/{id}", id).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.id").value(id.toString()));
  }

  @Test
  @DisplayName("Deve listar horários disponíveis do profissional no formato HH:mm (200)")
  void shouldGetAvailableTimesSuccessfully() throws Exception {
   UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
   String dateString = "2026-05-18";
   List<LocalTime> mockTimes = List.of(LocalTime.of(9, 0), LocalTime.of(10, 30));

   Mockito.when(service.getAvailableTimes(id, LocalDate.parse(dateString))).thenReturn(mockTimes);

   mockMvc.perform(get("/professionals/{id}/available-times", id).param("date", dateString).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.length()").value(2))
     .andExpect(jsonPath("$[0]").value("09:00"));
  }

  @Test
  @DisplayName("Deve retornar 404 ao buscar profissional inexistente")
  void shouldReturnNotFoundWhenProfessionalDoesNotExist() throws Exception {
   UUID id = UUID.randomUUID();
   Mockito.when(service.findProfessionalById(id)).thenThrow(new HealthProfessionalNotFoundException());
   mockMvc.perform(get("/professionals/{id}", id).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isNotFound());
  }
 }

 @Nested
 @DisplayName("Cenários de Atualização de Dados e Ações de Status (PUT / PATCH)")
 class AtualizacoesEStatus {
  @Test
  @DisplayName("Deve atualizar profissional com sucesso (200)")
  void shouldUpdateProfessionalSuccessfully() throws Exception {
   UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
   var response = HealthProfessionalMockDto.createProfessionalResponse1();
   var updateRequest = HealthProfessionalMockDto.createHealthProfessionalRequest();

   Mockito.when(service.updateProfessional(Mockito.eq(id), any(UpdateHealthProfessionalDTO.class))).thenReturn(response);

   mockMvc.perform(put("/professionals/{id}", id).header("Authorization", AuthTestHelper.bearerToken())
       .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(updateRequest)))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.id").value(id.toString()));
  }

  @Test
  @DisplayName("Deve inativar profissional com sucesso (204) usando PUT")
  void shouldInactivateProfessionalSuccessfully() throws Exception {
   UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
   Mockito.doNothing().when(service).inactivateProfessional(id);
   mockMvc.perform(put("/professionals/{id}/inactivate", id).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Deve ativar profissional com sucesso (204) usando PUT")
  void shouldActivateProfessionalSuccessfully() throws Exception {
   UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
   Mockito.doNothing().when(service).activateProfessional(id);
   mockMvc.perform(put("/professionals/{id}/activate", id).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Deve reativar profissional com sucesso (204) usando PATCH")
  void shouldReactivateProfessionalSuccessfully() throws Exception {
   UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
   Mockito.doNothing().when(service).reactivateProfessional(id);
   mockMvc.perform(patch("/professionals/{id}/reactivate", id).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isNoContent());
  }
 }

 @Nested
 @DisplayName("Cenários de Documentos e Foto de Perfil")
 class DocumentosEFotos {
  @Test
  @DisplayName("Deve listar documentos do profissional com sucesso (200)")
  void shouldGetProfessionalDocumentsSuccessfully() throws Exception {
   UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
   DocumentDTO docMock = new DocumentDTO(UUID.randomUUID(), "CRM_Doc", DocumentCategory.PROFESSIONAL, br.org.apae.api.documents.domain.enums.DocumentType.MEDICAL_REPORT, id.toString(), java.time.Year.of(2026));

   Mockito.when(documentApplicationService.listDocuments(any())).thenReturn(List.of(docMock));
   Mockito.when(documentApplicationService.generatePresignedUrl(any(), any())).thenReturn(
           new DocumentWithUrlResponseDTO(docMock.id(), "CRM_Doc", DocumentCategory.PROFESSIONAL,
                   br.org.apae.api.documents.domain.enums.DocumentType.MEDICAL_REPORT,
                   id.toString(), java.time.Year.of(2026), "https://aws.s3.url/presigned"));

   mockMvc.perform(get("/professionals/{id}/documents", id).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.length()").value(1))
     .andExpect(jsonPath("$[0].url").value("https://aws.s3.url/presigned"));
  }

  @ParameterizedTest
  @MethodSource("br.org.apae.api.controllers.professional.HealthProfessionalControllerTest#allowedDocumentFormats")
  @DisplayName("Deve atualizar documentos do profissional com sucesso (204) via PATCH Multipart")
  void shouldUpdateProfessionalDocumentsSuccessfully(String filename, String contentType) throws Exception {
   UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
   var firstAttachment = HealthProfessionalMockDto.attachmentAnyFile();
   var additionalAttachment = new MockMultipartFile("attachmentAny", filename, contentType, new byte[]{1});

   mockMvc.perform(updateDocumentsRequest(id)
                   .file(HealthProfessionalMockDto.volunteerAgreementFile())
                   .file(HealthProfessionalMockDto.curriculumFile())
                   .file(firstAttachment)
                   .file(additionalAttachment))
           .andExpect(status().isNoContent());

   var captor = ArgumentCaptor.forClass(UpdateProfessionalDocumentsDTO.class);
   Mockito.verify(service).updateProfessionalDocuments(Mockito.eq(id), captor.capture());
   assertEquals(List.of(firstAttachment, additionalAttachment), captor.getValue().attachmentAny());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  @DisplayName("Deve atualizar apenas o currículo com anexo omitido ou vazio (204)")
  void shouldAcceptPartialDocumentUpdate(boolean includeEmptyAttachment) throws Exception {
   UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
   var curriculum = HealthProfessionalMockDto.curriculumFile();
   var request = updateDocumentsRequest(id).file(curriculum);
   if (includeEmptyAttachment) {
    request.file(new MockMultipartFile("attachmentAny", new byte[0]));
   }

   mockMvc.perform(request).andExpect(status().isNoContent());

   var captor = ArgumentCaptor.forClass(UpdateProfessionalDocumentsDTO.class);
   Mockito.verify(service).updateProfessionalDocuments(Mockito.eq(id), captor.capture());
   assertNull(captor.getValue().volunteerAgreement());
   assertEquals(curriculum, captor.getValue().curriculum());
  }

  @ParameterizedTest
  @ValueSource(strings = {"volunteerAgreement", "curriculum", "attachmentAny"})
  @DisplayName("Deve recusar TXT na edição, inclusive após um anexo válido (400)")
  void shouldRejectInvalidDocumentFormat(String field) throws Exception {
   var request = updateDocumentsRequest(HealthProfessionalMockDto.PROFESSIONAL_ID_1)
           .file(HealthProfessionalMockDto.attachmentAnyFile())
           .file(new MockMultipartFile(field, "document.txt", MediaType.TEXT_PLAIN_VALUE, new byte[]{1}));

   mockMvc.perform(request)
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.fields[0].field").value(field))
           .andExpect(jsonPath("$.fields[0].message").value(INVALID_DOCUMENT_MESSAGE));
   Mockito.verifyNoInteractions(service);
  }

  @Test
  @DisplayName("Deve recusar anexo acima de 10 MB mesmo com outro anexo válido (400)")
  void shouldRejectOversizedAttachment() throws Exception {
   var attachment = new MockMultipartFile("attachmentAny", "large.pdf", MediaType.APPLICATION_PDF_VALUE,
           new byte[OVERSIZED_FILE_BYTES]);

   mockMvc.perform(updateDocumentsRequest(HealthProfessionalMockDto.PROFESSIONAL_ID_1)
                   .file(HealthProfessionalMockDto.attachmentAnyFile())
                   .file(attachment))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.fields[0].field").value("attachmentAny"))
           .andExpect(jsonPath("$.fields[0].message").value(containsString("excede o limite máximo")));
   Mockito.verifyNoInteractions(service);
  }

  private MockMultipartHttpServletRequestBuilder updateDocumentsRequest(UUID id) {
   var request = multipart(HttpMethod.PATCH, "/professionals/{id}/documents", id);
   request.header("Authorization", AuthTestHelper.bearerToken());
   return request;
  }

  @Test
  @DisplayName("Deve remover documento do profissional com sucesso (204)")
  void shouldRemoveProfessionalDocumentSuccessfully() throws Exception {
   UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
   UUID documentId = UUID.randomUUID();
   Mockito.doNothing().when(service).removeProfessionalDocument(id, documentId);
   mockMvc.perform(delete("/professionals/{id}/documents/{documentId}", id, documentId).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Deve fazer upload da foto de perfil com sucesso (204)")
  void shouldUploadProfessionalPhotoSuccessfully() throws Exception {
   UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
   MockMultipartFile photoFile = new MockMultipartFile("file", "profile.jpg", MediaType.IMAGE_JPEG_VALUE, "fake-image-bytes".getBytes());
   
   Mockito.doNothing().when(service).uploadProfessionalPhoto(Mockito.eq(id), any());

   mockMvc.perform(multipart("/professionals/{id}/photo", id).file(photoFile)
       .with(request -> {
        request.setMethod("PATCH");
        return request;
       })
       .header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isNoContent());
  }
 }
}
