package br.org.apae.api.appointment.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "historico_consulta")
public class ConsultationHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "agendamento_id", nullable = false)
  private UUID appointmentId;

  @Column(name = "data_consulta", nullable = false)
  private LocalDate consultationDate;

  @Column(name = "hora_consulta", nullable = false)
  private LocalTime consultationTime;

  @Column(name = "foi_realizada", nullable = false)
  private boolean wasCompleted;

  @Column(name = "justificativa")
  private String justification;

  @Column(name = "data_criacao", nullable = false)
  private LocalDateTime creationDate;

  public ConsultationHistory() {
  }

  public ConsultationHistory(UUID appointmentId, LocalDate consultationDate, LocalTime consultationTime,
      boolean wasCompleted, String justification, LocalDateTime creationDate) {
    this.appointmentId = appointmentId;
    this.consultationDate = consultationDate;
    this.consultationTime = consultationTime;
    this.wasCompleted = wasCompleted;
    this.justification = justification;
    this.creationDate = creationDate;
  }

  public ConsultationHistory(UUID id, UUID appointmentId, LocalDate consultationDate, LocalTime consultationTime,
      boolean wasCompleted, String justification, LocalDateTime creationDate) {
    this.id = id;
    this.appointmentId = appointmentId;
    this.consultationDate = consultationDate;
    this.consultationTime = consultationTime;
    this.wasCompleted = wasCompleted;
    this.justification = wasCompleted ? null : justification;
    this.creationDate = creationDate;
  }

  public UUID getId() {
    return id;
  }

  public UUID getAppointmentId() {
    return appointmentId;
  }

  public LocalDate getConsultationDate() {
    return consultationDate;
  }

  public LocalTime getConsultationTime() {
    return consultationTime;
  }

  public boolean isWasCompleted() {
    return wasCompleted;
  }

  public String getJustification() {
    return justification;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }
}
