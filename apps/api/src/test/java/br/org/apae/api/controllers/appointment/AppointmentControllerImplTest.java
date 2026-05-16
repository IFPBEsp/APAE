package br.org.apae.api.controllers.appointment;
import br.org.apae.api.appointment.application.interfaces.AppointmentApplicationService;
import br.org.apae.api.appointment.application.internal.AppointmentApplicationServiceImpl;
import br.org.apae.api.appointment.domain.exceptions.AppointmentAlreadyCancelledException;
import br.org.apae.api.appointment.domain.exceptions.AppointmentNotFoundException;
import br.org.apae.api.appointment.domain.model.GeneratedAppointment;
import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.common.dto.appointment.request.appointment.*;
import br.org.apae.api.common.dto.appointment.response.appointment.AnnualRegistryResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.GeneratedAppointmentResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.TodayAppointmentsResponseDTO;

import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ AppointmentControllerImpl.class })
class AppointmentControllerImplTest {

  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private AppointmentApplicationService service;
  @Autowired
  private ObjectMapper objectMapper;
  @MockitoBean
  private JwtProvider jwtProvider;
  @MockitoBean
  private UserService userService;
  private static final String URI = "/appointments";
  private static final String URI_WITH_ID = URI + "/{id}";
  private static final String GENERATED_URI_WITH_ID = URI + "/generated/{id}";

