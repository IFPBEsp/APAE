package br.org.apae.api.appointment.domain.model;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "falta",
        indexes = @Index(name = "idx_falta_gerado", columnList = "agendamento_gerado_id"))
public class Absence {

    @Id @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_gerado_id", nullable = false)
    private GeneratedAppointment generatedAppointment;

    @Column(name = "data_falta", nullable = false)
    private LocalDate absenceDate;

    @Column(name = "justificativa", length = 500)
    private String justification;

    @Column(name = "notificado", nullable = false)
    private Boolean notified = false;

    public Absence() {}

    public Absence(GeneratedAppointment generatedAppointment, LocalDate absenceDate, String justification) {
        this.generatedAppointment = generatedAppointment;
        this.absenceDate = absenceDate;
        this.justification = justification;
        this.notified = false;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public GeneratedAppointment getGeneratedAppointment() { return generatedAppointment; }
    public void setGeneratedAppointment(GeneratedAppointment generatedAppointment) { this.generatedAppointment = generatedAppointment; }

    public LocalDate getAbsenceDate() { return absenceDate; }
    public void setAbsenceDate(LocalDate absenceDate) { this.absenceDate = absenceDate; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }

    public Boolean getNotified() { return notified; }
    public void setNotified(Boolean notified) { this.notified = notified; }
}