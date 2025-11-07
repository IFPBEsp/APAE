package br.org.apae.api.patient.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.*;

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
    private Integer year;

    @Column(name = "paciente_id", nullable = false)
    private UUID patientId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cadastro_anual_transtorno", joinColumns = @JoinColumn(name = "cadastro_anual_id"), inverseJoinColumns = @JoinColumn(name = "transtorno_id"))
    private Set<Disorder> disorders = new HashSet<>();

    protected AnnualRegistry() {
    }

    public AnnualRegistry(String bpc, String diseases, BigDecimal familyIncome, int year, UUID patientId,
                          Set<Disorder> disorders) {
        this.bpc = bpc;
        this.diseases = diseases;
        this.familyIncome = familyIncome;
        this.year = year;
        this.patientId = patientId;
        this.disorders = disorders;
    }

    public AnnualRegistry(UUID id, String bpc, String diseases, BigDecimal familyIncome, int year, UUID patientId,
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

    public int getYear() {
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

    // ==========================================================
    // SETTERS ADICIONADOS PARA O MÉTODO UPDATE FUNCIONAR
    // ==========================================================
    public void setBpc(String bpc) {
        this.bpc = bpc;
    }
    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }
    public void setFamilyIncome(BigDecimal familyIncome) {
        this.familyIncome = familyIncome;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public void setDisorders(Set<Disorder> disorders) {
        this.disorders = disorders;
    }
}