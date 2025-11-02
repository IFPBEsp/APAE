package br.org.apae.api.professional.domain.model;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

import br.org.apae.api.address.domain.model.Address;

@Entity
@Table(name = "profissionais_da_saude")
public class HealthProfessional {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "area_da_saude", nullable = false)
    private String healthSector;

    @Column(name = "contato", nullable = false)
    private String phoneNumber;

    @Column(name = "documento_profissional", nullable = false, unique = true)
    private String professionalDocument;

    @Column(name = "rg", unique = true)
    private String identityDocument;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Address address;

    protected HealthProfessional() {
    }

    public HealthProfessional(String name, String email, String healthSector, String phoneNumber, String professionalDocument) {
        Objects.requireNonNull(name, "O nome não pode ser nulo.");
        Objects.requireNonNull(email, "O e-mail não pode ser nulo.");
        Objects.requireNonNull(healthSector, "A área da saúde não pode ser nula.");
        Objects.requireNonNull(phoneNumber, "O contato não pode ser nulo.");
        Objects.requireNonNull(professionalDocument, "O documento profissional não pode ser nulo.");

        this.name = name;
        this.email = email;
        this.healthSector = healthSector;
        this.phoneNumber = phoneNumber;
        this.professionalDocument = professionalDocument;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getHealthSector() { return healthSector; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getProfessionalDocument() { return professionalDocument; }
    public String getIdentityDocument() { return identityDocument; }
    public Address getAddress() { return address; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setHealthSector(String healthSector) { this.healthSector = healthSector; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setProfessionalDocument(String professionalDocument) { this.professionalDocument = professionalDocument; }
    public void setIdentityDocument(String identityDocument) { this.identityDocument = identityDocument; }
    public void setAddress(Address address) { this.address = address; }
}