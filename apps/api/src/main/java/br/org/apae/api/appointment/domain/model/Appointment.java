package br.org.apae.api.appointment.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "agendamento")
public class Appointment {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "paciente_id", nullable = false)
  private UUID patientId;

  @Column(name = "profissional_id", nullable = false)
  private UUID professionalId;

  @Column(name = "frequencia_dias", nullable = false)
  private Integer frequencyDays;

  @Column(name = "proxima_consulta", nullable = false)
  private LocalDate nextAppointment;

  @Column(name = "hora_proxima_consulta", nullable = false)
  private LocalTime nextAppointmentTime;

  @Column(name = "confirmado", nullable = false)
  private Boolean confirmed;

  @Column(name = "descricao")
  private String description;

  @Column(name = "justificativa")
  private String justification;

  @CreationTimestamp
  @Column(name = "data_criacao")
  private LocalDateTime creationDate;

  public Appointment() {
  }

  public Appointment(UUID patientId, UUID professionalId, Integer frequencyDays, LocalDate nextAppointment,
      LocalTime nextAppointmentTime, Boolean confirmed, String description, String justification,
      LocalDateTime creationDate) {
    this.patientId = patientId;
    this.professionalId = professionalId;
    this.frequencyDays = frequencyDays;
    this.nextAppointment = nextAppointment;
    this.nextAppointmentTime = nextAppointmentTime;
    this.confirmed = confirmed;
    this.description = description;
    this.justification = justification;
    this.creationDate = creationDate;
  }

  public Appointment(UUID id, UUID patientId, UUID professionalId, Integer frequencyDays, LocalDate nextAppointment,
      LocalTime nextAppointmentTime, Boolean confirmed, String description, String justification,
      LocalDateTime creationDate) {
    this.id = id;
    this.patientId = patientId;
    this.professionalId = professionalId;
    this.frequencyDays = frequencyDays;
    this.nextAppointment = nextAppointment;
    this.nextAppointmentTime = nextAppointmentTime;
    this.confirmed = confirmed;
    this.description = description;
    this.justification = justification;
    this.creationDate = creationDate;
  }

  public UUID getId() {
    return id;
  }

  public UUID getPatientId() {
    return patientId;
  }

  public UUID getProfessionalId() {
    return professionalId;
  }

  public Integer getFrequencyDays() {
    return frequencyDays;
  }

  public LocalDate getNextAppointment() {
    return nextAppointment;
  }

  public LocalTime getNextAppointmentTime() {
    return nextAppointmentTime;
  }

  public Boolean getConfirmed() {
    return confirmed;
  }

  public String getDescription() {
    return description;
  }

  public String getJustification() {
    return justification;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }
}
