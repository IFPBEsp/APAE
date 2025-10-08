package br.org.apae.api.patient.domain.model;

import br.org.apae.api.common.dto.paciente.dto.create.CreatePatientDTO;
import br.org.apae.api.common.dto.paciente.dto.update.UpdatePatientDTO;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.address.UpdateAddressDTO;
import br.org.apae.api.common.model.Address;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id", referencedColumnName = "id")
    private Guardian guardian;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parent> parents = new ArrayList<>();

    protected Patient() {
    }

    private Patient(Builder builder) {
        this.fullName = builder.fullName;
        this.birthplace = builder.birthplace;
        this.birthDate = builder.birthDate;
        this.contact = builder.contact;
        this.birthCertificateNumber = builder.birthCertificateNumber;
        this.registryOffice = builder.registryOffice;
        this.fls = builder.fls;
        this.book = builder.book;
        this.rg = builder.rg;
        this.issueDate = builder.issueDate;
        this.issuingAgency = builder.issuingAgency;
        this.cpf = builder.cpf;
        this.cns = builder.cns;
        this.nis = builder.nis;
        this.registrationDate = builder.registrationDate;
        this.allergies = builder.allergies;
        this.isStudent = builder.isStudent;
        this.address = builder.address;
        this.guardian = builder.guardian;
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
    public List<Parent> getParents() { return parents; }

    private void addParent(Parent parent) {
        this.parents.add(parent);
        parent.setPatient(this);
    }

    public static Patient from(CreatePatientDTO dto) {
        Address address = null;
        if (dto.address() != null) {
            CreateAddressDTO addressDto = dto.address();
            address = new Address(
                    addressDto.city(),
                    addressDto.cep(),
                    addressDto.state(),
                    addressDto.neighborhood(),
                    addressDto.street(),
                    addressDto.number(),
                    addressDto.complement()
            );
        }

        Guardian guardian = Guardian.from(dto.guardian());

        Patient patient = Patient.builder()
                .fullName(dto.fullName())
                .birthplace(dto.nationality())
                .birthDate(dto.birthDate())
                .contact(dto.contact())
                .birthCertificateNumber(dto.birthCertificateNumber())
                .registryOffice(dto.registryOffice())
                .fls(dto.fls())
                .book(dto.book())
                .rg(dto.rg())
                .issueDate(dto.issueDate())
                .issuingAgency(dto.issuingAgency())
                .cpf(dto.cpf())
                .cns(dto.cns())
                .nis(dto.nis())
                .registrationDate(dto.registrationDate())
                .allergies(dto.allergies())
                .isStudent(dto.isStudent())
                .address(address)
                .guardian(guardian)
                .build();

        if (dto.parents() != null) {
            dto.parents().forEach(parentDTO -> {
                Parent parent = Parent.from(parentDTO, patient);
                patient.addParent(parent);
            });
        }

        return patient;
    }

    public void updateWith(UpdatePatientDTO dto) {
        Optional.ofNullable(dto.fullName()).ifPresent(v -> this.fullName = v);
        Optional.ofNullable(dto.nationality()).ifPresent(v -> this.birthplace = v);
        Optional.ofNullable(dto.birthDate()).ifPresent(v -> this.birthDate = v);
        Optional.ofNullable(dto.contact()).ifPresent(v -> this.contact = v);

        this.birthCertificateNumber = dto.birthCertificateNumber();
        this.registryOffice = dto.registryOffice();
        this.fls = dto.fls();
        this.book = dto.book();
        this.rg = dto.rg();
        this.issueDate = dto.issueDate();
        this.issuingAgency = dto.issuingAgency();
        this.cpf = dto.cpf();
        this.cns = dto.cns();
        this.nis = dto.nis();
        this.registrationDate = dto.registrationDate();
        this.allergies = dto.allergies();
        this.isStudent = dto.isStudent();


        if (this.address != null && dto.address() != null) {
            UpdateAddressDTO addressDto = dto.address();

            Optional.ofNullable(addressDto.city()).ifPresent(this.address::setCity);
            Optional.ofNullable(addressDto.cep()).ifPresent(this.address::setCep);
            Optional.ofNullable(addressDto.state()).ifPresent(this.address::setState);
            Optional.ofNullable(addressDto.neighborhood()).ifPresent(this.address::setNeighborhood);
            Optional.ofNullable(addressDto.street()).ifPresent(this.address::setStreet);
            Optional.ofNullable(addressDto.number()).ifPresent(this.address::setNumber);
            Optional.ofNullable(addressDto.complement()).ifPresent(this.address::setComplement);
        }

        if (this.guardian != null && dto.guardian() != null) {
            this.guardian.updateWith(dto.guardian());
        }

        this.parents.clear();
        if (dto.parents() != null) {
            dto.parents().forEach(parentDTO -> {
                Parent parent = Parent.builder()
                        .name(parentDTO.name())
                        .rg(parentDTO.rg())
                        .cpf(parentDTO.cpf())
                        .isAlive(parentDTO.isAlive())
                        .profession(parentDTO.profession())
                        .kinship(parentDTO.kinship())
                        .patient(this)
                        .build();
                this.addParent(parent);
            });
        }
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String fullName;
        private String birthplace;
        private LocalDate birthDate;
        private String contact;
        private String birthCertificateNumber;
        private String registryOffice;
        private String fls;
        private String book;
        private String rg;
        private LocalDate issueDate;
        private String issuingAgency;
        private String cpf;
        private String cns;
        private String nis;
        private LocalDate registrationDate;
        private String allergies;
        private boolean isStudent;
        private Address address;
        private Guardian guardian;
        private List<Parent> parents = new ArrayList<>();

        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder birthplace(String birthplace) { this.birthplace = birthplace; return this; }
        public Builder birthDate(LocalDate birthDate) { this.birthDate = birthDate; return this; }
        public Builder contact(String contact) { this.contact = contact; return this; }
        public Builder birthCertificateNumber(String birthCertificateNumber) { this.birthCertificateNumber = birthCertificateNumber; return this; }
        public Builder registryOffice(String registryOffice) { this.registryOffice = registryOffice; return this; }
        public Builder fls(String fls) { this.fls = fls; return this; }
        public Builder book(String book) { this.book = book; return this; }
        public Builder rg(String rg) { this.rg = rg; return this; }
        public Builder issueDate(LocalDate issueDate) { this.issueDate = issueDate; return this; }
        public Builder issuingAgency(String issuingAgency) { this.issuingAgency = issuingAgency; return this; }
        public Builder cpf(String cpf) { this.cpf = cpf; return this; }
        public Builder cns(String cns) { this.cns = cns; return this; }
        public Builder nis(String nis) { this.nis = nis; return this; }
        public Builder registrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; return this; }
        public Builder allergies(String allergies) { this.allergies = allergies; return this; }
        public Builder isStudent(boolean isStudent) { this.isStudent = isStudent; return this; }
        public Builder address(Address address) { this.address = address; return this; }
        public Builder guardian(Guardian guardian) { this.guardian = guardian; return this; }
        public Builder parents(List<Parent> parents) { this.parents = parents; return this; }

        public Patient build() {
            Objects.requireNonNull(fullName, "O nome completo não pode ser nulo.");
            Objects.requireNonNull(birthDate, "A data de nascimento não pode ser nula.");
            Objects.requireNonNull(registrationDate, "A data de cadastro não pode ser nula.");
            Objects.requireNonNull(address, "O endereço não pode ser nulo.");
            Objects.requireNonNull(guardian, "O responsável não pode ser nulo.");

            return new Patient(this);
        }
    }
}