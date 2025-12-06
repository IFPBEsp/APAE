package br.org.apae.api.appointment.domain.model;

import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GeneratedAppointmentTest {

  private GeneratedAppointment createDefault(Appointment appointment) {
    return new GeneratedAppointment(
        appointment,
        LocalDateTime.now()
    );
  }

  @Nested
  @DisplayName("Testes do construtor")
  class ConstructorTests {

    @Test
    @DisplayName("Deve criar GeneratedAppointment com valores fornecidos e flags padrão")
    void shouldCreateWithDefaultFlags() {
      var appointment = Mockito.mock(Appointment.class);
      var date = LocalDateTime.now();

      var generated = new GeneratedAppointment(appointment, date);

      assertEquals(appointment, generated.getAppointment());
      assertEquals(date, generated.getScheduledDateTime());
      assertFalse(generated.getPerformed());
      assertFalse(generated.getCancelled());
    }
  }


  @Nested
  @DisplayName("Testes de getters e setters")
  class GetterSetterTests {

    @Test
    @DisplayName("Deve alterar data agendada")
    void shouldSetScheduledDateTime() {
      var generated = createDefault(Mockito.mock(Appointment.class));
      var newDate = LocalDateTime.now().plusDays(2);

      generated.setScheduledDateTime(newDate);

      assertEquals(newDate, generated.getScheduledDateTime());
    }

    @Test
    @DisplayName("Deve alterar data sobrescrita")
    void shouldSetOverriddenDateTime() {
      var generated = createDefault(Mockito.mock(Appointment.class));
      var overridden = LocalDateTime.now().plusDays(5);

      generated.setOverriddenDateTime(overridden);

      assertEquals(overridden, generated.getOverriddenDateTime());
    }

    @Test
    @DisplayName("Deve alterar flags de realização e cancelamento")
    void shouldSetPerformedAndCancelled() {
      var generated = createDefault(Mockito.mock(Appointment.class));

      generated.setPerformed(true);
      generated.setCancelled(true);

      assertTrue(generated.getPerformed());
      assertTrue(generated.getCancelled());
    }

    @Test
    @DisplayName("Deve alterar motivo de cancelamento")
    void shouldSetCancellationReason() {
      var generated = createDefault(Mockito.mock(Appointment.class));
      var reason = "Paciente faltou";

      generated.setCancellationReason(reason);

      assertEquals(reason, generated.getCancellationReason());
    }

    @Test
    @DisplayName("Deve definir manualmente patientId apenas via método protegido (testado via reflexão)")
    void shouldSetPatientIdProtected() throws Exception {
      var generated = createDefault(Mockito.mock(Appointment.class));
      var id = UUID.randomUUID();

      var method = GeneratedAppointment.class.getDeclaredMethod("setPatientId", UUID.class);
      method.setAccessible(true);
      method.invoke(generated, id);

      assertEquals(id, generated.getPatientId());
    }
  }

  @Nested
  @DisplayName("Testes da lógica de data efetiva")
  class EffectiveDateTests {

    @Test
    @DisplayName("Deve retornar scheduledDateTime quando overriddenDateTime é nulo")
    void shouldReturnScheduledWhenNoOverride() {
      var generated = createDefault(Mockito.mock(Appointment.class));
      var scheduled = LocalDateTime.now();

      generated.setScheduledDateTime(scheduled);
      generated.setOverriddenDateTime(null);

      assertEquals(scheduled, generated.getEffectiveDateTime());
    }

    @Test
    @DisplayName("Deve retornar overriddenDateTime quando disponível")
    void shouldReturnOverriddenWhenAvailable() {
      var generated = createDefault(Mockito.mock(Appointment.class));
      var overridden = LocalDateTime.now().plusDays(1);

      generated.setOverriddenDateTime(overridden);

      assertEquals(overridden, generated.getEffectiveDateTime());
    }
  }

  @Nested
  @DisplayName("Testes de sincronização do patientId")
  class SyncPatientIdTests {

    @Test
    @DisplayName("Deve sincronizar patientId durante @PrePersist")
    void shouldSyncPatientIdOnPrePersist() throws Exception {
      // Mock da estrutura: GeneratedAppointment -> Appointment -> AnnualRegistry -> Patient

      var patient = Mockito.mock(Patient.class);
      var patientId = UUID.randomUUID();
      Mockito.when(patient.getId()).thenReturn(patientId);

      var annualReg = Mockito.mock(AnnualRegistry.class);
      Mockito.when(annualReg.getPatient()).thenReturn(patient);

      var appointment = Mockito.mock(Appointment.class);
      Mockito.when(appointment.getAnnualRegistration()).thenReturn(annualReg);

      var generated = new GeneratedAppointment(appointment, LocalDateTime.now());

      // Invoca @PrePersist via reflexão
      var method = GeneratedAppointment.class.getDeclaredMethod("syncPatientId");
      method.setAccessible(true);
      method.invoke(generated);

      assertEquals(patientId, generated.getPatientId());
    }

    @Test
    @DisplayName("Não deve lançar erro quando não houver appointment ou patient")
    void shouldNotFailWhenNulls() throws Exception {
      var generated = new GeneratedAppointment(null, LocalDateTime.now());

      var method = GeneratedAppointment.class.getDeclaredMethod("syncPatientId");
      method.setAccessible(true);

      assertDoesNotThrow(() -> method.invoke(generated));
    }
  }
}
