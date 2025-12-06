package br.org.apae.api.common.dto.appointment.request.appointment;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de validação para CreateAppointmentDTO")
class CreateAppointmentDTOTest {

  private static Validator validator;

  @BeforeAll
  static void setup() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  private CreateAppointmentDTO validDTO() {
    return new CreateAppointmentDTO(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        7,
        LocalDate.now(),
        LocalTime.of(10, 0)
    );
  }

  @Test
  @DisplayName("Deve validar com sucesso quando o DTO é válido")
  void shouldValidateSuccessfullyWhenDTOIsValid() {
    var dto = validDTO();
    var violations = validator.validate(dto);

    assertTrue(violations.isEmpty(), "DTO válido não deveria gerar violações");
  }

  @Test
  @DisplayName("Deve falhar quando professionalId é nulo")
  void shouldFailWhenProfessionalIdIsNull() {
    var dto = new CreateAppointmentDTO(
        null,
        UUID.randomUUID(),
        UUID.randomUUID(),
        7,
        LocalDate.now(),
        LocalTime.now()
    );

    var violations = validator.validate(dto);

    assertTrue(violations.stream().anyMatch(
        v -> v.getPropertyPath().toString().equals("professionalId")
    ));
  }

  @Test
  @DisplayName("Deve falhar quando serviceId é nulo")
  void shouldFailWhenServiceIdIsNull() {
    var dto = new CreateAppointmentDTO(
        UUID.randomUUID(),
        null,
        UUID.randomUUID(),
        7,
        LocalDate.now(),
        LocalTime.now()
    );

    var violations = validator.validate(dto);

    assertTrue(violations.stream().anyMatch(
        v -> v.getPropertyPath().toString().equals("serviceId")
    ));
  }

  @Test
  @DisplayName("Deve falhar quando patientId é nulo")
  void shouldFailWhenPatientIdIsNull() {
    var dto = new CreateAppointmentDTO(
        UUID.randomUUID(),
        UUID.randomUUID(),
        null,
        7,
        LocalDate.now(),
        LocalTime.now()
    );

    var violations = validator.validate(dto);

    assertTrue(violations.stream().anyMatch(
        v -> v.getPropertyPath().toString().equals("patientId")
    ));
  }

  @Test
  @DisplayName("Deve falhar quando frequencyDays é nulo")
  void shouldFailWhenFrequencyDaysIsNull() {
    var dto = new CreateAppointmentDTO(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        null,
        LocalDate.now(),
        LocalTime.now()
    );

    var violations = validator.validate(dto);

    assertTrue(violations.stream().anyMatch(
        v -> v.getPropertyPath().toString().equals("frequencyDays")
    ));
  }

  @Test
  @DisplayName("Deve falhar quando frequencyDays não é positivo")
  void shouldFailWhenFrequencyDaysIsNotPositive() {
    var dto = new CreateAppointmentDTO(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        0,
        LocalDate.now(),
        LocalTime.now()
    );

    var violations = validator.validate(dto);

    assertTrue(violations.stream().anyMatch(
        v -> v.getPropertyPath().toString().equals("frequencyDays")
    ));
  }

  @Test
  @DisplayName("Deve falhar quando initialDate é nula")
  void shouldFailWhenInitialDateIsNull() {
    var dto = new CreateAppointmentDTO(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        7,
        null,
        LocalTime.now()
    );

    var violations = validator.validate(dto);

    assertTrue(violations.stream().anyMatch(
        v -> v.getPropertyPath().toString().equals("initialDate")
    ));
  }

  @Test
  @DisplayName("Deve falhar quando initialDate está no passado")
  void shouldFailWhenInitialDateIsInThePast() {
    var dto = new CreateAppointmentDTO(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        7,
        LocalDate.now().minusDays(1),
        LocalTime.now()
    );

    var violations = validator.validate(dto);

    assertTrue(violations.stream().anyMatch(
        v -> v.getMessage().contains("no passado")
    ));
  }

  // --- HOUR ---
  @Test
  @DisplayName("Deve falhar quando hour é nula")
  void shouldFailWhenHourIsNull() {
    var dto = new CreateAppointmentDTO(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        7,
        LocalDate.now(),
        null
    );

    var violations = validator.validate(dto);

    assertTrue(violations.stream().anyMatch(
        v -> v.getPropertyPath().toString().equals("hour")
    ));
  }
}
