package br.org.apae.api.paciente.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Paciente")
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

    @Column
    private String fls;

    @Column
    private String book;

    @Column
    private String rg;

    @Column(name = "data_de_emissao")
    private LocalDate issueDate;

    @Column(name = "orgao_emissor")
    private String issuingAgency;

    @Column
    private String cpf;

    @Column
    private String cns;

    @Column
    private String nis;

    @Column(name = "data_de_cadastro", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "alergias")
    private String allergies;

    @Column(name = "is_aluno", nullable = false)
    private boolean isStudent;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "responsavel_id", referencedColumnName = "id")
    private Guardian guardian;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parent> parents = new ArrayList<>();

    public Patient() {
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBirthCertificateNumber() {
        return birthCertificateNumber;
    }

    public void setBirthCertificateNumber(String birthCertificateNumber) {
        this.birthCertificateNumber = birthCertificateNumber;
    }

    public String getRegistryOffice() {
        return registryOffice;
    }

    public void setRegistryOffice(String registryOffice) {
        this.registryOffice = registryOffice;
    }

    public String getFls() {
        return fls;
    }

    public void setFls(String fls) {
        this.fls = fls;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssuingAgency() {
        return issuingAgency;
    }

    public void setIssuingAgency(String issuingAgency) {
        this.issuingAgency = issuingAgency;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCns() {
        return cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Guardian getGuardian() {
        return guardian;
    }

    public void setGuardian(Guardian guardian) {
        this.guardian = guardian;
    }

    public List<Parent> getParents() {
        return parents;
    }

    public void setParents(List<Parent> parents) {
        this.parents = parents;
    }
}

