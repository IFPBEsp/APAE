package br.org.apae.api.common.model;

public class Address {

    private String state;
    private String city;
    private String neighborhood;
    private String road;
    private String number;
    private String cep;
    private String complement;

    public Address() {
    }

    public Address(String state,
                   String city,
                   String neighborhood,
                   String road,
                   String number,
                   String cep,
                   String complement) {

        this.state = state;
        this.city = city;
        this.neighborhood = neighborhood;
        this.road = road;
        this.number = number;
        this.cep = cep;
        this.complement = complement;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }
}