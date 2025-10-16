package br.org.apae.api.common.model;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "enderecos")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cidade", nullable = false)
    private String city;

    @Column(name = "cep", nullable = false)
    private String cep;

    @Column(name = "estado", nullable = false)
    private String state;

    @Column(name = "bairro", nullable = false)
    private String neighborhood;

    @Column(name = "rua", nullable = false)
    private String street;

    @Column(name = "numero", nullable = false)
    private String number;

    @Column(name = "complemento")
    private String complement;

    @Deprecated
    protected Address() {}

    public Address(String city, String cep, String state, String neighborhood, String street, String number) {
        Objects.requireNonNull(city, "A cidade não pode ser nula.");
        Objects.requireNonNull(cep, "O CEP não pode ser nulo.");
        Objects.requireNonNull(state, "O estado não pode ser nulo.");
        Objects.requireNonNull(neighborhood, "O bairro não pode ser nulo.");
        Objects.requireNonNull(street, "A rua não pode ser nula.");
        Objects.requireNonNull(number, "O número não pode ser nulo.");
        this.city = city;
        this.cep = cep;
        this.state = state;
        this.neighborhood = neighborhood;
        this.street = street;
        this.number = number;
    }

    public UUID getId() { return id; }
    public String getCity() { return city; }
    public String getCep() { return cep; }
    public String getState() { return state; }
    public String getNeighborhood() { return neighborhood; }
    public String getStreet() { return street; }
    public String getNumber() { return number; }
    public String getComplement() { return complement; }

    public void setCity(String city) { this.city = city; }
    public void setCep(String cep) { this.cep = cep; }
    public void setState(String state) { this.state = state; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }
    public void setStreet(String street) { this.street = street; }
    public void setNumber(String number) { this.number = number; }
    public void setComplement(String complement) { this.complement = complement; }
}