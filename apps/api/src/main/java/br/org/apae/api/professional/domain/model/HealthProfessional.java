package br.org.apae.api.professional.domain.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.servicearea.domain.model.ServiceArea;

@Entity
@Table(name = "profissionais_da_saude")
public class HealthProfessional {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "area_de_atendimento", 
        referencedColumnName = "area",
        foreignKey = @ForeignKey(
            name = "FK_HEALTH_PROFESSIONAL_SERVICE_AREA",
            foreignKeyDefinition = "FOREIGN KEY (area_de_atendimento) REFERENCES areas_de_atendimento(area) ON UPDATE CASCADE"
    ))
    private ServiceArea serviceArea;

    @Column(name = "documento_profissional", unique = true)
    private String professionalDocument;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Availability> availabilities = new ArrayList<>();

    @Column(name = "foto_perfil")
    private String profilePhoto;

    protected HealthProfessional() {
    }

    public HealthProfessional(User user, ServiceArea serviceArea, String professionalDocument) {
        this.user = user;
        this.serviceArea = serviceArea;
        this.professionalDocument = professionalDocument;
    }

    public HealthProfessional(UUID id, User user, ServiceArea serviceArea, String professionalDocument) {
        this.id = id;
        this.user = user;
        this.serviceArea = serviceArea;
        this.professionalDocument = professionalDocument;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public UUID getUserId() {
        return user != null ? user.getId() : null;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return user != null ? user.getFullName() : null;
    }

    public void setName(String name) {
        updateUserProfile(name, getEmail(), getPhoneNumber(), getIdentityDocument());
    }

    public String getEmail() {
        return user != null ? user.getUsername() : null;
    }

    public void setEmail(String email) {
        updateUserProfile(getName(), email, getPhoneNumber(), getIdentityDocument());
    }

    public ServiceArea getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(ServiceArea serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getPhoneNumber() {
        return user != null ? user.getPhoneNumber() : null;
    }

    public void setPhoneNumber(String phoneNumber) {
        updateUserProfile(getName(), getEmail(), phoneNumber, getIdentityDocument());
    }

    public String getProfessionalDocument() {
        return professionalDocument;
    }

    public void setProfessionalDocument(String professionalDocument) {
        this.professionalDocument = professionalDocument;
    }

    public String getIdentityDocument() {
        return user != null ? user.getIdentityDocument() : null;
    }

    public void setIdentityDocument(String identityDocument) {
        updateUserProfile(getName(), getEmail(), getPhoneNumber(), identityDocument);
    }

    public Address getAddress() {
        return user != null ? user.getAddress() : null;
    }

    public void setAddress(Address address) {
        if (user != null) {
            user.updateAddress(address);
        }
    }

    public List<Availability> getAvailabilities() { return availabilities; }

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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    private void updateUserProfile(String name, String email, String phoneNumber, String identityDocument) {
        if (user != null) {
            user.updateProfile(email, name, phoneNumber, identityDocument);
        }
    }
}
