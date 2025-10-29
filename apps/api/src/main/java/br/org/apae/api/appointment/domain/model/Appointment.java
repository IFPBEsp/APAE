package br.org.apae.api.appointment.domain.model;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "agendamento",
        indexes = {
                @Index(name = "idx_agendamento_cadastro", columnList = "cadastro_anual_id"),
                @Index(name = "idx_agendamento_ativo", columnList = "ativo,data_fim_regra")
        })
public class Appointment {

    @Id @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cadastro_anual_id", nullable = false)
    private AnnualRegistration annualRegistration;

    @Column(name = "frequencia_dias", nullable = false)
    private Integer frequencyDays;

    @Column(name = "hora_consulta", nullable = false)
    private LocalTime appointmentTime;

    @Column(name = "ativo", nullable = false)
    private Boolean active = true;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate startDate;

    @Column(name = "data_fim")
    private LocalDate endDate;

    public Appointment() {}

    public Appointment(AnnualRegistration annualRegistration, Integer frequencyDays, LocalTime appointmentTime,
                       LocalDate startDate) {
        this.annualRegistration = annualRegistration;
        this.frequencyDays = frequencyDays;
        this.appointmentTime = appointmentTime;
        this.startDate = startDate;
        this.active = true;
    }

    @PrePersist
    private void prePersist() {
        if (active == null) active = true;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public AnnualRegistration getAnnualRegistration() { return annualRegistration; }
    public void setAnnualRegistration(AnnualRegistration annualRegistration) { this.annualRegistration = annualRegistration; }

    public Integer getFrequencyDays() { return frequencyDays; }
    public void setFrequencyDays(Integer frequencyDays) { this.frequencyDays = frequencyDays; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}