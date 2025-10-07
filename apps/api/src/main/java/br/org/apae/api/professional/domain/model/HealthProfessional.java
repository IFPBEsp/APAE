package br.org.apae.api.professional.domain.model;

import br.org.apae.api.common.model.Address;
import br.org.apae.api.common.dto.address.AddressDTO;
import br.org.apae.api.professional.dto.HealthProfessionalCreateDTO;
import br.org.apae.api.professional.dto.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.dto.HealthProfessionalUpdateDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "health_professionals")
@Setter
@Getter
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

    public HealthProfessional(String healthSector, String phoneNumber, String professionalDocument, String email, String name, String identityDocument, Address address) {
        this.healthSector = healthSector;
        this.phoneNumber = phoneNumber;
        this.professionalDocument = professionalDocument;
        this.email = email;
        this.name = name;
        this.identityDocument = identityDocument;
        this.address = address;
    }

    // factory
    public static HealthProfessional from(HealthProfessionalCreateDTO dto) {
        Address address = Address.builder()
                .city(dto.address().city())
                .cep(dto.address().cep())
                .state(dto.address().state())
                .neighborhood(dto.address().neighborhood())
                .street(dto.address().street())
                .number(dto.address().number())
                .complement(dto.address().complement())
                .build();

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
}