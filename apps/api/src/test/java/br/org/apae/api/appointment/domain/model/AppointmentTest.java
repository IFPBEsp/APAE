package br.org.apae.api.appointment.domain.model;

import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

  private Appointment createDefaultAppointment() {
    return new Appointment(
        Mockito.mock(HealthProfessional.class),
        UUID.randomUUID(),
        Mockito.mock(AnnualRegistry.class),
        7,
        LocalTime.of(10, 0),
        LocalDate.now(),
        LocalDate.now().plusDays(30)
    );
  }

  @Nested
  @DisplayName("Testes do construtor")
  class ConstructorTests {

    @Test
    @DisplayName("Deve criar Appointment com valores fornecidos")
    void shouldCreateAppointmentWithProvidedValues() {
      var professional = Mockito.mock(HealthProfessional.class);
      var serviceId = UUID.randomUUID();
      var annualReg = Mockito.mock(AnnualRegistry.class);
      var freq = 7;
      var hour = LocalTime.of(10, 0);
      var initialDate = LocalDate.now();
      var endDate = LocalDate.now().plusDays(10);

      var appointment = new Appointment(
          professional,
          serviceId,
          annualReg,
          freq,
          hour,
          initialDate,
          endDate
      );

      assertEquals(professional, appointment.getProfessional());
      assertEquals(serviceId, appointment.getServiceId());
      assertEquals(annualReg, appointment.getAnnualRegistration());
      assertEquals(freq, appointment.getFrequencyDays());
      assertEquals(hour, appointment.getHour());
      assertEquals(initialDate, appointment.getInitialDate());
      assertEquals(endDate, appointment.getEndDate());
    }

    @Test
    @DisplayName("Novo Appointment deve iniciar com isActive = true")
    void shouldStartAsActive() {
      var appointment = createDefaultAppointment();

      assertTrue(appointment.isActive());
    }
  }

  @Nested
  @DisplayName("Testes de getters e setters")
  class GetterSetterTests {

    @Test
    @DisplayName("Deve alterar o profissional corretamente")
    void shouldUpdateProfessional() {
      var appointment = createDefaultAppointment();
      var newProfessional = Mockito.mock(HealthProfessional.class);

      appointment.setProfessional(newProfessional);

      assertEquals(newProfessional, appointment.getProfessional());
    }

    @Test
    @DisplayName("Deve alterar a frequência corretamente")
    void shouldUpdateFrequencyDays() {
      var appointment = createDefaultAppointment();

      appointment.setFrequencyDays(14);

      assertEquals(14, appointment.getFrequencyDays());
    }

    @Test
    @DisplayName("Deve alterar a data final corretamente")
    void shouldUpdateEndDate() {
      var appointment = createDefaultAppointment();
      var newEndDate = LocalDate.now().plusDays(100);

      appointment.setEndDate(newEndDate);

      assertEquals(newEndDate, appointment.getEndDate());
    }

    @Test
    @DisplayName("Deve alterar o estado ativo corretamente")
    void shouldUpdateIsActive() {
      var appointment = createDefaultAppointment();

      appointment.setActive(false);

      assertFalse(appointment.isActive());
    }
  }

  @Nested
  @DisplayName("Testes do relacionamento generatedAppointments")
  class GeneratedAppointmentsTests {

    @Test
    @DisplayName("Deve iniciar com conjunto de generatedAppointments vazio")
    void shouldStartWithEmptyGeneratedAppointments() {
      var appointment = createDefaultAppointment();

      assertNotNull(appointment.getGeneratedAppointments());
      assertTrue(appointment.getGeneratedAppointments().isEmpty());
    }

    @Test
    @DisplayName("Deve permitir substituir o conjunto de generatedAppointments")
    void shouldAllowReplacingGeneratedAppointments() {
      var appointment = createDefaultAppointment();

      var set = new java.util.HashSet<GeneratedAppointment>();
      appointment.setGeneratedAppointments(set);

      assertSame(set, appointment.getGeneratedAppointments());
    }
  }
}
