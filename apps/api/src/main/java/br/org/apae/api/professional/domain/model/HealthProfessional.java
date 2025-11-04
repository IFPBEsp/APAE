package br.org.apae.api.professional.domain.model;

import jakarta.persistence.*;
import java.util.List;
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

    // ✅ RELACIONAMENTO ADICIONADO
    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Avaliability> availabilities;

    protected HealthProfessional() {}

    public HealthProfessional(String name, String email, String healthSector, String phoneNumber,
                              String identityDocument, String professionalDocument, Address address) {
        this.name = name;
        this.email = email;
        this.healthSector = healthSector;
        this.phoneNumber = phoneNumber;
        this.identityDocument = identityDocument;
        this.professionalDocument = professionalDocument;
        this.address = address;
    }

    public HealthProfessional(UUID id, String name, String email, String healthSector, String phoneNumber,
                              String identityDocument, String professionalDocument, Address address,
                              List<Avaliability> availabilities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.healthSector = healthSector;
        this.phoneNumber = phoneNumber;
        this.identityDocument = identityDocument;
        this.professionalDocument = professionalDocument;
        this.address = address;
        this.availabilities = availabilities;
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
        return availabilities;
    }

    public void setAvailabilities(List<Avaliability> availabilities) {
        this.availabilities = availabilities;
    }
}
