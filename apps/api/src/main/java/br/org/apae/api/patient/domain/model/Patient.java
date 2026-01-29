package br.org.apae.api.patient.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.patient.domain.model.patient.BirthRecord;
import br.org.apae.api.patient.domain.model.patient.Identification;
import br.org.apae.api.patient.domain.model.patient.PersonalInfo;

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

    @Column(name = "rg", nullable = false, unique = true)
    private String rg;

    @Column(name = "data_de_emissao", nullable = false)
    private LocalDate issueDate;

    @Column(name = "orgao_emissor", nullable = false)
    private String issuingAgency;

    @Column(name = "cpf", nullable = false, unique = true)
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

    @Column(name = "is_apagado", nullable = false)
    private boolean isDeleted = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Address address;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "paciente_vacina", joinColumns = @JoinColumn(name = "paciente_id"), inverseJoinColumns = @JoinColumn(name = "vacina_id"))
    private Set<Vaccine> vaccines = new HashSet<>();

    protected Patient() {
    }

    public Patient(PersonalInfo personalInfo,
            BirthRecord birthRecord,
            Identification identification,
            Address address, Set<Vaccine> vaccines) {

        this.fullName = personalInfo.getFullName();
        this.birthplace = personalInfo.getBirthplace();
        this.birthDate = personalInfo.getBirthDate();
        this.contact = personalInfo.getContact();
        this.allergies = personalInfo.getAllergies();
        this.isStudent = personalInfo.isStudent();

        this.birthCertificateNumber = birthRecord.getBirthCertificateNumber();
        this.registryOffice = birthRecord.getRegistryOffice();
        this.fls = birthRecord.getFls();
        this.book = birthRecord.getBook();
        this.registrationDate = birthRecord.getRegistrationDate();

        this.rg = identification.getRg();
        this.cpf = identification.getCpf();
        this.cns = identification.getCns();
        this.nis = identification.getNis();
        this.issueDate = identification.getIssueDate();
        this.issuingAgency = identification.getIssuingAgency();

        this.address = address;
        this.vaccines = vaccines;
    }

    public Patient(UUID id, PersonalInfo personalInfo,
            BirthRecord birthRecord,
            Identification identification,
            Address address, Set<Vaccine> vaccines) {

        this.id = id;
        this.fullName = personalInfo.getFullName();
        this.birthplace = personalInfo.getBirthplace();
        this.birthDate = personalInfo.getBirthDate();
        this.contact = personalInfo.getContact();
        this.allergies = personalInfo.getAllergies();
        this.isStudent = personalInfo.isStudent();

        this.birthCertificateNumber = birthRecord.getBirthCertificateNumber();
        this.registryOffice = birthRecord.getRegistryOffice();
        this.fls = birthRecord.getFls();
        this.book = birthRecord.getBook();
        this.registrationDate = birthRecord.getRegistrationDate();

        this.rg = identification.getRg();
        this.cpf = identification.getCpf();
        this.cns = identification.getCns();
        this.nis = identification.getNis();
        this.issueDate = identification.getIssueDate();
        this.issuingAgency = identification.getIssuingAgency();

        this.address = address;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public Address getAddress() {
        return address;
    }

    public Set<Vaccine> getVaccines() {
        return Collections.unmodifiableSet(vaccines);
    }
}