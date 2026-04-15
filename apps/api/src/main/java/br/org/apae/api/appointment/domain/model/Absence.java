package br.org.apae.api.appointment.domain.model;

import jakarta.persistence.*;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.checkerframework.common.aliasing.qual.Unique;
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
    @JoinColumn(
        name = "agendamento_gerado_id", 
        nullable = false,
        foreignKey = @ForeignKey(
            name = "FK_ABSENCE_APPOINTMENT",
            foreignKeyDefinition = "FOREIGN KEY (agendamento_gerado_id) REFERENCES agendamento_gerado(id) ON UPDATE CASCADE"
    ))
    private GeneratedAppointment generatedAppointment;

    @Column(name = "data_falta", nullable = false)
    private LocalDate absenceDate;

    @Column(name = "justificativa", length = 500)
    private String justification;

    @Column(name = "notificado", nullable = false)
    private Boolean notified = false;

    @Column(name = "is_justificada", nullable = false)
    private Boolean isJustified;

    @Column(name= "documento_justificativa_id", unique = true)
    private String justificationDocumentId;

    public Absence() {}

    public Absence(GeneratedAppointment generatedAppointment, LocalDate absenceDate, String justification) {
        this.generatedAppointment = generatedAppointment;
        this.absenceDate = absenceDate;
        this.justification = justification;
        this.notified = false;
        this.justificationDocumentId = null;
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

    public Boolean getIsJustified() { return isJustified; }
    public void setIsJustified(Boolean isJustified) { this.isJustified = isJustified; }

    public String getJustificationDocumentId() { return justificationDocumentId; }
    public void setJustificationDocumentId(String justificationDocumentId) { this.justificationDocumentId = justificationDocumentId; }
}