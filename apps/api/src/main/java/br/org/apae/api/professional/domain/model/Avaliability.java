package br.org.apae.api.professional.domain.model;

import br.org.apae.api.professional.domain.model.Enum.Day;
import br.org.apae.api.professional.domain.model.Enum.Shift;
import jakarta.persistence.*;

import java.nio.channels.FileChannel;
import java.util.UUID;

@Entity
@Table(name = "disponibilidades")
public class Avaliability {

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

    protected Avaliability() {}

    public Avaliability(Day day, Shift shift) {
        this.day = day;
        this.shift = shift;
    }

    public UUID getId() { return id; }
    public Day getDay() { return day; }
    public Shift getShift() { return shift; }
    public HealthProfessional getProfessional() { return professional; }
    public void setProfessional(HealthProfessional professional) { this.professional = professional; }


    public FileChannel stream() {
        return null;
    }
}
