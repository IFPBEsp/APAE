package br.org.apae.api.controller.disorder;

import br.org.apae.api.auth.infrastructure.security.SecurityFilter;
import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.patient.request.disorder.UpdateDisorderDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.controllers.disorder.DisorderControllerImpl;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.domain.exceptions.DisorderConflictException;
import br.org.apae.api.patient.domain.exceptions.DisorderNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("unit")
@Tag("controller")
@Tag("patient")
@WebMvcTest(
        controllers = DisorderControllerImpl.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityFilter.class
        ))
@AutoConfigureMockMvc(addFilters = false)
class DisorderControllerImplTest {

    @TestConfiguration
    @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
    static class ContextConfiguration {

    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DisorderApplicationService disorderService;

    private static final String BASE_URL = "/disorders";

    @Test
    void shouldCreateDisordSucess() throws Exception {
        CreateDisorderDTO requestDto = new CreateDisorderDTO("Transtorno Ansioso");
        UUID idGerado = UUID.randomUUID();

        DisorderResponseDTO responseDto = new DisorderResponseDTO(idGerado, requestDto.name());

        when(disorderService.createDisorder(requestDto)).thenReturn(responseDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(idGerado.toString()))
                .andExpect(jsonPath("$.name").value(requestDto.name()));
    }

    @Test
    void shouldGetAllDisordersSucess() throws Exception {
        DisorderResponseDTO d1 = new DisorderResponseDTO(UUID.randomUUID(), "Transtorno A");
        DisorderResponseDTO d2 = new DisorderResponseDTO(UUID.randomUUID(), "Transtorno B");
        List<DisorderResponseDTO> lista = Arrays.asList(d1, d2);

        when(disorderService.findAllDisorders()).thenReturn(lista);

        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Transtorno A")))
                .andExpect(jsonPath("$[1].name", is("Transtorno B")));
    }

    @Test
    void shouldSearchDisordsByIdSucess() throws Exception {
        UUID id = UUID.randomUUID();
        DisorderResponseDTO responseDto = new DisorderResponseDTO(id, "Transtorno Específico");

        when(disorderService.findDisorderById(id)).thenReturn(responseDto);

        mockMvc.perform(get(BASE_URL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Transtorno Específico"));
    }

    @Test
    void SouldUpdateDisorderSucess() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateDisorderDTO updateDto = new UpdateDisorderDTO("Transtorno Atualizado");
        DisorderResponseDTO responseDto = new DisorderResponseDTO(id, updateDto.name());

        when(disorderService.updateDisorder(id, updateDto)).thenReturn(responseDto);

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value(updateDto.name()));
    }

    @Test
    void shouldRemoveDisorderSucess() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNoContent());

        verify(disorderService).deleteDisorder(id);
    }

    @Test
    void shouldReturnConflictWhenCreateDisorderWithDuplicateName() throws Exception {
        CreateDisorderDTO requestDto = new CreateDisorderDTO("Transtorno Duplicado");

        when(disorderService.createDisorder(requestDto))
                .thenThrow(new DisorderConflictException(requestDto.name()));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFoundWhenSearchDisorderByIdNonExistent() throws Exception {
        UUID id = UUID.randomUUID();

        when(disorderService.findDisorderById(id))
                .thenThrow(new DisorderNotFoundException());

        mockMvc.perform(get(BASE_URL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFoundWhenUpdateDisorderNonExistent() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateDisorderDTO updateDto = new UpdateDisorderDTO("Nome qualquer");

        when(disorderService.updateDisorder(id, updateDto))
                .thenThrow(new DisorderNotFoundException());

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnConflictWhenUpdateDisorderWithDuplicateName() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateDisorderDTO updateDto = new UpdateDisorderDTO("Nome Já Existe");

        when(disorderService.updateDisorder(id, updateDto))
                .thenThrow(new DisorderConflictException(updateDto.name()));

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFoundWhenDeleteDisorderNonExistent() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(new DisorderNotFoundException()).when(disorderService).deleteDisorder(id);

        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isConflict());
    }
}