package br.org.apae.api.appointment.domain.model;

import br.org.apae.api.professional.domain.model.HealthProfessional;
import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cadastro_anual",
        uniqueConstraints = @UniqueConstraint(columnNames = {"paciente_id", "ano"}),
        indexes = {
                @Index(name = "idx_cadastro_anual_paciente_ano", columnList = "paciente_id,ano"),
                @Index(name = "idx_cadastro_anual_datas", columnList = "data_inicio,data_fim")
        })
public class AnnualRegistration {

    @Id @GeneratedValue
    private UUID id;

    @Column(name = "paciente_id", nullable = false)
    private UUID patientId;

    @Column(name = "ano", nullable = false)
    private Integer year;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate startDate;

    @Column(name = "data_fim")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = false)
    private HealthProfessional professional;

    // Constructors
    public AnnualRegistration() {}

    public AnnualRegistration(UUID patientId, Integer year, LocalDate startDate, LocalDate endDate, HealthProfessional professional) {
        this.patientId = patientId;
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
        this.professional = professional;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPatientId() { return patientId; }
    public void setPatientId(UUID patientId) { this.patientId = patientId; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public HealthProfessional getProfessional() { return professional; }
    public void setProfessional(HealthProfessional professional) { this.professional = professional; }
}