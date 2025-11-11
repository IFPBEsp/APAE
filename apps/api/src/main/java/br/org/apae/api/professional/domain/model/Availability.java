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
    private Day day;

    @Enumerated(EnumType.STRING)
    private Shift shift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id")
    private HealthProfessional professional;

    protected Availability() {}

    public Availability(Shift shift, Day day, HealthProfessional professional) {
        this.shift = shift;
        this.day = day;
        this.professional = professional;
    }

    public UUID getId() { return id; }
    public Day getDay() { return day; }
    public Shift getShift() { return shift; }
    public HealthProfessional getProfessional() { return professional; }
    public void setProfessional(HealthProfessional professional) { this.professional = professional; }

}
