package br.org.apae.api.patient.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Year;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cadastro_anual")
public class AnnualRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bpc")
    private String bpc; // Benefício de Prestação Continuada

    @Column(name = "doencas", columnDefinition = "TEXT")
    private String diseases;

    @Column(name = "renda_familiar")
    private BigDecimal familyIncome;

    @Column(name = "ano", nullable = false)
    private Year year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Patient patient;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cadastro_anual_transtorno",
            joinColumns = @JoinColumn(name = "cadastro_anual_id"),
            inverseJoinColumns = @JoinColumn(name = "transtorno_id")
    )
    private Set<Disorder> disorders = new HashSet<>();

    protected AnnualRegistry() {}

    public AnnualRegistry(String bpc, String diseases, BigDecimal familyIncome, Year year, Patient patient) {
        if (year == null) {
            throw new IllegalArgumentException("O ano do cadastro não pode ser nulo.");
        }
        if (patient == null) {
            throw new IllegalArgumentException("O paciente associado ao cadastro não pode ser nulo.");
        }
        this.bpc = bpc;
        this.diseases = diseases;
        this.familyIncome = familyIncome;
        this.year = year;
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public String getBpc() {
        return bpc;
    }

    public String getDiseases() {
        return diseases;
    }

    public BigDecimal getFamilyIncome() {
        return familyIncome;
    }

    public Year getYear() {
        return year;
    }

    public Patient getPatient() {
        return patient;
    }

    public Set<Disorder> getDisorders() {
        return disorders;
    }

    public void mapForUpdate(String bpc, String diseases, BigDecimal familyIncome, Year year) {
        this.bpc = bpc;
        this.diseases = diseases;
        this.familyIncome = familyIncome;
        if (year != null) {
            this.year = year;
        }
    }

    public void addDisorder(Disorder disorder) {
        this.disorders.add(disorder);
    }

    public void clearDisorders() {
        this.disorders.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnualRegistry that = (AnnualRegistry) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
