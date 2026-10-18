package br.org.apae.api.appointment.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import br.org.apae.api.appointment.domain.model.Appointment;
import br.org.apae.api.appointment.domain.model.GeneratedAppointment;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.model.UserRole;
import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.UpdateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AnnualRegistryResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.GeneratedAppointmentResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.TodayAppointmentsResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.model.Disorder;
import br.org.apae.api.professional.domain.model.Availability;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.model.enums.Day;
import br.org.apae.api.professional.domain.model.enums.Shift;
import br.org.apae.api.servicearea.domain.model.ServiceArea;

class AppointmentMapperTest {

    private static final UUID APPOINTMENT_ID = UUID.fromString("aaaaaaaa-1111-2222-3333-444444444444");
    private static final UUID PATIENT_ID = UUID.fromString("bbbbbbbb-1111-2222-3333-444444444444");
    private static final UUID ANNUAL_REGISTRY_ID = UUID.fromString("cccccccc-1111-2222-3333-444444444444");
    private static final UUID PROFESSIONAL_ID = UUID.fromString("dddddddd-1111-2222-3333-444444444444");
    private static final UUID GENERATED_APPOINTMENT_ID = UUID.fromString("eeeeeeee-1111-2222-3333-444444444444");
    private static final Integer SERVICE_AREA_ID = 1;
    private static final String SERVICE_AREA_NAME = "Fisioterapia";
    private static final String BPC = "BPC-12345";
    private static final String DISEASES = "Diabetes e hipertensao";
    private static final Integer FREQUENCY_DAYS = 30;
    private static final LocalTime HOUR = LocalTime.of(14, 30);
    private static final LocalDate INITIAL_DATE = LocalDate.of(2026, 3, 1);
    private static final LocalDate END_DATE = LocalDate.of(2026, 12, 20);
    private static final LocalDateTime SCHEDULED = LocalDateTime.of(2026, 3, 17, 14, 30);
    private static final LocalDateTime OVERRIDDEN = LocalDateTime.of(2026, 3, 18, 10, 0);

    private AppointmentMapper appointmentMapper;

    @BeforeEach
    void setUp() {
        appointmentMapper = new AppointmentMapper();
    }

    private static User professionalUser() {
        return new User("profissional@apae.org.br", "senha", "123.456.789-09", "Joao da Silva", UserRole.ATENDIMENTO);
    }

    private static HealthProfessional professional() {
        HealthProfessional professional = new HealthProfessional(
                PROFESSIONAL_ID, professionalUser(), new ServiceArea(SERVICE_AREA_ID, SERVICE_AREA_NAME),
                "CREFITO-12345");
        professional.addAvailability(new Availability(Day.SEGUNDA, Shift.MANHA, professional));
        professional.addAvailability(new Availability(Day.TERCA, Shift.TARDE, professional));
        return professional;
    }

    private static HealthProfessional professionalWithoutServiceArea() {
        return new HealthProfessional(PROFESSIONAL_ID, professionalUser(), null, "CREFITO-12345");
    }

    private static AnnualRegistry annualRegistry() {
        return new AnnualRegistry(
                ANNUAL_REGISTRY_ID,
                BPC,
                DISEASES,
                null,
                new BigDecimal("1500.00"),
                2026,
                PATIENT_ID,
                Set.of(new Disorder(UUID.fromString("ffffffff-1111-2222-3333-444444444444"), "TEA")),
                Set.of(new ServiceArea(SERVICE_AREA_ID, SERVICE_AREA_NAME)));
    }

    private static Appointment appointment() {
        Appointment appointment = new Appointment(professional(), annualRegistry(), FREQUENCY_DAYS, HOUR, INITIAL_DATE, null);
        ReflectionTestUtils.setField(appointment, "id", APPOINTMENT_ID);
        return appointment;
    }

    private static GeneratedAppointment generatedAppointment() {
        GeneratedAppointment generated = new GeneratedAppointment(appointment(), SCHEDULED);
        generated.setId(GENERATED_APPOINTMENT_ID);
        ReflectionTestUtils.setField(generated, "patientId", PATIENT_ID);
        return generated;
    }

