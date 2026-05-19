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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "substituido_por_id")
  private Appointment replacedBy; // aponta para o novo agendamento que substituiu este

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "atualizado_de_id")
  private Appointment updatedFrom; // aponta para o agendamento original do qual este foi gerado

  public Appointment() {
  }

  public Appointment(HealthProfessional professional, AnnualRegistry annualRegistration, Integer frequencyDays, LocalTime hour, LocalDate initialDate, LocalDate endDate) {
    this.professional = professional;
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

  public Appointment getReplacedBy() { return replacedBy; }
  public void setReplacedBy(Appointment replacedBy) { this.replacedBy = replacedBy; }

  public Appointment getUpdatedFrom() { return updatedFrom; }
  public void setUpdatedFrom(Appointment updatedFrom) { this.updatedFrom = updatedFrom; }
}
