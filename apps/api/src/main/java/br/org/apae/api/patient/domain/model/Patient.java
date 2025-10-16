package br.org.apae.api.patient.domain.model;

import br.org.apae.api.common.model.Address;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "pacientes")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome_completo", nullable = false)
    private String fullName;

    @Column(name = "naturalidade")
    private String birthplace;

    @Column(name = "data_de_nascimento", nullable = false)
    private LocalDate birthDate;

    @Column(name = "contato")
    private String contact;

    @Column(name = "numero_registro_de_nascimento")
    private String birthCertificateNumber;

    @Column(name = "cartorio")
    private String registryOffice;

    @Column(name = "fls")
    private String fls;

    @Column(name = "livro")
    private String book;

    @Column(name = "rg")
    private String rg;

    @Column(name = "data_de_emissao")
    private LocalDate issueDate;

    @Column(name = "orgao_emissor")
    private String issuingAgency;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "cns")
    private String cns;

    @Column(name = "nis")
    private String nis;

    @Column(name = "data_de_cadastro", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "alergias")
    private String allergies;

    @Column(name = "is_aluno", nullable = false)
    private boolean isStudent;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id", referencedColumnName = "id")
    private Guardian guardian;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parent> parents = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "paciente_vacina",
            joinColumns = @JoinColumn(name = "paciente_id"),
            inverseJoinColumns = @JoinColumn(name = "vacina_id")
    )
    private Set<Vaccine> vaccines = new HashSet<>();

    @Deprecated
    protected Patient() {}

    public Patient(String fullName, LocalDate birthDate, LocalDate registrationDate, Address address, Guardian guardian) {
        Objects.requireNonNull(fullName, "O nome completo não pode ser nulo.");
        Objects.requireNonNull(birthDate, "A data de nascimento não pode ser nula.");
        Objects.requireNonNull(registrationDate, "A data de cadastro não pode ser nula.");
        Objects.requireNonNull(address, "O endereço não pode ser nulo.");
        Objects.requireNonNull(guardian, "O responsável não pode ser nulo.");
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.address = address;
        this.guardian = guardian;
    }

    public UUID getId() { return id; }
    public String getFullName() { return fullName; }
    public String getBirthplace() { return birthplace; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getContact() { return contact; }
    public String getBirthCertificateNumber() { return birthCertificateNumber; }
    public String getRegistryOffice() { return registryOffice; }
    public String getFls() { return fls; }
    public String getBook() { return book; }
    public String getRg() { return rg; }
    public LocalDate getIssueDate() { return issueDate; }
    public String getIssuingAgency() { return issuingAgency; }
    public String getCpf() { return cpf; }
    public String getCns() { return cns; }
    public String getNis() { return nis; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public String getAllergies() { return allergies; }
    public boolean isStudent() { return isStudent; }
    public Address getAddress() { return address; }
    public Guardian getGuardian() { return guardian; }
    public List<Parent> getParents() {
        return Collections.unmodifiableList(parents);
    }

    public Set<Vaccine> getVaccines() {
        return Collections.unmodifiableSet(vaccines);
    }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setBirthplace(String birthplace) { this.birthplace = birthplace; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setContact(String contact) { this.contact = contact; }
    public void setBirthCertificateNumber(String birthCertificateNumber) { this.birthCertificateNumber = birthCertificateNumber; }
    public void setRegistryOffice(String registryOffice) { this.registryOffice = registryOffice; }
    public void setFls(String fls) { this.fls = fls; }
    public void setBook(String book) { this.book = book; }
    public void setRg(String rg) { this.rg = rg; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public void setIssuingAgency(String issuingAgency) { this.issuingAgency = issuingAgency; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setCns(String cns) { this.cns = cns; }
    public void setNis(String nis) { this.nis = nis; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public void setStudent(boolean student) { isStudent = student; }
    public void setAddress(Address address) { this.address = address; }
    public void setGuardian(Guardian guardian) { this.guardian = guardian; }

    public void addParent(Parent parent) {
        this.parents.add(parent);
        parent.setPatient(this);
    }
    public void addVaccine(Vaccine vaccine) {
        this.vaccines.add(vaccine);
    }
    public void clearParents() {
        this.parents.clear();
    }
    public void clearVaccines() {
        this.vaccines.clear();
    }
}