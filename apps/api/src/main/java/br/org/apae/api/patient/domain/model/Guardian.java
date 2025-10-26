package br.org.apae.api.patient.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

import br.org.apae.api.address.domain.model.Address;

@Entity
@Table(name = "responsaveis")
public class Guardian {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "contato", nullable = false)
    private String contact;

    @Column(name = "parentesco", nullable = false)
    private String kinship;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "paciente_id", nullable = false)
    private UUID patientId;

    protected Guardian() {
    }

    public Guardian(String name, String contact, String kinship, Address address, UUID patientId) {
        this.name = name;
        this.contact = contact;
        this.kinship = kinship;
        this.address = address;
        this.patientId = patientId;
    }

    public Guardian(UUID id, String name, String contact, String kinship, Address address, UUID patientId) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.kinship = kinship;
        this.address = address;
        this.patientId = patientId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getKinship() {
        return kinship;
    }

    public Address getAddress() {
        return address;
    }

    public UUID getPatientId() {
        return patientId;
    }
}