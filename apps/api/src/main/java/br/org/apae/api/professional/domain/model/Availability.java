package br.org.apae.api.professional.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

import br.org.apae.api.professional.domain.model.enums.Shift;
import br.org.apae.api.professional.domain.model.enums.Day;

@Entity
@Table(name = "disponibilidades")
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Day day;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Shift shift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private HealthProfessional professional;

    protected Availability() {}

    public Availability(Day day, Shift shift, HealthProfessional professional) {
        this.day = day;
        this.shift = shift;
        this.professional = professional;
    }

    public UUID getId() { return id; }
    public Day getDay() { return day; }
    public void setDay(Day day) { this.day = day; }
    public Shift getShift() { return shift; }
    public void setShift(Shift shift) { this.shift = shift; }
    public HealthProfessional getProfessional() { return professional; }
    public void setProfessional(HealthProfessional professional) { this.professional = professional; }
}