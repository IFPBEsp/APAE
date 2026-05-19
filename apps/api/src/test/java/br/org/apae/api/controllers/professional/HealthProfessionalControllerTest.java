package br.org.apae.api.controllers.professional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.controllers.mocks.professional.HealthProfessionalMockDto;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalApplicationService;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.domain.enums.DocumentCategory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Tag("controller")
@Tag("health-professional")
@WebMvcTest(controllers = HealthProfessionalControllerImpl.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({
    SpringDataWebConfiguration.class,
    SecurityConfiguration.class
})
class HealthProfessionalControllerTest {

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
    private DocumentApplicationService documentApplicationService;

    @BeforeEach
    void setupAuth() {
        AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);
    }

    @Test
    @DisplayName("Deve criar profissional com sucesso (201)")
    void shouldCreateProfessionalSuccessfully() throws Exception {
        var request = HealthProfessionalMockDto.createHealthProfessionalRequest();
        var response = HealthProfessionalMockDto.createProfessionalResponse1();

        Mockito.when(
            service.createProfessional(
                any(CreateHealthProfessionalDTO.class),
                any(CreateProfessionalDocumentsDTO.class)
            )
        ).thenReturn(response);

        var professionalPart = new MockMultipartFile(
            "professional",
            "",
            MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(request)
        );

        mockMvc.perform(
                multipart("/professionals")
                    .file(professionalPart)
                    .file(HealthProfessionalMockDto.volunteerAgreementFile())
                    .file(HealthProfessionalMockDto.curriculumFile())
                    .file(HealthProfessionalMockDto.attachmentAnyFile())
                    .header("Authorization", AuthTestHelper.bearerToken())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isCreated());

        Mockito.verify(service).createProfessional(any(), any());
    }

    @Test
    @DisplayName("Deve listar todos os profissionais paginado (200)")
    void shouldReturnAllProfessionals() throws Exception {
        var responses = HealthProfessionalMockDto.createProfessionalResponseList();

        var page = new PageImpl<>(
            responses,
            PageRequest.of(0, 10),
            responses.size()
        );

        Mockito.when(service.findAllProfessionals(isNull(), any(Pageable.class)))
            .thenReturn(page);

        mockMvc.perform(get("/professionals")
                .param("Ativo", "true")
                .header("Authorization", AuthTestHelper.bearerToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].id").value(HealthProfessionalMockDto.PROFESSIONAL_ID_1.toString()))
            .andExpect(jsonPath("$.content[0].healthSector").value("Fisioterapia"))
            .andExpect(jsonPath("$.content[0].address.city").value("São Paulo"))
            .andExpect(jsonPath("$.content[1].id").value(HealthProfessionalMockDto.PROFESSIONAL_ID_2.toString()))
            .andExpect(jsonPath("$.content[1].name").value("Maria Souza"))
            .andExpect(jsonPath("$.content[1].address.neighborhood").value("Santana"));
    }

    @Test
    @DisplayName("Deve inativar profissional com sucesso (204) usando PUT")
    void shouldInactivateProfessionalSuccessfully() throws Exception {
        UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;

        Mockito.doNothing().when(service).inactivateProfessional(id);

        mockMvc.perform(put("/professionals/{id}/inactivate", id)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNoContent()); 

        Mockito.verify(service).inactivateProfessional(id);
    }

    @Test
    @DisplayName("Deve ativar profissional com sucesso (204) usando PUT")
    void shouldActivateProfessionalSuccessfully() throws Exception {
        UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;

        Mockito.doNothing().when(service).activateProfessional(id);

        mockMvc.perform(put("/professionals/{id}/activate", id)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNoContent());

        Mockito.verify(service).activateProfessional(id);
    }

    @Test
    @DisplayName("Deve reativar profissional com sucesso (204) usando PATCH")
    void shouldReactivateProfessionalSuccessfully() throws Exception {
        UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;

        Mockito.doNothing().when(service).reactivateProfessional(id);

        mockMvc.perform(patch("/professionals/{id}/reactivate", id)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNoContent());

        Mockito.verify(service).reactivateProfessional(id);
    }

    @Test
    @DisplayName("Deve buscar profissional por ID com sucesso (200)")
    void shouldFindProfessionalByIdSuccessfully() throws Exception {
        UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
        var response = HealthProfessionalMockDto.createProfessionalResponse1();

        Mockito.when(service.findProfessionalById(id)).thenReturn(response);

        mockMvc.perform(get("/professionals/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.healthSector").value("Fisioterapia"));
    }

    @Test
    @DisplayName("Deve atualizar profissional com sucesso (200)")
    void shouldUpdateProfessionalSuccessfully() throws Exception {
        UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
        var response = HealthProfessionalMockDto.createProfessionalResponse1();
        var updateRequest = HealthProfessionalMockDto.createHealthProfessionalRequest();

        Mockito.when(service.updateProfessional(Mockito.eq(id), any(UpdateHealthProfessionalDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/professionals/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    @DisplayName("Deve listar documentos do profissional com sucesso (200)")
    void shouldGetProfessionalDocumentsSuccessfully() throws Exception {
        UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
        UUID docId = UUID.randomUUID();
        
        DocumentDTO docMock = new DocumentDTO(
            docId, 
            "CRM_Doc", 
            DocumentCategory.PROFESSIONAL, 
            br.org.apae.api.documents.domain.enums.DocumentType.MEDICAL_REPORT, 
            id.toString(), 
            java.time.Year.of(2026)
        );

        Mockito.when(documentApplicationService.listDocuments(any()))
                .thenReturn(List.of(docMock));

        Mockito.when(documentApplicationService.getPresignedDocumentUrl(any()))
                .thenReturn("https://aws.s3.url/presigned");

        mockMvc.perform(get("/professionals/{id}/documents", id)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("CRM_Doc"))
                .andExpect(jsonPath("$[0].url").value("https://aws.s3.url/presigned"));
    }

    @Test
    @DisplayName("Deve atualizar documentos do profissional com sucesso (204) via PATCH Multipart")
    void shouldUpdateProfessionalDocumentsSuccessfully() throws Exception {
        UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;

        mockMvc.perform(multipart("/professionals/{id}/documents", id)
                        .file(HealthProfessionalMockDto.volunteerAgreementFile()) // ADAPTAÇÃO: utilizando arquivos do mock
                        .file(HealthProfessionalMockDto.curriculumFile())
                        .with(request -> { request.setMethod("PATCH"); return request; })
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNoContent());

        Mockito.verify(service).updateProfessionalDocuments(Mockito.eq(id), any());
    }

    @Test
    @DisplayName("Deve remover documento do profissional com sucesso (204)")
    void shouldRemoveProfessionalDocumentSuccessfully() throws Exception {
        UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
        UUID documentId = UUID.randomUUID();

        Mockito.doNothing().when(service).removeProfessionalDocument(id, documentId);

        mockMvc.perform(delete("/professionals/{id}/documents/{documentId}", id, documentId)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve fazer upload da foto de perfil com sucesso (204)")
    void shouldUploadProfessionalPhotoSuccessfully() throws Exception {
        UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
        MockMultipartFile photoFile = new MockMultipartFile(
            "file", 
            "profile.jpg", 
            MediaType.IMAGE_JPEG_VALUE, 
            "fake-image-bytes".getBytes()
        );

        Mockito.doNothing().when(service).uploadProfessionalPhoto(Mockito.eq(id), any(MultipartFile.class));

        mockMvc.perform(multipart("/professionals/{id}/photo", id)
                        .file(photoFile)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNoContent());

        Mockito.verify(service).uploadProfessionalPhoto(Mockito.eq(id), any(MultipartFile.class));
    }

    @Test
    @DisplayName("Deve listar horários disponíveis do profissional no formato HH:mm (200)")
    void shouldGetAvailableTimesSuccessfully() throws Exception {
        UUID id = HealthProfessionalMockDto.PROFESSIONAL_ID_1;
        String dateString = "2026-05-18";

        List<LocalTime> mockTimes = List.of(LocalTime.of(9, 0), LocalTime.of(10, 30));

        Mockito.when(service.getAvailableTimes(id, LocalDate.parse(dateString)))
                .thenReturn(mockTimes);

        mockMvc.perform(get("/professionals/{id}/available-times", id)
                        .param("date", dateString)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("09:00"))
                .andExpect(jsonPath("$[1]").value("10:30"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar profissional inexistente")
    void shouldReturnNotFoundWhenProfessionalDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID(); 

        Mockito.when(service.findProfessionalById(id))
                .thenThrow(new HealthProfessionalNotFoundException());

        mockMvc.perform(get("/professionals/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNotFound());
    }
}