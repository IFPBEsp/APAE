package br.org.apae.api.appointment.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import br.org.apae.api.appointment.domain.model.Absence;
import br.org.apae.api.appointment.domain.model.Appointment;
import br.org.apae.api.appointment.domain.model.GeneratedAppointment;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.model.UserRole;
import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.servicearea.domain.model.ServiceArea;

class AbsenceMapperTest {

    private static final UUID ABSENCE_ID = UUID.fromString("11111111-2222-3333-4444-555555555555");
    private static final UUID GENERATED_APPOINTMENT_ID = UUID.fromString("66666666-7777-8888-9999-aaaaaaaaaaaa");
    private static final UUID PATIENT_ID = UUID.fromString("bbbbbbbb-cccc-dddd-eeee-ffffffffffff");
    private static final UUID PROFESSIONAL_ID = UUID.fromString("12345678-1234-1234-1234-123456789012");
    private static final LocalDate ABSENCE_DATE = LocalDate.of(2026, 3, 17);
    private static final String JUSTIFICATION = "Atendimento medico urgente";
    private static final String DOCUMENT_ID = "documento-123";

    private AbsenceMapper absenceMapper;
    private Appointment appointment;
    private GeneratedAppointment generatedAppointment;

    @BeforeEach
    void setUp() {
        absenceMapper = new AbsenceMapper();

        HealthProfessional professional = new HealthProfessional(
                PROFESSIONAL_ID,
                new User("profissional@apae.org.br", "senha", "123.456.789-09", "Joao da Silva", UserRole.ATENDIMENTO),
                new ServiceArea("Fisioterapia"),
                "CREFITO-123");

        appointment = new Appointment(
                professional,
                null,
                30,
                LocalTime.of(14, 0),
                LocalDate.of(2026, 3, 1),
                null);

        generatedAppointment = new GeneratedAppointment(appointment, LocalDateTime.of(2026, 3, 17, 14, 0));
        generatedAppointment.setId(GENERATED_APPOINTMENT_ID);
        ReflectionTestUtils.setField(generatedAppointment, "patientId", PATIENT_ID);
    }

    @Test
    @DisplayName("toEntity deve mapear data, justificativa, justificacao e documento")
    void toEntity_ShouldMapAllFields() {
        CreateAbsenceDTO dto = new CreateAbsenceDTO(
                GENERATED_APPOINTMENT_ID,
                ABSENCE_DATE,
                true,
                JUSTIFICATION,
                DOCUMENT_ID);

        Absence entity = absenceMapper.toEntity(dto);

        assertEquals(ABSENCE_DATE, entity.getAbsenceDate());
        assertEquals(JUSTIFICATION, entity.getJustification());
        assertEquals(Boolean.TRUE, entity.getIsJustified());
        assertEquals(DOCUMENT_ID, entity.getJustificationDocumentId());
    }

    @Test
    @DisplayName("toEntity descarta o generatedAppointmentId do DTO e deixa a associacao nula")
    void toEntity_ShouldLeaveGeneratedAppointmentNull() {
        CreateAbsenceDTO dto = new CreateAbsenceDTO(
                GENERATED_APPOINTMENT_ID,
                ABSENCE_DATE,
                true,
                JUSTIFICATION,
                DOCUMENT_ID);

        Absence entity = absenceMapper.toEntity(dto);

        assertNull(entity.getGeneratedAppointment());
    }

    @Test
    @DisplayName("toEntity deve iniciar notificado como falso")
    void toEntity_ShouldSetNotifiedFalseByDefault() {
        CreateAbsenceDTO dto = new CreateAbsenceDTO(
                GENERATED_APPOINTMENT_ID,
                ABSENCE_DATE,
                true,
                JUSTIFICATION,
                DOCUMENT_ID);

        Absence entity = absenceMapper.toEntity(dto);

        assertFalse(entity.getNotified());
    }

