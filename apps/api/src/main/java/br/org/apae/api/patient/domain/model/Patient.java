package br.org.apae.api.patient.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

import br.org.apae.api.address.domain.model.Address;

@Entity
@Table(name = "pacientes")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome_completo", nullable = false)
    private String fullName;

    @Column(name = "naturalidade", nullable = false)
    private String birthplace;

    @Column(name = "data_de_nascimento", nullable = false)
    private LocalDate birthDate;

    @Column(name = "contato", nullable = false)
    private String contact;

    @Column(name = "numero_registro_de_nascimento", nullable = false)
    private String birthCertificateNumber;

    @Column(name = "cartorio", nullable = false)
    private String registryOffice;

    @Column(name = "fls", nullable = false)
    private String fls;

    @Column(name = "livro", nullable = false)
    private String book;

    @Column(name = "rg", nullable = false)
    private String rg;

    @Column(name = "data_de_emissao", nullable = false)
    private LocalDate issueDate;

    @Column(name = "orgao_emissor", nullable = false)
    private String issuingAgency;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "cns", nullable = false)
    private String cns;

    @Column(name = "nis", nullable = false)
    private String nis;

    @Column(name = "data_de_cadastro", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "alergias", nullable = false)
    private String allergies;

    @Column(name = "is_aluno", nullable = false)
    private boolean isStudent;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id", nullable = false)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id", referencedColumnName = "id")
    private Guardian guardian;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parent> parents = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "paciente_vacina", joinColumns = @JoinColumn(name = "paciente_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "vacina_id", referencedColumnName = "id"))
    private Set<Vaccine> vaccines = new HashSet<>();

    protected Patient() {
    }

    public Patient(String fullName, String birthplace, LocalDate birthDate, String contact,
            String birthCertificateNumber, String registryOffice, String fls, String book, String rg,
            LocalDate issueDate, String issuingAgency, String cpf, String cns, String nis, LocalDate registrationDate,
            String allergies, boolean isStudent, Address address, Guardian guardian,
            Set<Vaccine> vaccines) {
        this.fullName = fullName;
        this.birthplace = birthplace;
        this.birthDate = birthDate;
        this.contact = contact;
        this.birthCertificateNumber = birthCertificateNumber;
        this.registryOffice = registryOffice;
        this.fls = fls;
        this.book = book;
        this.rg = rg;
        this.issueDate = issueDate;
        this.issuingAgency = issuingAgency;
        this.cpf = cpf;
        this.cns = cns;
        this.nis = nis;
        this.registrationDate = registrationDate;
        this.allergies = allergies;
        this.isStudent = isStudent;
        this.address = address;
        this.guardian = guardian;
        this.vaccines = vaccines;
    }

    public Patient(UUID id, String fullName, String birthplace, LocalDate birthDate, String contact,
            String birthCertificateNumber, String registryOffice, String fls, String book, String rg,
            LocalDate issueDate, String issuingAgency, String cpf, String cns, String nis,
            LocalDate registrationDate, String allergies, boolean isStudent, Address address, Guardian guardian,
            Set<Vaccine> vaccines) {
        this.id = id;
        this.fullName = fullName;
        this.birthplace = birthplace;
        this.birthDate = birthDate;
        this.contact = contact;
        this.birthCertificateNumber = birthCertificateNumber;
        this.registryOffice = registryOffice;
        this.fls = fls;
        this.book = book;
        this.rg = rg;
        this.issueDate = issueDate;
        this.issuingAgency = issuingAgency;
        this.cpf = cpf;
        this.cns = cns;
        this.nis = nis;
        this.registrationDate = registrationDate;
        this.allergies = allergies;
        this.isStudent = isStudent;
        this.address = address;
        this.guardian = guardian;
        this.vaccines = vaccines;
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getContact() {
        return contact;
    }

    public String getBirthCertificateNumber() {
        return birthCertificateNumber;
    }

    public String getRegistryOffice() {
        return registryOffice;
    }

    public String getFls() {
        return fls;
    }

    public String getBook() {
        return book;
    }

    public String getRg() {
        return rg;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public String getIssuingAgency() {
        return issuingAgency;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCns() {
        return cns;
    }

    public String getNis() {
        return nis;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public String getAllergies() {
        return allergies;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public Address getAddress() {
        return address;
    }

    public Guardian getGuardian() {
        return guardian;
    }

    public List<Parent> getParents() {
        return Collections.unmodifiableList(parents);
    }

    public Set<Vaccine> getVaccines() {
        return Collections.unmodifiableSet(vaccines);
    }

    private void addParents(List<Parent> parents) {
        this.parents.addAll(parents);
    }

    private void clearParents() {
        this.parents.clear();
    }

    public void setParents(List<Parent> parents) {
        // TODO: validação (não pode ser vazia, deve ter ao menos um parente, deve ter
        // no máximo dois parentes)
        clearParents();
        addParents(parents);
    }
}