    private static PatientResponseDTO patient() {
        return new PatientResponseDTO(
                PATIENT_ID,
                "Maria Souza",
                "Sao Paulo",
                LocalDate.of(1990, 5, 10),
                "11988888888",
                "1234567890",
                "Cartorio Central",
                "FL-1",
                "Livro 1",
                "RG-1",
                LocalDate.of(2010, 3, 4),
                "SSP",
                "123.456.789-09",
                "1234567890",
                "10000000000",
                LocalDate.of(2026, 1, 5),
                "Nenhuma",
                true,
                false,
                null,
                null,
                List.of(),
                Set.of(),
                null);
    }

    private static CreateAppointmentDTO createDto() {
        return new CreateAppointmentDTO(PROFESSIONAL_ID, PATIENT_ID, FREQUENCY_DAYS, INITIAL_DATE, HOUR);
    }

    private static UpdateAppointmentDTO updateDto(UUID professionalId, UUID annualRegistrationId) {
        return new UpdateAppointmentDTO(professionalId, annualRegistrationId, null, 45, END_DATE, HOUR, null);
    }

    @Test
    @DisplayName("toEntity deve mapear os campos do DTO e iniciar como ativo, sem data final")
    void toEntity_ShouldMapAllFields() {
        HealthProfessional professional = professional();
        AnnualRegistry annualRegistry = annualRegistry();

        Appointment entity = appointmentMapper.toEntity(createDto(), professional, annualRegistry);

        assertNull(entity.getId());
        assertSame(professional, entity.getProfessional());
        assertSame(annualRegistry, entity.getAnnualRegistration());
        assertEquals(FREQUENCY_DAYS, entity.getFrequencyDays());
        assertEquals(HOUR, entity.getHour());
        assertEquals(INITIAL_DATE, entity.getInitialDate());
        assertNull(entity.getEndDate());
        assertTrue(entity.isActive());
    }

    @Test
    @DisplayName("toEntity nao usa os ids do DTO, que so chegam como entidades ja resolvidas")
    void toEntity_ShouldUseResolvedEntities_NotTheIdsFromTheDTO() {
        HealthProfessional other = new HealthProfessional(
                UUID.fromString("99999999-1111-2222-3333-444444444444"), professionalUser(),
                new ServiceArea(2, "Psicologia"), "CRP-1");
        AnnualRegistry anotherRegistry = new AnnualRegistry(
                UUID.fromString("88888888-1111-2222-3333-444444444444"), "BPC-1", "Asma", null,
                BigDecimal.TEN, 2025, PATIENT_ID, Set.of(), Set.of());

        Appointment entity = appointmentMapper.toEntity(createDto(), other, anotherRegistry);

        assertSame(other, entity.getProfessional());
        assertSame(anotherRegistry, entity.getAnnualRegistration());
        assertEquals(PROFESSIONAL_ID, createDto().professionalId());
    }

    @Test
    @DisplayName("updateEntity deve substituir todos os campos informados no DTO")
    void updateEntity_ShouldReplaceAllFields_WhenAllProvided() {
        Appointment appointment = appointment();
        HealthProfessional newProfessional = professional();
        AnnualRegistry newRegistry = annualRegistry();

        Appointment result = appointmentMapper.updateEntity(
                appointment, updateDto(PROFESSIONAL_ID, ANNUAL_REGISTRY_ID), newProfessional, newRegistry);

        assertSame(appointment, result);
        assertSame(newProfessional, result.getProfessional());
        assertSame(newRegistry, result.getAnnualRegistration());
        assertEquals(45, result.getFrequencyDays());
        assertEquals(END_DATE, result.getInitialDate());
        assertNull(result.getEndDate());
    }

    @Test
    @DisplayName("updateEntity deve preservar os valores atuais quando todos os opcionais do DTO sao nulos")
    void updateEntity_ShouldKeepCurrentValues_WhenAllOptionalFieldsAreNull() {
        Appointment appointment = appointment();
        HealthProfessional currentProfessional = appointment.getProfessional();
        AnnualRegistry currentRegistry = appointment.getAnnualRegistration();
        UpdateAppointmentDTO empty = new UpdateAppointmentDTO(null, null, null, null, null, null, null);

        Appointment result = appointmentMapper.updateEntity(appointment, empty, professional(), annualRegistry());

        assertSame(currentProfessional, result.getProfessional());
        assertSame(currentRegistry, result.getAnnualRegistration());
        assertEquals(FREQUENCY_DAYS, result.getFrequencyDays());
        assertEquals(HOUR, result.getHour());
        assertEquals(INITIAL_DATE, result.getInitialDate());
        assertNull(result.getEndDate());
        assertTrue(result.isActive());
    }

