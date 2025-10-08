package br.org.apae.api.professional.domain.model;

import br.org.apae.api.common.model.Address;
import br.org.apae.api.professional.dto.HealthProfessionalCreateDTO;
import br.org.apae.api.professional.dto.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.dto.HealthProfessionalUpdateDTO;
import br.org.apae.api.common.dto.address.AddressDTO;
import jakarta.persistence.*;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "health_professionals")
public class HealthProfessional {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String healthSector;
    private String phoneNumber;
    private String professionalDocument;
    private String email;
    private String name;
    private String identityDocument;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    protected HealthProfessional() {}

    public HealthProfessional(String healthSector, String phoneNumber, String professionalDocument, String email, String name, String identityDocument, Address address) {
        this.healthSector = healthSector;
        this.phoneNumber = phoneNumber;
        this.professionalDocument = professionalDocument;
        this.email = email;
        this.name = name;
        this.identityDocument = identityDocument;
        this.address = address;
    }

    public HealthProfessional(UUID id, String healthSector, String phoneNumber, String professionalDocument, String email, String name, String identityDocument, Address address) {
        this.id = id;
        this.healthSector = healthSector;
        this.phoneNumber = phoneNumber;
        this.professionalDocument = professionalDocument;
        this.email = email;
        this.name = name;
        this.identityDocument = identityDocument;
        this.address = address;
    }

    public static HealthProfessional from(HealthProfessionalCreateDTO dto) {
        Address address = new Address(
                dto.address().city(),
                dto.address().cep(),
                dto.address().state(),
                dto.address().neighborhood(),
                dto.address().street(),
                dto.address().number(),
                dto.address().complement()
        );

        return new HealthProfessional(
                dto.healthSector(),
                dto.phoneNumber(),
                dto.professionalDocument(),
                dto.email(),
                dto.name(),
                dto.identityDocument(),
                address
        );
    }

    public void updateWith(HealthProfessionalUpdateDTO dto) {
        Optional.ofNullable(dto.healthSector()).ifPresent(this::setHealthSector);
        Optional.ofNullable(dto.professionalDocument()).ifPresent(this::setProfessionalDocument);
        Optional.ofNullable(dto.name()).ifPresent(this::setName);
        Optional.ofNullable(dto.email()).ifPresent(this::setEmail);
        Optional.ofNullable(dto.phoneNumber()).ifPresent(this::setPhoneNumber);
        Optional.ofNullable(dto.identityDocument()).ifPresent(this::setIdentityDocument);

        if (dto.address() != null && this.address != null) {
            AddressDTO addressDto = dto.address();

            Optional.ofNullable(addressDto.city()).ifPresent(this.address::setCity);
            Optional.ofNullable(addressDto.cep()).ifPresent(this.address::setCep);
            Optional.ofNullable(addressDto.state()).ifPresent(this.address::setState);
            Optional.ofNullable(addressDto.neighborhood()).ifPresent(this.address::setNeighborhood);
            Optional.ofNullable(addressDto.street()).ifPresent(this.address::setStreet);
            Optional.ofNullable(addressDto.number()).ifPresent(this.address::setNumber);

            if (addressDto.complement() != null) {
                this.address.setComplement(addressDto.complement());
            }
        }
    }

    public HealthProfessionalResponseDTO toResponseDTO() {
        return new HealthProfessionalResponseDTO(
                this.id,
                this.healthSector,
                this.phoneNumber,
                this.professionalDocument,
                this.email,
                this.name,
                this.identityDocument,
                this.address
        );
    }

    public UUID getId() {
        return id;
    }

//    public void setId(UUID id) {
//        this.id = id;
//    }

    public String getHealthSector() {
        return healthSector;
    }

    public void setHealthSector(String healthSector) {
        this.healthSector = healthSector;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfessionalDocument() {
        return professionalDocument;
    }

    public void setProfessionalDocument(String professionalDocument) {
        this.professionalDocument = professionalDocument;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentityDocument() {
        return identityDocument;
    }

    public void setIdentityDocument(String identityDocument) {
        this.identityDocument = identityDocument;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}