package br.org.apae.api.patient.domain.model;

import br.org.apae.api.common.dto.paciente.dto.create.CreateAddressDTO;
import br.org.apae.api.common.dto.paciente.dto.update.UpdateAddressDTO;
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

    protected Address() {
    }

    private Address(Builder builder) {
        this.city = builder.city;
        this.cep = builder.cep;
        this.state = builder.state;
        this.neighborhood = builder.neighborhood;
        this.street = builder.street;
        this.number = builder.number;
        this.complement = builder.complement;
    }

    public UUID getId() { return id; }
    public String getCity() { return city; }
    public String getCep() { return cep; }
    public String getState() { return state; }
    public String getNeighborhood() { return neighborhood; }
    public String getStreet() { return street; }
    public String getNumber() { return number; }
    public String getComplement() { return complement; }

    public static Address from(CreateAddressDTO dto) {
        if (dto == null) return null;
        return Address.builder()
                .city(dto.city())
                .cep(dto.cep())
                .state(dto.state())
                .neighborhood(dto.neighborhood())
                .street(dto.street())
                .number(dto.number())
                .complement(dto.complement())
                .build();
    }

    public void updateWith(UpdateAddressDTO dto) {
        if (dto == null) return;
        this.city = dto.city();
        this.cep = dto.cep();
        this.state = dto.state();
        this.neighborhood = dto.neighborhood();
        this.street = dto.street();
        this.number = dto.number();
        this.complement = dto.complement();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String city;
        private String cep;
        private String state;
        private String neighborhood;
        private String street;
        private String number;
        private String complement;

        public Builder city(String city) { this.city = city; return this; }
        public Builder cep(String cep) { this.cep = cep; return this; }
        public Builder state(String state) { this.state = state; return this; }
        public Builder neighborhood(String neighborhood) { this.neighborhood = neighborhood; return this; }
        public Builder street(String street) { this.street = street; return this; }
        public Builder number(String number) { this.number = number; return this; }
        public Builder complement(String complement) { this.complement = complement; return this; }

        public Address build() {
            Objects.requireNonNull(city, "A cidade não pode ser nula.");
            Objects.requireNonNull(cep, "O CEP não pode ser nulo.");
            Objects.requireNonNull(state, "O estado não pode ser nulo.");
            Objects.requireNonNull(neighborhood, "O bairro não pode ser nulo.");
            Objects.requireNonNull(street, "A rua não pode ser nula.");
            Objects.requireNonNull(number, "O número não pode ser nulo.");

            return new Address(this);
        }
    }
}
