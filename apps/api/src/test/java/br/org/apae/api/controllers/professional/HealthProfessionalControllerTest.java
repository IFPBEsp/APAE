package br.org.apae.api.controllers.professional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Year;
import java.util.List;
import java.util.UUID;

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

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.request.documents.UpdateProfessionalDocumentsDTO;
import br.org.apae.api.controllers.mocks.professional.HealthProfessionalMockDto;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalApplicationService;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;


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
    @DisplayName("Cria com sucesso um novo profissional")
    void shouldCreateProfessionalSuccessfully() throws Exception {
        var request =
            HealthProfessionalMockDto.createHealthProfessionalRequest();

        var response =
            HealthProfessionalMockDto.createProfessionalResponse1();

        Mockito.when(
            service.createProfessional(
                any(CreateHealthProfessionalDTO.class),
                any(CreateProfessionalDocumentsDTO.class)
            )
        ).thenReturn(response);

        var professionalPart =
            new MockMultipartFile(
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

        Mockito.verify(service)
            .createProfessional(any(), any());
    }

    @Test
    @DisplayName("Retorna a lista de profissionais cadastrados")
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

            .andExpect(jsonPath("$.content[0].id")
                .value("11111111-1111-1111-1111-111111111111"))
            .andExpect(jsonPath("$.content[0].healthSector")
                .value("Fisioterapia"))
            .andExpect(jsonPath("$.content[0].address.city")
                .value("São Paulo"))

            .andExpect(jsonPath("$.content[1].id")
                .value("22222222-2222-2222-2222-222222222222"))
            .andExpect(jsonPath("$.content[1].name")
                .value("Maria Souza"))
            .andExpect(jsonPath("$.content[1].address.neighborhood")
                .value("Santana"));
    }

    @Test
    @DisplayName("Retorna profissional por id com sucesso")
    void shouldFindProfessionalById() throws Exception {
        var response = HealthProfessionalMockDto.createProfessionalResponse1();

        Mockito.when(service.findProfessionalById(HealthProfessionalMockDto.PROFESSIONAL_ID_1))
            .thenReturn(response);

        mockMvc.perform(
                get("/professionals/{id}", HealthProfessionalMockDto.PROFESSIONAL_ID_1)
                    .header("Authorization", AuthTestHelper.bearerToken())
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id")
                .value(HealthProfessionalMockDto.PROFESSIONAL_ID_1.toString()))
            .andExpect(jsonPath("$.name").value("João da Silva"));

        Mockito.verify(service)
            .findProfessionalById(HealthProfessionalMockDto.PROFESSIONAL_ID_1);
    }

    @Test
    @DisplayName("Atualiza profissional com sucesso")
    void shouldUpdateProfessional() throws Exception {
        var request = HealthProfessionalMockDto.updateHealthProfessionalRequest();
        var response = HealthProfessionalMockDto.createProfessionalResponse1();

        Mockito.when(service.updateProfessional(
                Mockito.eq(HealthProfessionalMockDto.PROFESSIONAL_ID_1),
                any(UpdateHealthProfessionalDTO.class)
            )
        ).thenReturn(response);

        mockMvc.perform(
                put("/professionals/{id}", HealthProfessionalMockDto.PROFESSIONAL_ID_1)
                    .header("Authorization", AuthTestHelper.bearerToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id")
                .value(HealthProfessionalMockDto.PROFESSIONAL_ID_1.toString()));

        Mockito.verify(service)
            .updateProfessional(
                Mockito.eq(HealthProfessionalMockDto.PROFESSIONAL_ID_1),
                any(UpdateHealthProfessionalDTO.class)
            );
    }

    @Test
    @DisplayName("Inativa profissional com sucesso")
    void shouldInactivateProfessional() throws Exception {
        Mockito.doNothing()
            .when(service)
            .inactivateProfessional(HealthProfessionalMockDto.PROFESSIONAL_ID_1);

        mockMvc.perform(
                put("/professionals/{id}/inactivate", HealthProfessionalMockDto.PROFESSIONAL_ID_1)
                    .header("Authorization", AuthTestHelper.bearerToken())
            )
            .andExpect(status().isNoContent());

        Mockito.verify(service)
            .inactivateProfessional(HealthProfessionalMockDto.PROFESSIONAL_ID_1);
    }

    @Test
    @DisplayName("Ativa profissional com sucesso")
    void shouldActivateProfessional() throws Exception {
        Mockito.doNothing()
            .when(service)
            .activateProfessional(HealthProfessionalMockDto.PROFESSIONAL_ID_1);

        mockMvc.perform(
                put("/professionals/{id}/activate", HealthProfessionalMockDto.PROFESSIONAL_ID_1)
                    .header("Authorization", AuthTestHelper.bearerToken())
            )
            .andExpect(status().isNoContent());

        Mockito.verify(service)
            .activateProfessional(HealthProfessionalMockDto.PROFESSIONAL_ID_1);
    }

    @Test
    @DisplayName("Reativa profissional com sucesso")
    void shouldReactivateProfessional() throws Exception {
        Mockito.doNothing()
            .when(service)
            .reactivateProfessional(HealthProfessionalMockDto.PROFESSIONAL_ID_1);

        mockMvc.perform(
                patch("/professionals/{id}/reactivate", HealthProfessionalMockDto.PROFESSIONAL_ID_1)
                    .header("Authorization", AuthTestHelper.bearerToken())
            )
            .andExpect(status().isNoContent());

        Mockito.verify(service)
            .reactivateProfessional(HealthProfessionalMockDto.PROFESSIONAL_ID_1);
    }

    @Test
    @DisplayName("Atualiza documentos do profissional")
    void shouldUpdateProfessionalDocuments() throws Exception {
        Mockito.doNothing()
            .when(service)
            .updateProfessionalDocuments(
                Mockito.eq(HealthProfessionalMockDto.PROFESSIONAL_ID_1),
                any(UpdateProfessionalDocumentsDTO.class)
            );

        mockMvc.perform(
                multipart("/professionals/{id}/documents", HealthProfessionalMockDto.PROFESSIONAL_ID_1)
                    .file(HealthProfessionalMockDto.volunteerAgreementFile())
                    .file(HealthProfessionalMockDto.curriculumFile())
                    .with(request -> {
                        request.setMethod("PATCH");
                        return request;
                    })
                    .header("Authorization", AuthTestHelper.bearerToken())
            )
            .andExpect(status().isNoContent());

        Mockito.verify(service)
            .updateProfessionalDocuments(Mockito.eq(HealthProfessionalMockDto.PROFESSIONAL_ID_1), any());
    }

    @Test
    @DisplayName("Remove documento do profissional")
    void shouldRemoveProfessionalDocument() throws Exception {
        UUID documentId = UUID.randomUUID();

        Mockito.doNothing()
            .when(service)
            .removeProfessionalDocument(
                HealthProfessionalMockDto.PROFESSIONAL_ID_1,
                documentId
            );

        mockMvc.perform(
                delete(
                    "/professionals/{id}/documents/{documentId}",
                    HealthProfessionalMockDto.PROFESSIONAL_ID_1,
                    documentId
                )
                .header("Authorization", AuthTestHelper.bearerToken())
            )
            .andExpect(status().isNoContent());

        Mockito.verify(service)
            .removeProfessionalDocument(
                HealthProfessionalMockDto.PROFESSIONAL_ID_1,
                documentId
            );
    }

    @Test
    @DisplayName("Retorna documentos do profissional com URL assinada")
    void shouldReturnProfessionalDocumentsWithPresignedUrl() throws Exception {
        UUID professionalId = HealthProfessionalMockDto.PROFESSIONAL_ID_1;

        var document1 = new DocumentDTO(
            UUID.randomUUID(),
            "curriculo.pdf",
            DocumentCategory.PROFESSIONAL,
            DocumentType.CURRICULUM,
            professionalId.toString(),
            Year.of(2024)
        );

        var document2 = new DocumentDTO(
            UUID.randomUUID(),
            "contrato.pdf",
            DocumentCategory.PROFESSIONAL,
            DocumentType.VOLUNTEER_AGREEMENT,
            professionalId.toString(),
            Year.of(2023)
        );

        Mockito.when(documentApplicationService.listDocuments(any()))
            .thenReturn(List.of(document1, document2));

        Mockito.when(documentApplicationService.getPresignedDocumentUrl(any()))
            .thenReturn("https://signed-url.com/document.pdf");

        mockMvc.perform(
                get("/professionals/{id}/documents", professionalId)
                    .header("Authorization", AuthTestHelper.bearerToken())
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(document1.id().toString()))
            .andExpect(jsonPath("$[0].name").value("curriculo.pdf"))
            .andExpect(jsonPath("$[0].url").value("https://signed-url.com/document.pdf"))
            .andExpect(jsonPath("$[1].id").value(document2.id().toString()))
            .andExpect(jsonPath("$[1].name").value("contrato.pdf"))
            .andExpect(jsonPath("$[1].url").value("https://signed-url.com/document.pdf"));

        Mockito.verify(documentApplicationService).listDocuments(any());
        Mockito.verify(documentApplicationService, Mockito.atLeastOnce())
            .getPresignedDocumentUrl(any());
    }
}
