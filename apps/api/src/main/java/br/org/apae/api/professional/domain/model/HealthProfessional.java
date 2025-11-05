package br.org.apae.api.professional.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.org.apae.api.address.domain.model.Address;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Avaliability> availabilities;

    protected HealthProfessional() {}

    public HealthProfessional(String name, String email, String healthSector,
                              String phoneNumber, String identityDocument,
                              String professionalDocument, Address address) {
        this.name = name;
        this.email = email;
        this.healthSector = healthSector;
        this.phoneNumber = phoneNumber;
        this.identityDocument = identityDocument;
        this.professionalDocument = professionalDocument;
        this.address = address;
        this.availabilities = new ArrayList<>();
    }

    public HealthProfessional(UUID id, String name, String email, String healthSector,
                              String phoneNumber, String identityDocument,
                              String professionalDocument, Address address) {
        this(name, email, healthSector, phoneNumber, identityDocument, professionalDocument, address);
        this.id = id;
    }

    public HealthProfessional(UUID id, String name, String email, String healthSector,
                              String phoneNumber, String identityDocument,
                              String professionalDocument, Address address,
                              List<Avaliability> availabilities) {
        this(id, name, email, healthSector, phoneNumber, identityDocument, professionalDocument, address);
        this.availabilities = availabilities != null ? new ArrayList<>(availabilities) : new ArrayList<>();
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

    public List<Avaliability> getAvailabilities() {
        return availabilities != null ? new ArrayList<>(availabilities) : new ArrayList<>();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHealthSector(String healthSector) {
        this.healthSector = healthSector;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfessionalDocument(String professionalDocument) {
        this.professionalDocument = professionalDocument;
    }

    public void setIdentityDocument(String identityDocument) {
        this.identityDocument = identityDocument;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setAvailabilities(List<Avaliability> availabilities) {
        this.availabilities = availabilities != null ? new ArrayList<>(availabilities) : new ArrayList<>();
    }
}
