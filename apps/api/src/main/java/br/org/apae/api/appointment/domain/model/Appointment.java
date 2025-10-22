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

  @Column(name = "frequencia_dias", nullable = false)
  private Integer frequencyDays;

  @Column(name = "data_inicial", nullable = false)
  private LocalDate initialDate;

  @Column(name = "hora", nullable = false)
  private LocalTime hour;

  @Column(name = "data_final", nullable = false)
  private LocalDate endDate;

  @Column(name = "atendimento_id", nullable = false)
  private UUID serviceId;

  @Column(name = "cadastro_anual_id", nullable = false)
  private UUID annualRegistrationId;

  @CreationTimestamp
  @Column(name = "data_criacao")
  private LocalDateTime creationDate;

  public Appointment() {
  }

  public Appointment(UUID professionalId, Integer frequencyDays, LocalDate initialDate, LocalTime hour, LocalDate endDate, UUID serviceId, UUID annualRegistrationId) {
      this.professionalId = professionalId;
      this.frequencyDays = frequencyDays;
      this.initialDate = initialDate;
      this.hour = hour;
      this.endDate = endDate;
      this.serviceId = serviceId;
      this.annualRegistrationId = annualRegistrationId;
  }

    public Appointment(UUID id, UUID professionalId, Integer frequencyDays, LocalDate initialDate, LocalTime hour, LocalDate endDate, UUID serviceId, UUID annualRegistrationId, LocalDateTime creationDate) {
        this.id = id;
        this.professionalId = professionalId;
        this.frequencyDays = frequencyDays;
        this.initialDate = initialDate;
        this.hour = hour;
        this.endDate = endDate;
        this.serviceId = serviceId;
        this.annualRegistrationId = annualRegistrationId;
        this.creationDate = creationDate;
    }

    public LocalDateTime getCreationDate() {
    return creationDate;
  }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(UUID professionalId) {
        this.professionalId = professionalId;
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

    public LocalTime getHour() {
        return hour;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
