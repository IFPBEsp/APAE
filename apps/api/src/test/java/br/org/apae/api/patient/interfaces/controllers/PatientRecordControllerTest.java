package br.org.apae.api.patient.interfaces.controllers;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.dto.assessment.AssessmentResponseDTO;
import br.org.apae.api.common.dto.report.ReportResponseDTO;
import br.org.apae.api.common.exceptions.handler.GlobalExceptionHandler;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.patient.application.interfaces.PatientRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientRecordController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({
  SecurityConfiguration.class,
  GlobalExceptionHandler.class
})
@Tag("patient")
@Tag("unit")
@Tag("controller")
class PatientRecordControllerTest {

 @Autowired
 private MockMvc mockMvc;

 @Autowired
 private ObjectMapper objectMapper;

 @MockitoBean
 private PatientRecordService patientRecordService;

 @MockitoBean
 private JwtProvider jwtProvider;

 @MockitoBean
 private UserService userService;

 private static final String BASE_URL = "/patients/{id}";

 @BeforeEach
 void setupAuth() {
  AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);
 }

 @AfterEach
 void tearDown() {
  Mockito.reset(patientRecordService, jwtProvider, userService);
 }

 private ReportResponseDTO createReportDTO(UUID patientId) {
  return new ReportResponseDTO(
    UUID.randomUUID(),
    patientId,
    "Aluno Teste",
    UUID.randomUUID(),
    "Professor Teste",
    UUID.randomUUID(),
    "Turma A",
    "Atividades",
    "Habilidades",
    "Estratégias",
    "Recursos",
    LocalDateTime.now());
 }

 private AssessmentResponseDTO createAssessmentDTO(UUID patientId) {
  return new AssessmentResponseDTO(
    UUID.randomUUID(),
    patientId,
    "Aluno Teste",
    UUID.randomUUID(),
    "Professor Teste",
    "Descrição da avaliação",
    LocalDateTime.now());
 }

 @Nested
 @DisplayName("Cenários de Busca de Relatórios (GET /patients/{id}/reports)")
 class Relatorios {

  @Test
  @DisplayName("Deve retornar a lista de relatórios do paciente com sucesso (200)")
  void shouldReturnReportsSuccessfully() throws Exception {
   UUID patientId = UUID.randomUUID();
   ReportResponseDTO report = createReportDTO(patientId);

   when(patientRecordService.getReportsByPatientId(patientId)).thenReturn(List.of(report));

   mockMvc.perform(get(BASE_URL + "/reports", patientId).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$", hasSize(1)))
     .andExpect(jsonPath("$[0].id", is(report.id().toString())))
     .andExpect(jsonPath("$[0].alunoId", is(patientId.toString())))
     .andExpect(jsonPath("$[0].turmaNome", is("Turma A")));

   verify(patientRecordService).getReportsByPatientId(patientId);
  }

  @Test
  @DisplayName("Deve retornar lista vazia quando o paciente não possui relatórios (200)")
  void shouldReturnEmptyListWhenNoReports() throws Exception {
   UUID patientId = UUID.randomUUID();

   when(patientRecordService.getReportsByPatientId(patientId)).thenReturn(Collections.emptyList());

   mockMvc.perform(get(BASE_URL + "/reports", patientId).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  @DisplayName("Deve retornar InternalServerError (500) quando o serviço lançar exceção")
  void shouldReturnErrorWhenServiceThrows() throws Exception {
   UUID patientId = UUID.randomUUID();

   when(patientRecordService.getReportsByPatientId(patientId))
     .thenThrow(new RuntimeException("Falha ao buscar relatórios"));

   mockMvc.perform(get(BASE_URL + "/reports", patientId).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isInternalServerError());
  }
 }

 @Nested
 @DisplayName("Cenários de Busca de Avaliações (GET /patients/{id}/assessments)")
 class Avaliacoes {

  @Test
  @DisplayName("Deve retornar a lista de avaliações do paciente com sucesso (200)")
  void shouldReturnAssessmentsSuccessfully() throws Exception {
   UUID patientId = UUID.randomUUID();
   AssessmentResponseDTO assessment = createAssessmentDTO(patientId);

   when(patientRecordService.getAssessmentByPatientId(patientId)).thenReturn(List.of(assessment));

   mockMvc.perform(get(BASE_URL + "/assessments", patientId).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$", hasSize(1)))
     .andExpect(jsonPath("$[0].id", is(assessment.id().toString())))
     .andExpect(jsonPath("$[0].alunoId", is(patientId.toString())))
     .andExpect(jsonPath("$[0].descricao", is("Descrição da avaliação")));

   verify(patientRecordService).getAssessmentByPatientId(patientId);
  }

  @Test
  @DisplayName("Deve retornar lista vazia quando o paciente não possui avaliações (200)")
  void shouldReturnEmptyListWhenNoAssessments() throws Exception {
   UUID patientId = UUID.randomUUID();

   when(patientRecordService.getAssessmentByPatientId(patientId)).thenReturn(Collections.emptyList());

   mockMvc.perform(get(BASE_URL + "/assessments", patientId).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  @DisplayName("Deve retornar InternalServerError (500) quando o serviço lançar exceção")
  void shouldReturnErrorWhenServiceThrows() throws Exception {
   UUID patientId = UUID.randomUUID();

   when(patientRecordService.getAssessmentByPatientId(patientId))
     .thenThrow(new RuntimeException("Falha ao buscar avaliações"));

   mockMvc.perform(get(BASE_URL + "/assessments", patientId).header("Authorization", AuthTestHelper.bearerToken()))
     .andExpect(status().isInternalServerError());
  }
 }
}