    @Test
    @DisplayName("updateEntity deve trocar apenas o profissional quando so o id do profissional vem preenchido")
    void updateEntity_ShouldReplaceOnlyProfessional_WhenOnlyProfessionalIdProvided() {
        Appointment appointment = appointment();
        AnnualRegistry currentRegistry = appointment.getAnnualRegistration();
        HealthProfessional newProfessional = new HealthProfessional(
                PROFESSIONAL_ID, professionalUser(), new ServiceArea(2, "Psicologia"), "CRP-2");
        UpdateAppointmentDTO dto = new UpdateAppointmentDTO(PROFESSIONAL_ID, null, null, null, null, null, null);

        Appointment result = appointmentMapper.updateEntity(appointment, dto, newProfessional, annualRegistry());

        assertSame(newProfessional, result.getProfessional());
        assertSame(currentRegistry, result.getAnnualRegistration());
        assertEquals(FREQUENCY_DAYS, result.getFrequencyDays());
    }

    @Test
    @DisplayName("updateEntity deve trocar apenas o cadastro anual quando so esse id vem preenchido")
    void updateEntity_ShouldReplaceOnlyAnnualRegistry_WhenOnlyAnnualRegistrationIdProvided() {
        Appointment appointment = appointment();
        HealthProfessional currentProfessional = appointment.getProfessional();
        AnnualRegistry newRegistry = new AnnualRegistry(
                UUID.fromString("77777777-1111-2222-3333-444444444444"), "BPC-9", "Depressao", null,
                BigDecimal.ONE, 2025, PATIENT_ID, Set.of(), Set.of());
        UpdateAppointmentDTO dto = new UpdateAppointmentDTO(null, ANNUAL_REGISTRY_ID, null, null, null, null, null);

        Appointment result = appointmentMapper.updateEntity(appointment, dto, professional(), newRegistry);

        assertSame(newRegistry, result.getAnnualRegistration());
        assertSame(currentProfessional, result.getProfessional());
    }

    @Test
    @DisplayName("updateEntity ignora o serviceId do DTO porque nao existe area no agendamento")
    void updateEntity_ShouldIgnoreServiceId() {
        Appointment appointment = appointment();
        HealthProfessional currentProfessional = appointment.getProfessional();
        UpdateAppointmentDTO dto = new UpdateAppointmentDTO(null, null,
                UUID.fromString("eeeeeeee-9999-8888-7777-666666666666"), null, null, null, null);

        Appointment result = appointmentMapper.updateEntity(appointment, dto, professional(), annualRegistry());

        assertSame(currentProfessional, result.getProfessional());
        assertSame(appointment.getAnnualRegistration(), result.getAnnualRegistration());
    }

    @Test
    @DisplayName("updateEntity deve aplicar a data final quando informada")
    void updateEntity_ShouldSetEndDate_WhenProvided() {
        Appointment appointment = appointment();

        Appointment result = appointmentMapper.updateEntity(
                appointment,
                new UpdateAppointmentDTO(null, null, null, null, null, null, END_DATE),
                professional(),
                annualRegistry());

        assertEquals(END_DATE, result.getEndDate());
        assertEquals(INITIAL_DATE, result.getInitialDate());
    }

    @Test
    @DisplayName("toGeneratedResponse deve mapcar os nove campos do agendamento gerado")
    void toGeneratedResponse_ShouldMapAllFields() {
        GeneratedAppointment generated = generatedAppointment();
        generated.setCancellationReason("Remarcado a pedido do paciente");

        GeneratedAppointmentResponseDTO dto = appointmentMapper.toGeneratedResponse(generated);

        assertEquals(GENERATED_APPOINTMENT_ID, dto.id());
        assertEquals(APPOINTMENT_ID, dto.appointmentId());
        assertEquals(SCHEDULED, dto.scheduledDateTime());
        assertNull(dto.overriddenDateTime());
        assertFalse(dto.performed());
        assertFalse(dto.cancelled());
        assertEquals("Remarcado a pedido do paciente", dto.cancellationReason());
        assertEquals(PATIENT_ID, dto.patientId());
        assertEquals(SCHEDULED, dto.effectiveDateTime());
    }

