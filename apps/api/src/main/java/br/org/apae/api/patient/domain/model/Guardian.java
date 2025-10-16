package br.org.apae.api.patient.domain.model;

import br.org.apae.api.common.model.Address;
import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

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

    @Deprecated
    protected Guardian() {}

    public Guardian(String name, String contact, String kinship, Address address) {
        Objects.requireNonNull(name, "O nome do responsável não pode ser nulo.");
        Objects.requireNonNull(contact, "O contato do responsável não pode ser nulo.");
        Objects.requireNonNull(kinship, "O parentesco do responsável não pode ser nulo.");
        this.name = name;
        this.contact = contact;
        this.kinship = kinship;
        this.address = address;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getKinship() { return kinship; }
    public Address getAddress() { return address; }

    public void setName(String name) { this.name = name; }
    public void setContact(String contact) { this.contact = contact; }
    public void setKinship(String kinship) { this.kinship = kinship; }
    public void setAddress(Address address) { this.address = address; }
}