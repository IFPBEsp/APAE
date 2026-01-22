package br.org.apae.api.controllers.professional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.controllers.mocks.professional.HealthProfessionalMockDto;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalApplicationService;

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
    void shouldReturnAllProfessionals() throws Exception {
        var responses = HealthProfessionalMockDto.createProfessionalResponseList();

        var page = new PageImpl<>(
            responses,
            PageRequest.of(0, 10),
            responses.size()
        );

        Mockito.when(service.findAllProfessionals(any(Pageable.class)))
            .thenReturn(page);

        mockMvc.perform(get("/professionals")
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
}
