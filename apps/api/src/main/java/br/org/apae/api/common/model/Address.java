package br.org.apae.api.common.model;

import jakarta.persistence.*;
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

    protected Address() {
    }

    public Address(String city, String cep, String state, String neighborhood, String street, String number, String complement) {
        this.city = city;
        this.cep = cep;
        this.state = state;
        this.neighborhood = neighborhood;
        this.street = street;
        this.number = number;
        this.complement = complement;
    }

    public UUID getId() { return id; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getComplement() { return complement; }
    public void setComplement(String complement) { this.complement = complement; }
}