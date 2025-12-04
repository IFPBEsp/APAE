package br.org.apae.api.professional.domain.model;

import br.org.apae.api.professional.domain.model.Enum.Day;
import br.org.apae.api.professional.domain.model.Enum.Shift;
import jakarta.persistence.*;

import java.util.UUID;

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