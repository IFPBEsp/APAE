package br.org.apae.api.appointment.interfaces.controllers.impl;

import br.org.apae.api.appointment.application.interfaces.AbsenceApplicationService;
import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AbsenceControllerImpl.class)
class AbsenceControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AbsenceApplicationService service;

    @Autowired
    private ObjectMapper objectMapper;

    // Registrar falta com sucesso
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
                "Paciente Faltou"
        );

        AbsenceResponseDTO response = new AbsenceResponseDTO(
                id,
                generatedId,
                patientId,
                professionalId,
                date,
                "Paciente Faltou",
                false
        );

        when(service.register(any(CreateAbsenceDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/absences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.reason").value("Paciente Faltou"))
                .andExpect(jsonPath("$.justified").value(false));
    }

    // Buscar faltas com filtros e paginação
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
                true
        );

        Page<AbsenceResponseDTO> page = new PageImpl<>(
                List.of(response),
                PageRequest.of(0, 10),
                1
        );

        when(service.findAllByFilters(any(), any(), any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/absences")
                        .param("generatedId", generatedId.toString())
                        .param("patientId", patientId.toString())
                        .param("professionalId", professionalId.toString())
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reason").value("Falta justificada"))
                .andExpect(jsonPath("$.content[0].justified").value(true));
    }
}
