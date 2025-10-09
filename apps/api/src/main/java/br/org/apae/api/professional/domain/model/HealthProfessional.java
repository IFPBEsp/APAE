package br.org.apae.api.professional.domain.model;

import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.common.model.Address;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "profissionais_da_saude")
public class HealthProfessional {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "area_da_saude")
    private String healthSector;

    @Column(name = "contato")
    private String phoneNumber;

    @Column(name = "documento_profisisonal")
    private String professionalDocument;

    @Column(name = "rg")
    private String identityDocument;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Address address;

    protected HealthProfessional() {
    }

    private HealthProfessional(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.healthSector = builder.healthSector;
        this.phoneNumber = builder.phoneNumber;
        this.professionalDocument = builder.professionalDocument;
        this.identityDocument = builder.identityDocument;
        this.address = builder.address;
    }

    public static HealthProfessional from(CreateHealthProfessionalDTO dto) {
        return HealthProfessional.builder()
                .name(dto.name())
                .email(dto.email())
                .healthSector(dto.healthSector())
                .phoneNumber(dto.phoneNumber())
                .professionalDocument(dto.professionalDocument())
                .identityDocument(dto.identityDocument())
                .address(Address.from(dto.address()))
                .build();
    }

    public void updateWith(UpdateHealthProfessionalDTO dto) {
        Optional.ofNullable(dto.name()).ifPresent(value -> this.name = value);
        Optional.ofNullable(dto.email()).ifPresent(value -> this.email = value);
        Optional.ofNullable(dto.healthSector()).ifPresent(value -> this.healthSector = value);
        Optional.ofNullable(dto.phoneNumber()).ifPresent(value -> this.phoneNumber = value);
        Optional.ofNullable(dto.professionalDocument()).ifPresent(value -> this.professionalDocument = value);
        Optional.ofNullable(dto.identityDocument()).ifPresent(value -> this.identityDocument = value);

        if (dto.address() != null && this.address != null) {
            this.address.updateWith(dto.address());
        }
    }

    public HealthProfessionalResponseDTO toResponseDTO() {
        return new HealthProfessionalResponseDTO(
                this.id,
                this.name,
                this.email,
                this.healthSector,
                this.phoneNumber,
                this.professionalDocument,
                this.identityDocument,
                this.address);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getHealthSector() {
        return healthSector;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfessionalDocument() {
        return professionalDocument;
    }

    public String getIdentityDocument() {
        return identityDocument;
    }

    public Address getAddress() {
        return address;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String email;
        private String healthSector;
        private String phoneNumber;
        private String professionalDocument;
        private String identityDocument;
        private Address address;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder healthSector(String healthSector) {
            this.healthSector = healthSector;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder professionalDocument(String professionalDocument) {
            this.professionalDocument = professionalDocument;
            return this;
        }

        public Builder identityDocument(String identityDocument) {
            this.identityDocument = identityDocument;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public HealthProfessional build() {
            Objects.requireNonNull(address, "O endereço não pode ser nulo.");

            return new HealthProfessional(this);
        }
    }
}