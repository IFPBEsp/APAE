package br.org.apae.api.controllers.professional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
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
import br.org.apae.api.controllers.mocks.professional.HealthProfessionalMockDto;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalApplicationService;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.professional.application.exceptions.HealthProfessionalExceptionHandler;
import br.org.apae.api.professional.domain.exceptions.EmailConflictException;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.professional.domain.exceptions.IdentityDocumentConflictException;
import br.org.apae.api.professional.domain.exceptions.ProfessionalDocumentConflictException;


@Tag("controller")
@Tag("health-professional")
@WebMvcTest(controllers = HealthProfessionalControllerImpl.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({
    SpringDataWebConfiguration.class,
    SecurityConfiguration.class,
    HealthProfessionalExceptionHandler.class
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
    void shouldReturnConflictWhenProfessionalDocumentAlreadyExists() throws Exception {
        var request =
            HealthProfessionalMockDto.createHealthProfessionalRequest();

        Mockito.when(
            service.createProfessional(
                any(CreateHealthProfessionalDTO.class),
                any(CreateProfessionalDocumentsDTO.class)
            )
        ).thenThrow(new ProfessionalDocumentConflictException());

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
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message")
                .value("Documento profissional já cadastrado."));

        Mockito.verify(service)
            .createProfessional(any(), any());
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        var request =
            HealthProfessionalMockDto.createHealthProfessionalRequest();

        Mockito.when(
            service.createProfessional(
                any(CreateHealthProfessionalDTO.class),
                any(CreateProfessionalDocumentsDTO.class)
            )
        ).thenThrow(new EmailConflictException());

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
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message")
                .value("Email já cadastrado."));

        Mockito.verify(service)
            .createProfessional(any(), any());
    }

    @Test
    void shouldReturnConflictWhenIdentityDocumentAlreadyExists() throws Exception {
        var request =
            HealthProfessionalMockDto.createHealthProfessionalRequest();

        Mockito.when(
            service.createProfessional(
                any(CreateHealthProfessionalDTO.class),
                any(CreateProfessionalDocumentsDTO.class)
            )
        ).thenThrow(new IdentityDocumentConflictException());

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
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message")
                .value("O documento de identidade  já cadastrado."));

        Mockito.verify(service)
            .createProfessional(any(), any());
    }

    @Test
    void shouldReturnAllProfessionals() throws Exception {
        var responses = HealthProfessionalMockDto.createProfessionalResponseList();

        var page = new PageImpl<>(
            responses,
            PageRequest.of(0, 10),
            responses.size()
        );

        Mockito.when(service.findAllProfessionals(eq(true), any(Pageable.class)))
            .thenReturn(page);

        mockMvc.perform(get("/professionals")
            .param("ativo", "true")
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
    void shouldReturnProfessionalById() throws Exception {
        var response = HealthProfessionalMockDto.createProfessionalResponse1();

        Mockito.when(service.findProfessionalById(HealthProfessionalMockDto.PROFESSIONAL_ID_1))
            .thenReturn(response);

        mockMvc.perform(get("/professionals/{id}", HealthProfessionalMockDto.PROFESSIONAL_ID_1)
                .header("Authorization", AuthTestHelper.bearerToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id")
                .value("11111111-1111-1111-1111-111111111111"))
            .andExpect(jsonPath("$.name")
                .value("João da Silva"))
            .andExpect(jsonPath("$.healthSector")
                .value("Fisioterapia"));

        Mockito.verify(service).findProfessionalById(HealthProfessionalMockDto.PROFESSIONAL_ID_1);
    }

    @Test
    void shouldReturnNotFoundWhenProfessionalDoesNotExist() throws Exception {
        var missingId = UUID.fromString("99999999-9999-9999-9999-999999999999");

        Mockito.when(service.findProfessionalById(missingId))
            .thenThrow(new HealthProfessionalNotFoundException());

        mockMvc.perform(get("/professionals/{id}", missingId)
                .header("Authorization", AuthTestHelper.bearerToken()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message")
                .value("Profissional não encontrado."));

        Mockito.verify(service).findProfessionalById(missingId);
    }

    @Test
    void shouldUpdateProfessionalSuccessfully() throws Exception {
        var request = new UpdateHealthProfessionalDTO(
            new UpdateServiceAreaDTO("Psicologia"),
            "11999999999",
            "CREFITO-12345",
            "teste@apae.org.br",
            "João da Silva",
            "123456789",
            HealthProfessionalMockDto.createAddressRequest(),
            List.of()
        );

        var response = HealthProfessionalMockDto.createProfessionalResponse1();

        Mockito.when(service.updateProfessional(eq(HealthProfessionalMockDto.PROFESSIONAL_ID_1),
                any(UpdateHealthProfessionalDTO.class)))
            .thenReturn(response);

        mockMvc.perform(put("/professionals/{id}", HealthProfessionalMockDto.PROFESSIONAL_ID_1)
                .header("Authorization", AuthTestHelper.bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id")
                .value("11111111-1111-1111-1111-111111111111"))
            .andExpect(jsonPath("$.email")
                .value("teste@apae.org.br"));

        Mockito.verify(service).updateProfessional(eq(HealthProfessionalMockDto.PROFESSIONAL_ID_1), any());
    }

    @Test
    void shouldInactivateProfessionalSuccessfully() throws Exception {
        mockMvc.perform(put("/professionals/{id}/inactivate", HealthProfessionalMockDto.PROFESSIONAL_ID_1)
                .header("Authorization", AuthTestHelper.bearerToken()))
            .andExpect(status().isNoContent());

        Mockito.verify(service).inactivateProfessional(HealthProfessionalMockDto.PROFESSIONAL_ID_1);
    }
}
