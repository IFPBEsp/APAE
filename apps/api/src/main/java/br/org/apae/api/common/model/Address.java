package br.org.apae.api.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private String number;
    private String cep;
    private String complement;

    public Address() {
    }

    public Address(String state,
                   String city,
                   String neighborhood,
                   String street,
                   String number,
                   String cep,
                   String complement) {

        this.state = state;
        this.city = city;
        this.neighborhood = neighborhood;
        this.street = street;
        this.number = number;
        this.cep = cep;
        this.complement = complement;
    }
}