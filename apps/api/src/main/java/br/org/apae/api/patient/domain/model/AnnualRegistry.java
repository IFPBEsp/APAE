package br.org.apae.api.patient.domain.model;

import br.org.apae.api.servicearea.domain.model.ServiceArea;
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
    private String bpc;

    @Column(name = "doencas", nullable = false)
    private String diseases;

    @Column(name = "medicamentos_continuos")
    private String continuousMedication;

    @Column(name = "renda_familiar", nullable = false)
    private BigDecimal familyIncome;

    @Column(name = "ano", nullable = false)
    private Integer year;

    @Column(name = "paciente_id", nullable = false)
    private UUID patientId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cadastro_anual_transtorno", joinColumns = @JoinColumn(name = "cadastro_anual_id"), inverseJoinColumns = @JoinColumn(name = "transtorno_id"))
    private Set<Disorder> disorders = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cadastro_anual_areas_de_atendimento", joinColumns = @JoinColumn(name = "cadastro_anual_id"), inverseJoinColumns = @JoinColumn(name = "areas_de_atendimento_id"))
    private Set<ServiceArea> serviceAreas = new HashSet<>();

    protected AnnualRegistry() {
    }

    public AnnualRegistry(String bpc, String diseases, String continuousMedication, BigDecimal familyIncome, int year, UUID patientId,
                          Set<Disorder> disorders, Set<ServiceArea> serviceAreas) {
        this.bpc = bpc;
        this.diseases = diseases;
        this.continuousMedication = continuousMedication;
        this.familyIncome = familyIncome;
        this.year = year;
        this.patientId = patientId;
        this.disorders = disorders;
        this.serviceAreas = serviceAreas;
    }

    public AnnualRegistry(UUID id, String bpc, String diseases, String continuousMedication, BigDecimal familyIncome, int year, UUID patientId,
                          Set<Disorder> disorders, Set<ServiceArea> serviceAreas) {
        this.id = id;
        this.bpc = bpc;
        this.diseases = diseases;
        this.continuousMedication = continuousMedication;
        this.familyIncome = familyIncome;
        this.year = year;
        this.patientId = patientId;
        this.disorders = disorders;
        this.serviceAreas = serviceAreas;
    }

    public UUID getId() { return id; }
    public String getBpc() { return bpc; }
    public String getDiseases() { return diseases; }
    public String getContinuousMedication() { return continuousMedication; }
    public BigDecimal getFamilyIncome() { return familyIncome; }
    public Integer getYear() { return year; }
    public UUID getPatientId() { return patientId; }

    public Set<ServiceArea> getServiceAreas() {
        return serviceAreas;
    }
    public Set<Disorder> getDisorders() { return Collections.unmodifiableSet(disorders); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnualRegistry that = (AnnualRegistry) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
    public void setBpc(String bpc) { this.bpc = bpc; }
    public void setDiseases(String diseases) { this.diseases = diseases; }
    public void setContinuousMedication(String continuousMedication) { this.continuousMedication = continuousMedication; }
    public void setFamilyIncome(BigDecimal familyIncome) { this.familyIncome = familyIncome; }
    public void setYear(Integer year) { this.year = year; }
    public void setDisorders(Set<Disorder> disorders) { this.disorders = disorders; }
    public void setServiceAreas(Set<ServiceArea> serviceAreas) {
        this.serviceAreas = serviceAreas;
    }
}