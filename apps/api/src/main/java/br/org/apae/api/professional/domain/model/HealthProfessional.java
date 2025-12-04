package br.org.apae.api.professional.domain.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.servicearea.domain.model.ServiceArea;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_de_atendimento", referencedColumnName = "area")
    private ServiceArea serviceArea;

    @Column(name = "contato", nullable = false)
    private String phoneNumber;

    @Column(name = "documento_profissional", nullable = false, unique = true)
    private String professionalDocument;

    @Column(name = "rg", unique = true)
    private String identityDocument;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Availability> availabilities = new ArrayList<>();

    protected HealthProfessional() {
    }

    public HealthProfessional(String name, String email, ServiceArea serviceArea, String phoneNumber,
            String identityDocument,
            String professionalDocument, Address address) {
        this.name = name;
        this.email = email;
        this.serviceArea = serviceArea;
        this.phoneNumber = phoneNumber;
        this.identityDocument = identityDocument;
        this.professionalDocument = professionalDocument;
        this.address = address;
    }

    public HealthProfessional(UUID id, String name, String email, ServiceArea serviceArea, String phoneNumber,
            String identityDocument, String professionalDocument, Address address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.serviceArea = serviceArea;
        this.phoneNumber = phoneNumber;
        this.identityDocument = identityDocument;
        this.professionalDocument = professionalDocument;
        this.address = address;
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

    public ServiceArea getServiceArea() {
        return serviceArea;
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

    public List<Availability> getAvailabilities() { return availabilities; }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setAvailabilities(List<Availability> newAvailabilities) {
        this.availabilities.clear();
        if (newAvailabilities != null) {
            this.availabilities.addAll(newAvailabilities);
        }
    }

    public void addAvailability(Availability availability) {
        this.availabilities.add(availability);
        availability.setProfessional(this);
    }
}
