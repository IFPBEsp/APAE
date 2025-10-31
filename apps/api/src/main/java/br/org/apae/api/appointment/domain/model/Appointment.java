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
@Table(name = "agendamentos")
public class Appointment {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(name = "profissional_id", nullable = false)
  private UUID professionalId;

  @Column(name = "atendimento_id", nullable = false)
  private UUID serviceId;

  @Column(name = "cadastro_anual_id", nullable = false)
  private UUID annualRegistrationId;

  @Column(name = "frequencia_dias", nullable = false)
  private Integer frequencyDays;

  @Column(name = "hora", nullable = false)
  private LocalTime hour;

  @Column(name = "data_inicial", nullable = false)
  private LocalDate initialDate;

  @Column(name = "data_final", nullable = false)
  private LocalDate endDate;

  @Column(name = "ativo")
  private boolean isActive;

  @CreationTimestamp
  @Column(name = "data_criacao")
  private LocalDateTime creationDate;

  public Appointment() {
  }


  public Appointment(UUID professionalId, UUID serviceId, UUID annualRegistrationId, Integer frequencyDays, LocalTime hour, LocalDate initialDate, LocalDate endDate) {
    this.professionalId = professionalId;
    this.serviceId = serviceId;
    this.annualRegistrationId = annualRegistrationId;
    this.frequencyDays = frequencyDays;
    this.hour = hour;
    this.initialDate = initialDate;
    this.endDate = endDate;
    this.isActive = true;
  }

  public LocalTime getHour() {
    return hour;
  }

  public void setHour(LocalTime hour) {
    this.hour = hour;
  }

  public UUID getId() {
    return id;
  }

  public UUID getProfessionalId() {
    return professionalId;
  }

  public void setProfessionalId(UUID professionalId) {
    this.professionalId = professionalId;
  }

  public UUID getServiceId() {
    return serviceId;
  }

  public void setServiceId(UUID serviceId) {
    this.serviceId = serviceId;
  }

  public UUID getAnnualRegistrationId() {
    return annualRegistrationId;
  }

  public void setAnnualRegistrationId(UUID annualRegistrationId) {
    this.annualRegistrationId = annualRegistrationId;
  }

  public Integer getFrequencyDays() {
    return frequencyDays;
  }

  public void setFrequencyDays(Integer frequencyDays) {
    this.frequencyDays = frequencyDays;
  }

  public LocalDate getInitialDate() {
    return initialDate;
  }

  public void setInitialDate(LocalDate initialDate) {
    this.initialDate = initialDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }
}
