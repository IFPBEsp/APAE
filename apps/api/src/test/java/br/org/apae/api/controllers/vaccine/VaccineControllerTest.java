package br.org.apae.api.controllers.vaccine;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.common.exceptions.handler.GlobalExceptionHandler;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.patient.application.interfaces.VaccineApplicationService;
import br.org.apae.api.patient.domain.exceptions.VaccineNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VaccineControllerImpl.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({
  SpringDataWebConfiguration.class,
  SecurityConfiguration.class,
  GlobalExceptionHandler.class
})
@Tag("patient")
@Tag("unit")
@Tag("controller")
public class VaccineControllerTest {

 @TestConfiguration
 @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
 static class ContextConfiguration {}

 @Autowired
 private MockMvc mockMvc;

 @MockitoBean
 private VaccineApplicationService vaccineService;

 @MockitoBean
 private JwtProvider jwtProvider;

 @MockitoBean
 private UserService userService;

 private static final String BASE_URL = "/vaccines";

 @BeforeEach
 void setupAuth() {
  AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);
 }

 @AfterEach
 void tearDown() {
  Mockito.reset(vaccineService, jwtProvider, userService);
 }

 @Nested
 @DisplayName("Cenários de Busca e Listagem (GET)")
 class BuscaEListagem {
  @Test
  @DisplayName("Deve listar todas as vacinas com sucesso (200)")
  void shouldGetAllVaccinesSuccess() throws Exception {
   VaccineResponseDTO v1 = new VaccineResponseDTO(UUID.randomUUID(), "BCG", false);
   VaccineResponseDTO v2 = new VaccineResponseDTO(UUID.randomUUID(), "Viral", true);

   when(vaccineService.findAllVaccines()).thenReturn(List.of(v1, v2));

   mockMvc.perform(get(BASE_URL).header("Authorization", AuthTestHelper.bearerToken()).accept(MediaType.APPLICATION_JSON))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$", hasSize(2)))
     .andExpect(jsonPath("$[0].name", is("BCG")));
  }

  @Test
  @DisplayName("Deve buscar vacina por ID com sucesso (200)")
  void shouldSearchVaccineByIdSuccess() throws Exception {
   UUID id = UUID.randomUUID();
   VaccineResponseDTO responseDto = new VaccineResponseDTO(id, "BCG", false);

   when(vaccineService.findVaccineById(id)).thenReturn(responseDto);

   mockMvc.perform(get(BASE_URL + "/{id}", id).header("Authorization", AuthTestHelper.bearerToken()).accept(MediaType.APPLICATION_JSON))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.id").value(id.toString()));
  }

  @Test
  @DisplayName("Deve retornar NotFound (404) ao buscar vacina por ID inexistente")
  void shouldReturnNotFoundWhenSearchVaccineByIdNonExistent() throws Exception {
   UUID id = UUID.randomUUID();
   when(vaccineService.findVaccineById(id)).thenThrow(new VaccineNotFoundException());
   mockMvc.perform(get(BASE_URL + "/{id}", id).header("Authorization", AuthTestHelper.bearerToken()).accept(MediaType.APPLICATION_JSON))
     .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Deve buscar vacina por nome com sucesso (200)")
  void shouldSearchVaccineByNameSuccess() throws Exception {
   String name = "BCG";
   when(vaccineService.findVaccineByName(name)).thenReturn(new VaccineResponseDTO(UUID.randomUUID(), name, false));

   mockMvc.perform(get(BASE_URL + "/search/by-name").header("Authorization", AuthTestHelper.bearerToken()).param("name", name).accept(MediaType.APPLICATION_JSON))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.name").value(name));
  }

  @Test
  @DisplayName("Deve retornar NotFound (404) ao buscar vacina por nome inexistente")
  void shouldReturnNotFoundWhenSearchVaccineByNameNonExistent() throws Exception {
   String name = "Inexistente";
   when(vaccineService.findVaccineByName(name)).thenThrow(new VaccineNotFoundException(name));
   mockMvc.perform(get(BASE_URL + "/search/by-name").header("Authorization", AuthTestHelper.bearerToken()).param("name", name).accept(MediaType.APPLICATION_JSON))
     .andExpect(status().isNotFound());
  }
 }

}
