package br.org.apae.api.patient.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Year;
import java.util.*;

@Entity
@Table(name = "cadastros_anuais")
public class AnnualRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "bpc", nullable = false)
    private String bpc; // Benefício de Prestação Continuada

    @Column(name = "doencas", columnDefinition = "TEXT", nullable = false)
    private String diseases;

    @Column(name = "renda_familiar", nullable = false)
    private BigDecimal familyIncome;

    @Column(name = "ano", nullable = false)
    private Year year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Patient patient;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cadastro_anual_transtorno", joinColumns = @JoinColumn(name = "cadastro_anual_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "transtorno_id", referencedColumnName = "id"))
    private Set<Disorder> disorders = new HashSet<>();

    protected AnnualRegistry() {
    }

    public AnnualRegistry(String bpc, String diseases, BigDecimal familyIncome, Year year, Patient patient,
            Set<Disorder> disorders) {
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
        this.disorders = disorders;
    }

    public AnnualRegistry(UUID id, String bpc, String diseases, BigDecimal familyIncome, Year year, Patient patient,
            Set<Disorder> disorders) {
        this.id = id;
        this.bpc = bpc;
        this.diseases = diseases;
        this.familyIncome = familyIncome;
        this.year = year;
        this.patient = patient;
        this.disorders = disorders;
    }

    public UUID getId() {
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
        return Collections.unmodifiableSet(disorders);
    }

    private void addDisorders(Set<Disorder> disorder) {
        this.disorders.addAll(disorder);
    }

    private void clearDisorders() {
        this.disorders.clear();
    }

    public void setDisorders(Set<Disorder> disorders) {
        // TODO: validar(ter pelo menos um transtorno relacionado)
        this.clearDisorders();
        this.addDisorders(disorders);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AnnualRegistry that = (AnnualRegistry) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}