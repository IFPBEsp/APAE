package br.org.apae.api.patient.domain.model;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cadastros_anuais")
public class AnnualRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "bpc", nullable = false)
    private String bpc; // Benefício de Prestação Continuada

    @Column(name = "doencas", nullable = false)
    private String diseases;

    @Column(name = "renda_familiar", nullable = false)
    private BigDecimal familyIncome;

    @Column(name = "ano", nullable = false)
    private Year year;

    @Column(name = "paciente_id", nullable = false)
    private UUID patientId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cadastro_anual_transtorno", joinColumns = @JoinColumn(name = "cadastro_anual_id"), inverseJoinColumns = @JoinColumn(name = "transtorno_id"))
    private Set<Disorder> disorders = new HashSet<>();

    protected AnnualRegistry() {
    }

    public AnnualRegistry(String bpc, String diseases, BigDecimal familyIncome, Year year, UUID patientId,
            Set<Disorder> disorders) {
        this.bpc = bpc;
        this.diseases = diseases;
        this.familyIncome = familyIncome;
        this.year = year;
        this.patientId = patientId;
        this.disorders = disorders;
    }

    public AnnualRegistry(UUID id, String bpc, String diseases, BigDecimal familyIncome, Year year, UUID patientId,
            Set<Disorder> disorders) {
        this.id = id;
        this.bpc = bpc;
        this.diseases = diseases;
        this.familyIncome = familyIncome;
        this.year = year;
        this.patientId = patientId;
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

    public UUID getPatientId() {
        return patientId;
    }

    public Set<Disorder> getDisorders() {
        return Collections.unmodifiableSet(disorders);
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