    @Test
    @DisplayName("toGeneratedResponse deve usar a data sobrescrita como data efetiva quando existir")
    void toGeneratedResponse_ShouldUseOverriddenDateTimeAsEffectiveDateTime() {
        GeneratedAppointment generated = generatedAppointment();
        generated.setOverriddenDateTime(OVERRIDDEN);
        generated.setPerformed(true);

        GeneratedAppointmentResponseDTO dto = appointmentMapper.toGeneratedResponse(generated);

        assertEquals(OVERRIDDEN, dto.overriddenDateTime());
        assertEquals(OVERRIDDEN, dto.effectiveDateTime());
        assertEquals(SCHEDULED, dto.scheduledDateTime());
        assertTrue(dto.performed());
    }

    @Test
    @DisplayName("toResponse de cadastro anual deve mapear todos os campos e os transtornos com hasPatient falso")
    void toResponse_AnnualRegistry_ShouldMapAllFields() {
        PatientResponseDTO patient = patient();

        AnnualRegistryResponseDTO dto = appointmentMapper.toResponse(annualRegistry(), patient);

        assertEquals(ANNUAL_REGISTRY_ID, dto.id());
        assertEquals(BPC, dto.bpc());
        assertEquals(DISEASES, dto.diseases());
        assertEquals(new BigDecimal("1500.00"), dto.familyIncome());
        assertEquals(Year.of(2026), dto.year());
        assertSame(patient, dto.patient());
        assertEquals(1, dto.disorders().size());
        assertEquals("TEA", dto.disorders().get(0).name());
        assertEquals(UUID.fromString("ffffffff-1111-2222-3333-444444444444"), dto.disorders().get(0).id());
        assertFalse(dto.disorders().get(0).hasPatient());
    }

    @Test
    @DisplayName("toResponse de cadastro anual deve devolver lista de transtornos vazia quando nao ha nenhum")
    void toResponse_AnnualRegistry_ShouldReturnEmptyDisorders_WhenNoneExist() {
        AnnualRegistry registry = new AnnualRegistry(
                ANNUAL_REGISTRY_ID, BPC, DISEASES, null, BigDecimal.TEN, 2024, PATIENT_ID, Set.of(), Set.of());

        AnnualRegistryResponseDTO dto = appointmentMapper.toResponse(registry, patient());

        assertTrue(dto.disorders().isEmpty());
        assertEquals(Year.of(2024), dto.year());
    }

    @Test
    @DisplayName("toResponse de agendamento deve mapear todos os campos, com aninhamento de profissional e cadastro")
    void toResponse_Appointment_ShouldMapAllFields() {
        PatientResponseDTO patient = patient();

        AppointmentResponseDTO dto = appointmentMapper.toResponse(appointment(), patient);

        assertEquals(APPOINTMENT_ID, dto.id());
        assertEquals(FREQUENCY_DAYS, dto.frequencyDays());
        assertEquals(INITIAL_DATE, dto.initialDate());
        assertNull(dto.endDate());
        assertEquals(HOUR, dto.hour());
        assertTrue(dto.isActive());
        assertNull(dto.replacedByDate());
        assertNull(dto.updatedFromDate());

        assertNotNull(dto.professional());
        assertEquals(PROFESSIONAL_ID, dto.professional().id());
        assertEquals("Joao da Silva", dto.professional().name());
        assertEquals(SERVICE_AREA_NAME, dto.professional().healthSector());
        assertEquals(SERVICE_AREA_ID, dto.professional().serviceArea().id());
        assertEquals(SERVICE_AREA_NAME, dto.professional().serviceArea().area());
        assertEquals(2, dto.professional().availabilities().size());
        assertEquals("SEGUNDA", dto.professional().availabilities().get(0).day());
        assertEquals("TARDE", dto.professional().availabilities().get(1).shift());

        assertNotNull(dto.annualRegistration());
        assertEquals(ANNUAL_REGISTRY_ID, dto.annualRegistration().id());
        assertEquals(Year.of(2026), dto.annualRegistration().year());
        assertSame(patient, dto.annualRegistration().patient());
    }

