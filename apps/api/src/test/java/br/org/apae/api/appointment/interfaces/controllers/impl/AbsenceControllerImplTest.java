package br.org.apae.api.appointment.interfaces.controllers.impl;

import br.org.apae.api.appointment.application.interfaces.AbsenceApplicationService;
import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import br.org.apae.api.common.dto.appointment.response.absence.JustifyAbsenceDTO;
import br.org.apae.api.controllers.absence.AbsenceControllerImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(controllers = AbsenceControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class AbsenceControllerImplTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private AbsenceApplicationService service;

        @MockitoBean
        private JwtProvider jwtProvider;

        @MockitoBean
        private UserService userService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void registerAbsenceSuccessfully() throws Exception {
                UUID id = UUID.randomUUID();
                UUID generatedId = UUID.randomUUID();
                UUID patientId = UUID.randomUUID();
                UUID professionalId = UUID.randomUUID();
                LocalDate date = LocalDate.now();

                CreateAbsenceDTO request = new CreateAbsenceDTO(
                        generatedId,
                        date,
                        false,
                        null,
                        null);

                AbsenceResponseDTO response = new AbsenceResponseDTO(
                        id,
                        generatedId,
                        patientId,
                        professionalId,
                        date,
                        null,
                        false,
                        false,
                        null);

                when(service.register(any(CreateAbsenceDTO.class))).thenReturn(response);

                var result = mockMvc.perform(post("/absences")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn();

                assertEquals(201, result.getResponse().getStatus());
                assertNotNull(result.getResponse().getHeader("Location"));
                assertTrue(result.getResponse().getHeader("Location").contains(id.toString()));

                AbsenceResponseDTO body = objectMapper.readValue(
                        result.getResponse().getContentAsString(), AbsenceResponseDTO.class);

                assertEquals(id, body.id());
                assertEquals(generatedId, body.generatedAppointmentId());
                assertFalse(body.notified());
                assertFalse(body.isJustified());
        }

        @Test
        void shouldReturnBadRequestWhenRegisterWithNullGeneratedAppointmentId() throws Exception {
                CreateAbsenceDTO invalidRequest = new CreateAbsenceDTO(
                        null,
                        LocalDate.now(),
                        false,
                        null,
                        null);

                var result = mockMvc.perform(post("/absences")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andReturn();

                assertEquals(400, result.getResponse().getStatus());
        }

        @Test
        void shouldReturnBadRequestWhenRegisterWithNullAbsenceDate() throws Exception {
                CreateAbsenceDTO invalidRequest = new CreateAbsenceDTO(
                        UUID.randomUUID(),
                        null,
                        false,
                        null,
                        null);

                var result = mockMvc.perform(post("/absences")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andReturn();

                assertEquals(400, result.getResponse().getStatus());
        }

        @Test
        void shouldReturnBadRequestWhenServiceThrowsExceptionOnRegister() throws Exception {
                CreateAbsenceDTO request = new CreateAbsenceDTO(
                        UUID.randomUUID(),
                        LocalDate.now(),
                        false,
                        null,
                        null);

                when(service.register(any(CreateAbsenceDTO.class)))
                        .thenThrow(new IllegalArgumentException("Falta já registrada"));

                var result = mockMvc.perform(post("/absences")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn();

                assertEquals(400, result.getResponse().getStatus());
        }

        // ==================== findAll ====================

        @Test
        void searchForAbsencesUsingFiltersAndPagination() throws Exception {
                UUID generatedId = UUID.randomUUID();
                UUID patientId = UUID.randomUUID();
                UUID professionalId = UUID.randomUUID();
                LocalDate date = LocalDate.now();

                AbsenceResponseDTO response = new AbsenceResponseDTO(
                        UUID.randomUUID(),
                        generatedId,
                        patientId,
                        professionalId,
                        date,
                        "Falta justificada",
                        true,
                        true,
                        null);

                Page<AbsenceResponseDTO> page = new PageImpl<>(
                        List.of(response),
                        PageRequest.of(0, 10),
                        1);

                when(service.findAllByFilters(any(), any(), any(), any())).thenReturn(page);

                var result = mockMvc.perform(get("/absences")
                                .param("generatedId", generatedId.toString())
                                .param("patientId", patientId.toString())
                                .param("professionalId", professionalId.toString())
                                .param("page", "0")
                                .param("size", "10"))
                        .andReturn();

                assertEquals(200, result.getResponse().getStatus());

                String json = result.getResponse().getContentAsString();
                assertTrue(json.contains("\"content\""));
                assertTrue(json.contains("Falta justificada"));
                assertTrue(json.contains("\"notified\":true"));
                assertTrue(json.contains("\"isJustified\":true"));
        }

        @Test
        void shouldSearchAbsencesWithoutFilters() throws Exception {
                AbsenceResponseDTO response = new AbsenceResponseDTO(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        LocalDate.now(),
                        null,
                        false,
                        false,
                        null);

                Page<AbsenceResponseDTO> page = new PageImpl<>(List.of(response));

                when(service.findAllByFilters(isNull(), isNull(), isNull(), any()))
                        .thenReturn(page);

                var result = mockMvc.perform(get("/absences")
                                .param("page", "0")
                                .param("size", "10"))
                        .andReturn();

                assertEquals(200, result.getResponse().getStatus());

                String json = result.getResponse().getContentAsString();
                assertTrue(json.contains("\"content\""));
                assertTrue(json.contains("\"notified\":false"));
        }

        @Test
        void shouldReturnEmptyPageWhenNoAbsencesFound() throws Exception {
                when(service.findAllByFilters(isNull(), isNull(), isNull(), any()))
                        .thenReturn(Page.empty());

                var result = mockMvc.perform(get("/absences")
                                .param("page", "0")
                                .param("size", "10"))
                        .andReturn();

                assertEquals(200, result.getResponse().getStatus());

                String json = result.getResponse().getContentAsString();
                assertTrue(json.contains("\"content\":[]"));
        }

        // ==================== justifyAbsence ====================

        @Test
        void justifyAbsenceSuccessfully() throws Exception {
                UUID absenceId = UUID.randomUUID();
                UUID generatedId = UUID.randomUUID();
                UUID patientId = UUID.randomUUID();
                UUID professionalId = UUID.randomUUID();
                LocalDate date = LocalDate.now();
                String documentId = "doc-123";

                JustifyAbsenceDTO request = new JustifyAbsenceDTO(
                        "Motivo de saúde urgente",
                        documentId);

                AbsenceResponseDTO response = new AbsenceResponseDTO(
                        absenceId,
                        generatedId,
                        patientId,
                        professionalId,
                        date,
                        "Motivo de saúde urgente",
                        false,
                        true,
                        documentId);

                when(service.justify(eq(absenceId), any(JustifyAbsenceDTO.class)))
                        .thenReturn(response);

                var result = mockMvc.perform(patch("/absences/{id}/justify", absenceId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn();

                assertEquals(200, result.getResponse().getStatus());

                AbsenceResponseDTO body = objectMapper.readValue(
                        result.getResponse().getContentAsString(), AbsenceResponseDTO.class);

                assertEquals(absenceId, body.id());
                assertEquals("Motivo de saúde urgente", body.justification());
                assertTrue(body.isJustified());
                assertEquals(documentId, body.justificationDocumentId());
        }

        @Test
        void shouldReturnBadRequestWhenJustifyAbsenceWithBlankJustification() throws Exception {
                UUID absenceId = UUID.randomUUID();

                JustifyAbsenceDTO invalidRequest = new JustifyAbsenceDTO("", null);

                var result = mockMvc.perform(patch("/absences/{id}/justify", absenceId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andReturn();

                assertEquals(400, result.getResponse().getStatus());
        }

        @Test
        void shouldReturnBadRequestWhenJustifyAbsenceWithNullJustification() throws Exception {
                UUID absenceId = UUID.randomUUID();

                JustifyAbsenceDTO invalidRequest = new JustifyAbsenceDTO(null, null);

                var result = mockMvc.perform(patch("/absences/{id}/justify", absenceId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andReturn();

                assertEquals(400, result.getResponse().getStatus());
        }

        @Test
        void shouldReturnBadRequestWhenServiceThrowsExceptionOnJustify() throws Exception {
                UUID absenceId = UUID.randomUUID();

                JustifyAbsenceDTO request = new JustifyAbsenceDTO("Justificativa válida", null);

                when(service.justify(eq(absenceId), any(JustifyAbsenceDTO.class)))
                        .thenThrow(new IllegalArgumentException("Falta não encontrada"));

                var result = mockMvc.perform(patch("/absences/{id}/justify", absenceId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn();

                assertEquals(400, result.getResponse().getStatus());
        }
}
