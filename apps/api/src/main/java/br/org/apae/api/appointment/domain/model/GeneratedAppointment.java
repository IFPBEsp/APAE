package br.org.apae.api.appointment.domain.model;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "agendamento_gerado",
        indexes = {
                @Index(name = "idx_gerado_agendamento", columnList = "agendamento_id"),
                @Index(name = "idx_gerado_data", columnList = "data_hora_agendada"),
                @Index(name = "idx_gerado_paciente", columnList = "paciente_id, data_hora_agendada")
        })
public class GeneratedAppointment {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Appointment appointment;

    @Column(name = "data_hora_agendada", nullable = false)
    private LocalDateTime scheduledDateTime;

    @Column(name = "data_hora_sobrescrita")
    private LocalDateTime overriddenDateTime;

    @Column(name = "realizada", nullable = false)
    private Boolean performed = false;

    @Column(name = "cancelada", nullable = false)
    private Boolean cancelled = false;

    @Column(name = "motivo_cancelamento", length = 500)
    private String cancellationReason;

    @Column(name = "paciente_id", nullable = false, updatable = false)
    private UUID patientId;

    @PrePersist
    @PreUpdate
    private void syncPatientId() {
        if (appointment != null
                && appointment.getAnnualRegistration() != null
                && appointment.getAnnualRegistration().getPatient() != null) {
            this.patientId = appointment.getAnnualRegistration().getPatient().getId();
        }
    }

    public LocalDateTime getEffectiveDateTime() {
        return overriddenDateTime != null ? overriddenDateTime : scheduledDateTime;
    }

    // Construtores
    public GeneratedAppointment() {}

    public GeneratedAppointment(Appointment appointment, LocalDateTime scheduledDateTime) {
        this.appointment = appointment;
        this.scheduledDateTime = scheduledDateTime;
        this.performed = false;
        this.cancelled = false;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public LocalDateTime getOverriddenDateTime() {
        return overriddenDateTime;
    }

    public void setOverriddenDateTime(LocalDateTime overriddenDateTime) {
        this.overriddenDateTime = overriddenDateTime;
    }

    public Boolean getPerformed() {
        return performed;
    }

    public void setPerformed(Boolean performed) {
        this.performed = performed;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public UUID getPatientId() {
        return patientId;
    }

    // setPatientId é protegido para evitar uso indevido fora do sync
    void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }
}