    @Test
    @DisplayName("toEntity deve propagar justificativa e documento nulos")
    void toEntity_ShouldAllowNullJustificationAndDocument() {
        CreateAbsenceDTO dto = new CreateAbsenceDTO(
                GENERATED_APPOINTMENT_ID,
                ABSENCE_DATE,
                false,
                null,
                null);

        Absence entity = absenceMapper.toEntity(dto);

        assertNull(entity.getJustification());
        assertNull(entity.getJustificationDocumentId());
        assertEquals(Boolean.FALSE, entity.getIsJustified());
        assertEquals(ABSENCE_DATE, entity.getAbsenceDate());
    }

    @Test
    @DisplayName("toEntity deve propagar isJustified nulo mesmo com coluna obrigatoria no banco")
    void toEntity_ShouldPropagateNullIsJustified() {
        CreateAbsenceDTO dto = new CreateAbsenceDTO(
                GENERATED_APPOINTMENT_ID,
                ABSENCE_DATE,
                null,
                JUSTIFICATION,
                DOCUMENT_ID);

        Absence entity = absenceMapper.toEntity(dto);

        assertNull(entity.getIsJustified());
    }

    @Test
    @DisplayName("toAbsenceResponse deve derivar paciente e profissional a partir do agendamento gerado")
    void toAbsenceResponse_ShouldMapAllFieldsIncludingPatientAndProfessionalIds() {
        Absence absence = absenceMapper.toEntity(new CreateAbsenceDTO(
                GENERATED_APPOINTMENT_ID,
                ABSENCE_DATE,
                true,
                JUSTIFICATION,
                DOCUMENT_ID));
        absence.setId(ABSENCE_ID);
        absence.setGeneratedAppointment(generatedAppointment);
        absence.setNotified(true);

        AbsenceResponseDTO dto = absenceMapper.toAbsenceResponse(absence);

        assertEquals(ABSENCE_ID, dto.id());
        assertEquals(GENERATED_APPOINTMENT_ID, dto.generatedAppointmentId());
        assertEquals(PATIENT_ID, dto.patientId());
        assertEquals(PROFESSIONAL_ID, dto.professionalId());
        assertEquals(ABSENCE_DATE, dto.absenceDate());
        assertEquals(JUSTIFICATION, dto.justification());
        assertEquals(Boolean.TRUE, dto.notified());
        assertEquals(Boolean.TRUE, dto.isJustified());
        assertEquals(DOCUMENT_ID, dto.justificationDocumentId());
    }

    @Test
    @DisplayName("toAbsenceResponse deve propagar justificativa e documento nulos")
    void toAbsenceResponse_ShouldMapNullJustificationAndDocument() {
        Absence absence = new Absence(generatedAppointment, ABSENCE_DATE, null);
        absence.setId(ABSENCE_ID);
        absence.setIsJustified(false);

        AbsenceResponseDTO dto = absenceMapper.toAbsenceResponse(absence);

        assertNull(dto.justification());
        assertNull(dto.justificationDocumentId());
        assertEquals(Boolean.FALSE, dto.notified());
        assertEquals(Boolean.FALSE, dto.isJustified());
    }

    @Test
    @DisplayName("toAbsenceResponse falha quando a falta nao tem agendamento gerado, como o proprio toEntity produz")
    void toAbsenceResponse_ShouldThrowNullPointerException_WhenGeneratedAppointmentIsNull() {
        Absence absence = absenceMapper.toEntity(new CreateAbsenceDTO(
                GENERATED_APPOINTMENT_ID,
                ABSENCE_DATE,
                true,
                JUSTIFICATION,
                DOCUMENT_ID));

        assertNull(absence.getGeneratedAppointment());
        assertThrows(NullPointerException.class, () -> absenceMapper.toAbsenceResponse(absence));
    }

    @Test
    @DisplayName("toAbsenceResponse deve preservar a mesma instancia de agendamento gerado")
    void toAbsenceResponse_ShouldReadFromTheSameGeneratedAppointmentInstance() {
        Absence absence = new Absence(generatedAppointment, ABSENCE_DATE, JUSTIFICATION);
        absence.setId(ABSENCE_ID);

        assertSame(generatedAppointment, absence.getGeneratedAppointment());
        assertEquals(GENERATED_APPOINTMENT_ID, absenceMapper.toAbsenceResponse(absence).generatedAppointmentId());
    }
}
