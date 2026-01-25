package br.org.apae.api.appointment.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "agendamentos")
public class Appointment {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profissional_id", nullable = false)
  private HealthProfessional professional;

  @Column(name = "atendimento_id", nullable = false)
  private UUID serviceId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cadastro_anual_id", nullable = false)
  private AnnualRegistry annualRegistration;

  @Column(name = "frequencia_dias", nullable = false)
  private Integer frequencyDays;

  @Column(name = "hora", nullable = false)
  private LocalTime hour;

  @Column(name = "data_inicial", nullable = false)
  private LocalDate initialDate;

  @Column(name = "data_final")
  private LocalDate endDate;

  @Column(name = "ativo")
  private boolean isActive;

  @OneToMany(
      mappedBy = "appointment",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private Set<GeneratedAppointment>  generatedAppointments = new HashSet<>();

  @CreationTimestamp
  @Column(name = "data_criacao")
  private LocalDateTime creationDate;

  public Appointment() {
  }

  public Appointment(HealthProfessional professional, UUID serviceId, AnnualRegistry annualRegistration, Integer frequencyDays, LocalTime hour, LocalDate initialDate, LocalDate endDate) {
    this.professional = professional;
    this.serviceId = serviceId;
    this.annualRegistration = annualRegistration;
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

  public HealthProfessional getProfessional() {
    return professional;
  }

  public void setProfessional(HealthProfessional professional) {
    this.professional = professional;
  }

  public UUID getServiceId() {
    return serviceId;
  }

  public void setServiceId(UUID serviceId) {
    this.serviceId = serviceId;
  }

  public AnnualRegistry getAnnualRegistration() {
    return annualRegistration;
  }

  public void setAnnualRegistration(AnnualRegistry annualRegistration) {
    this.annualRegistration = annualRegistration;
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

  public Set<GeneratedAppointment> getGeneratedAppointments() {
    return generatedAppointments;
  }

  public void setGeneratedAppointments(Set<GeneratedAppointment> generatedAppointments) {
    this.generatedAppointments = generatedAppointments;
  }
}