    @Test
    @DisplayName("toResponse de agendamento deve expor a data inicial do agendamento substituido e do original")
    void toResponse_Appointment_ShouldMapReplacedByAndUpdatedFromDates() {
        Appointment appointment = appointment();
        Appointment replacing = new Appointment(appointment.getProfessional(), appointment.getAnnualRegistration(),
                15, LocalTime.of(9, 0), LocalDate.of(2026, 6, 1), null);
        Appointment updatedFrom = new Appointment(appointment.getProfessional(), appointment.getAnnualRegistration(),
                60, LocalTime.of(16, 0), LocalDate.of(2025, 9, 1), null);
        appointment.setReplacedBy(replacing);
        appointment.setUpdatedFrom(updatedFrom);
        appointment.setActive(false);

        AppointmentResponseDTO dto = appointmentMapper.toResponse(appointment, patient());

        assertEquals(LocalDate.of(2026, 6, 1), dto.replacedByDate());
        assertEquals(LocalDate.of(2025, 9, 1), dto.updatedFromDate());
        assertFalse(dto.isActive());
    }

    @Test
    @DisplayName("toResponse de agendamento deixa creationDate nula, pois so o JPA preenche esse campo")
    void toResponse_Appointment_ShouldLeaveCreationDateNull() {
        Appointment appointment = appointment();

        AppointmentResponseDTO dto = appointmentMapper.toResponse(appointment, patient());

        assertNull(appointment.getCreationDate());
        assertNull(dto.creationDate());
    }

    @Test
    @DisplayName("toResponse de agendamento exige area de atendimento no profissional")
    void toResponse_Appointment_ShouldThrowNullPointerException_WhenServiceAreaIsNull() {
        Appointment appointment = new Appointment(
                professionalWithoutServiceArea(), annualRegistry(), FREQUENCY_DAYS, HOUR, INITIAL_DATE, null);

        assertThrows(NullPointerException.class, () -> appointmentMapper.toResponse(appointment, patient()));
    }

    @Test
    @DisplayName("toTodayResponseDTO deve mapcar os onze campos, com ruleId vindo do agendamento")
    void toTodayResponseDTO_ShouldMapAllFields() {
        GeneratedAppointment generated = generatedAppointment();
        PatientResponseDTO patient = patient();

        TodayAppointmentsResponseDTO dto = appointmentMapper.toTodayResponseDTO(generated, patient, false);

        assertEquals(GENERATED_APPOINTMENT_ID, dto.id());
        assertSame(patient, dto.patient());
        assertEquals(PROFESSIONAL_ID, dto.professional().id());
        assertEquals(SERVICE_AREA_NAME, dto.professional().serviceArea().area());
        assertEquals(2, dto.professional().availabilities().size());
        assertEquals(SCHEDULED, dto.scheduledDateTime());
        assertNull(dto.overriddenDateTime());
        assertFalse(dto.performed());
        assertFalse(dto.cancelled());
        assertNull(dto.cancellationReason());
        assertEquals(SCHEDULED, dto.effectiveDateTime());
        assertEquals(APPOINTMENT_ID, dto.ruleId());
        assertFalse(dto.hasAbsence());
    }

    @Test
    @DisplayName("toTodayResponseDTO deve propagar a data sobrescrita, o cancelamento e a existencia de falta")
    void toTodayResponseDTO_ShouldMapCancellationAndAbsenceFlag() {
        GeneratedAppointment generated = generatedAppointment();
        generated.setOverriddenDateTime(OVERRIDDEN);
        generated.setCancelled(true);
        generated.setCancellationReason("Falta do profissional");
        generated.setPerformed(true);

        TodayAppointmentsResponseDTO dto = appointmentMapper.toTodayResponseDTO(generated, patient(), true);

        assertEquals(OVERRIDDEN, dto.overriddenDateTime());
        assertEquals(OVERRIDDEN, dto.effectiveDateTime());
        assertTrue(dto.cancelled());
        assertTrue(dto.performed());
        assertEquals("Falta do profissional", dto.cancellationReason());
        assertTrue(dto.hasAbsence());
    }
}