  private AppointmentResponseDTO createAppointmentDTO(UUID id) {
    return new AppointmentResponseDTO(
            id,
            mock(HealthProfessionalResponseDTO.class),
            mock(AnnualRegistryResponseDTO.class),
            7,
            LocalDate.now(),
            LocalDate.of(2026, 12, 31),
            LocalTime.now().withNano(0),
            true,
            LocalDateTime.now().withNano(0),
            null,
            null
    );
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldCreateAppointmentSuccessfully() throws Exception {
    var payload = new CreateAppointmentDTO(
        UUID.randomUUID(),
        UUID.randomUUID(),
        2,
        LocalDate.now(),
        LocalTime.now()
    );

    mockMvc.perform(post(URI)
            .content(objectMapper.writeValueAsString(payload))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldGetAppointmentByIdSuccessfully() throws Exception {
    UUID id = UUID.randomUUID();

    AppointmentResponseDTO response = createAppointmentDTO(id);

    when(service.findById(id)).thenReturn(response);

    mockMvc.perform(get(URI_WITH_ID, id)
        .contentType(MediaType.APPLICATION_JSON)
        .with(csrf()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(response.id().toString()))
      .andExpect(jsonPath("$.frequencyDays").value(response.frequencyDays()))
      .andExpect(jsonPath("$.initialDate").value(response.initialDate().toString()))
      .andExpect(jsonPath("$.endDate").value(response.endDate().toString()))
      .andExpect(jsonPath("$.hour").value(response.hour().toString()))
      .andExpect(jsonPath("$.isActive").value(response.isActive()))
      .andExpect(jsonPath("$.creationDate").value(response.creationDate().toString()));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldDeleteAppointmentSuccessfully() throws Exception {
    UUID id = UUID.randomUUID();

    mockMvc.perform(delete(URI_WITH_ID, id)
            .with(csrf()))
        .andExpect(status().isNoContent());

    verify(service, times(1)).delete(id);
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldRescheduleAppointmentSuccessfully() throws Exception {
    UUID id = UUID.randomUUID();
    var response = new GeneratedAppointmentResponseDTO(
            id,
            UUID.randomUUID(),
            LocalDateTime.now().withNano(0),
            null,
            false,
            false,
            null,
            UUID.randomUUID(),
            LocalDateTime.now().withNano(0)
    );

    LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
    var payload = new RescheduleGeneratedAppointmentDTO(localDateTime);

    when(service.reschedule(id, payload.newDateTime())).thenReturn(response);

    mockMvc.perform(patch(GENERATED_URI_WITH_ID + "/reschedule", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(payload))
        .with(csrf()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(response.id().toString()))
      .andExpect(jsonPath("$.appointmentId").value(response.appointmentId().toString()))
      .andExpect(jsonPath("$.scheduledDateTime").value(response.scheduledDateTime().toString()))
      .andExpect(jsonPath("$.overriddenDateTime").isEmpty())
      .andExpect(jsonPath("$.performed").isBoolean())
      .andExpect(jsonPath("$.cancelled").isBoolean())
      .andExpect(jsonPath("$.cancellationReason").isEmpty())
      .andExpect(jsonPath("$.patientId").value(response.patientId().toString()))
      .andExpect(jsonPath("$.effectiveDateTime").value(response.effectiveDateTime().toString()));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldPerformedAppointmentSuccessfully() throws Exception {
    UUID id = UUID.randomUUID();
    var response = new GeneratedAppointmentResponseDTO(
            id,
            UUID.randomUUID(),
            null,
            null,
            false,
            false,
            null,
            UUID.randomUUID(),
            LocalDateTime.now().withNano(0)
    );

    when(service.markAsPerformed(id)).thenReturn(response);

    mockMvc.perform(patch(GENERATED_URI_WITH_ID + "/performed", id)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(response.id().toString()))
        .andExpect(jsonPath("$.appointmentId").value(response.appointmentId().toString()))
        .andExpect(jsonPath("$.scheduledDateTime").isEmpty())
        .andExpect(jsonPath("$.overriddenDateTime").isEmpty())
        .andExpect(jsonPath("$.performed").value(response.performed()))
        .andExpect(jsonPath("$.cancelled").value(response.cancelled()))
        .andExpect(jsonPath("$.cancellationReason").isEmpty())
        .andExpect(jsonPath("$.patientId").value(response.patientId().toString()))
        .andExpect(jsonPath("$.effectiveDateTime").value(response.effectiveDateTime().toString()));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldCancelAppointmentSuccessfully() throws Exception {
    UUID id = UUID.randomUUID();
    var payload = new CancelGeneratedAppointmentDTO("cancel reason");

    var response = new GeneratedAppointmentResponseDTO(
        id,
        UUID.randomUUID(),
        null,
        null,
        false,
        true,
        payload.reason(),
        UUID.randomUUID(),
        LocalDateTime.now().withNano(0)
    );

    when(service.cancel(id, payload.reason())).thenReturn(response);

    mockMvc.perform(patch(GENERATED_URI_WITH_ID + "/cancel", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(payload))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(response.id().toString()))
        .andExpect(jsonPath("$.appointmentId").value(response.appointmentId().toString()))
        .andExpect(jsonPath("$.scheduledDateTime").isEmpty())
        .andExpect(jsonPath("$.overriddenDateTime").isEmpty())
        .andExpect(jsonPath("$.performed").value(response.performed()))
        .andExpect(jsonPath("$.cancelled").value(response.cancelled()))
        .andExpect(jsonPath("$.cancellationReason").value(response.cancellationReason()))
        .andExpect(jsonPath("$.patientId").value(response.patientId().toString()))
        .andExpect(jsonPath("$.effectiveDateTime").value(response.effectiveDateTime().toString()));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldUpdateAppointmentRuleSuccessfully() throws Exception {
    UUID id = UUID.randomUUID();

    var payload = new UpdateAppointmentDTO(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            14,
            LocalDate.now().plusDays(1),
            LocalTime.of(10, 30, 0),
            LocalDate.now().plusDays(30)
    );

    var response = new AppointmentResponseDTO(
            id,
            mock(HealthProfessionalResponseDTO.class),
            mock(AnnualRegistryResponseDTO.class),
            payload.frequencyDays(),
            payload.initialDate(),
            payload.endDate(),
            payload.hour(),
            true,
            LocalDateTime.now().withNano(0),
            null,
            null
    );

    when(service.update(id, payload)).thenReturn(response);

    mockMvc.perform(patch(URI_WITH_ID, id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(payload))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(response.id().toString()))
        .andExpect(jsonPath("$.frequencyDays").value(response.frequencyDays()))
        .andExpect(jsonPath("$.hour").value(response.hour().format(DateTimeFormatter.ISO_LOCAL_TIME)))
        .andExpect(jsonPath("$.isActive").value(response.isActive()))
        .andExpect(jsonPath("$.creationDate").value(response.creationDate().toString()));

    verify(service, times(1)).update(eq(id), any(UpdateAppointmentDTO.class));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldListGeneratedAppointmentsByPatientSuccessfully() throws Exception {
    UUID patientId = UUID.randomUUID();

    LocalDate start = LocalDate.of(2026, 1, 1);
    LocalDate end = LocalDate.of(2026, 1, 31);

    var response = new GeneratedAppointmentResponseDTO(
        UUID.randomUUID(),
        UUID.randomUUID(),
        LocalDateTime.now().withNano(0),
        null,
        false,
        false,
        null,
        patientId,
        null
    );

    Page<GeneratedAppointmentResponseDTO> page =
        new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

    when(service.listByPatient(eq(patientId), eq(start), eq(end), any(Pageable.class)))
        .thenReturn(page);

    mockMvc.perform(get(URI + "/patient/{patientId}", patientId)
            .param("start", start.toString())
            .param("end", end.toString())
            .param("page", "0")
            .param("size", "10")
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].id").value(response.id().toString()))
        .andExpect(jsonPath("$.content[0].appointmentId").value(response.appointmentId().toString()))
        .andExpect(jsonPath("$.content[0].scheduledDateTime").value(response.scheduledDateTime().toString()))
        .andExpect(jsonPath("$.content[0].performed").value(response.performed()))
        .andExpect(jsonPath("$.content[0].cancelled").value(response.cancelled()))
        .andExpect(jsonPath("$.content[0].patientId").value(response.patientId().toString()));

    verify(service, times(1))
        .listByPatient(eq(patientId), eq(start), eq(end), any(Pageable.class));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldListTodayAppointmentsSuccessfully() throws Exception {
    LocalDate date = LocalDate.now();

    var response = new TodayAppointmentsResponseDTO(
        UUID.randomUUID(),
        mock(PatientResponseDTO.class),
        mock(HealthProfessionalResponseDTO.class),
        LocalDateTime.now().withNano(0),
        null,
        false,
        false,
        null,
        null,
        UUID.randomUUID(),
        false
    );

    Page<TodayAppointmentsResponseDTO> page =
        new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

    when(service.listAppointmentForToday(eq(date), any(Pageable.class)))
        .thenReturn(page);

    mockMvc.perform(get(URI + "/today")
            .param("date", date.toString())
            .param("page", "0")
            .param("size", "10")
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].id").value(response.id().toString()))
        .andExpect(jsonPath("$.content[0].performed").value(response.performed()))
        .andExpect(jsonPath("$.content[0].cancelled").value(response.cancelled()))
        .andExpect(jsonPath("$.content[0].ruleId").value(response.ruleId().toString()));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldGetAllAppointmentsSuccessfully() throws Exception {
    LocalDate date = LocalDate.of(2026, 1, 21);
    LocalTime time = LocalTime.of(10, 30);

    AppointmentResponseDTO response1 = createAppointmentDTO(UUID.randomUUID()) ;

    AppointmentResponseDTO response2 = createAppointmentDTO(UUID.randomUUID());
    Page<AppointmentResponseDTO> page =
        new PageImpl<>(List.of(response1, response2), PageRequest.of(0, 10), 2);

    when(service.findAll(eq(date), eq(time), any(Pageable.class)))
        .thenReturn(page);

    mockMvc.perform(get(URI)
            .param("date", date.toString())
            .param("time", time.format(DateTimeFormatter.ISO_LOCAL_TIME))
            .param("page", "0")
            .param("size", "10")
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].id").value(response1.id().toString()))
        .andExpect(jsonPath("$.content[0].frequencyDays").value(response1.frequencyDays()))
        .andExpect(jsonPath("$.content[0].hour").value(
            response1.hour().format(DateTimeFormatter.ISO_LOCAL_TIME)))
        .andExpect(jsonPath("$.content[1].id").value(response2.id().toString()))
        .andExpect(jsonPath("$.content[1].frequencyDays").value(response2.frequencyDays()))
        .andExpect(jsonPath("$.content[1].hour").value(
            response2.hour().format(DateTimeFormatter.ISO_LOCAL_TIME)));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldReturnNotFoundWhenGetNonExistentAppointment() throws Exception {
    UUID id = UUID.randomUUID();

    when(service.findById(id)).thenThrow(new AppointmentNotFoundException());

    mockMvc.perform(get(URI_WITH_ID, id)
                    .with(csrf()))
            .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldReturnNotFoundWhenReschedulingNonExistentAppointment() throws Exception {
    UUID id = UUID.randomUUID();
    var payload = new RescheduleGeneratedAppointmentDTO(LocalDateTime.now().plusDays(1));

    when(service.reschedule(any(), any())).thenThrow(new AppointmentNotFoundException());

    mockMvc.perform(patch(GENERATED_URI_WITH_ID + "/reschedule", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
                    .with(csrf()))
            .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldReturnBadRequestWhenCancellingAlreadyCancelledAppointment() throws Exception {
    UUID id = UUID.randomUUID();
    var payload = new CancelGeneratedAppointmentDTO("Motivo qualquer");

    when(service.cancel(eq(id), anyString())).thenThrow(new AppointmentAlreadyCancelledException());

    mockMvc.perform(patch(GENERATED_URI_WITH_ID + "/cancel", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
                    .with(csrf()))
            .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldReturnBadRequestWhenProfessionalIdIsNull() throws Exception {
    var payload = new CreateAppointmentDTO(
        UUID.randomUUID(),
        null,
        7,
        LocalDate.now().plusDays(1),
        LocalTime.now()
    );

    mockMvc.perform(post(URI)
            .content(objectMapper.writeValueAsString(payload))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldReturnBadRequestWhenPatientIdIsNull() throws Exception {
    var payload = new CreateAppointmentDTO(
            UUID.randomUUID(),
            null,
            7,
            LocalDate.now().plusDays(1),
            LocalTime.now()
    );

    mockMvc.perform(post(URI)
            .content(objectMapper.writeValueAsString(payload))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldReturnBadRequestWhenFrequencyDaysIsNull() throws Exception {
    var payload = new CreateAppointmentDTO(
            UUID.randomUUID(),
            UUID.randomUUID(),
            null,
            LocalDate.now().plusDays(1),
            LocalTime.now()
    );

    mockMvc.perform(post(URI)
            .content(objectMapper.writeValueAsString(payload))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldReturnBadRequestWhenFrequencyDaysIsZero() throws Exception {

    var payload = new CreateAppointmentDTO(
            UUID.randomUUID(),
            UUID.randomUUID(),
            0,
            LocalDate.now().plusDays(1),
            LocalTime.now()
    );

    mockMvc.perform(post(URI)
            .content(objectMapper.writeValueAsString(payload))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void shouldReturnBadRequestWhenFrequencyDaysIsNegative() throws Exception {
    var payload = new CreateAppointmentDTO(
            UUID.randomUUID(),
            UUID.randomUUID(),
            -5,
            LocalDate.now().plusDays(1),
            LocalTime.now()
    );

    mockMvc.perform(post(URI)
            .content(objectMapper.writeValueAsString(payload))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isBadRequest());
  }

//  @Test
//  @WithMockUser(username = "admin", roles = {"ADMIN"})
//  void shouldReturnBadRequestWhenInitialDateIsNull() throws Exception {
//    var payload = new CreateAppointmentDTO(
//        UUID.randomUUID(),
//        UUID.randomUUID(),
//        UUID.randomUUID(),
//        7,
//        null,
//        LocalTime.now()
//    );
//
//    mockMvc.perform(post(URI)
//            .content(objectMapper.writeValueAsString(payload))
//            .contentType(MediaType.APPLICATION_JSON)
//            .with(csrf()))
//        .andExpect(status().isBadRequest());
//  }
//
//  @Test
//  @WithMockUser(username = "admin", roles = {"ADMIN"})
//  void shouldReturnBadRequestWhenInitialDateIsInThePast() throws Exception {
//    var payload = new CreateAppointmentDTO(
//        UUID.randomUUID(),
//        UUID.randomUUID(),
//        UUID.randomUUID(),
//        7,
//        LocalDate.now().minusDays(1),
//        LocalTime.now()
//    );
//
//    mockMvc.perform(post(URI)
//            .content(objectMapper.writeValueAsString(payload))
//            .contentType(MediaType.APPLICATION_JSON)
//            .with(csrf()))
//        .andExpect(status().isBadRequest());
//  }
//
//  @Test
//  @WithMockUser(username = "admin", roles = {"ADMIN"})
//  void shouldReturnBadRequestWhenHourIsNull() throws Exception {
//    var payload = new CreateAppointmentDTO(
//        UUID.randomUUID(),
//        UUID.randomUUID(),
//        UUID.randomUUID(),
//        7,
//        LocalDate.now().plusDays(1),
//        null
//    );
//
//    mockMvc.perform(post(URI)
//            .content(objectMapper.writeValueAsString(payload))
//            .contentType(MediaType.APPLICATION_JSON)
//            .with(csrf()))
//        .andExpect(status().isBadRequest());
//  }
//
//  @Test
//  @WithMockUser(username = "admin", roles = {"ADMIN"})
//  void shouldReturnBadRequestWhenCreateBodyIsMissing() throws Exception {
//    mockMvc.perform(post(URI)
//            .contentType(MediaType.APPLICATION_JSON)
//            .with(csrf()))
//        .andExpect(status().isBadRequest());
//  }
}
