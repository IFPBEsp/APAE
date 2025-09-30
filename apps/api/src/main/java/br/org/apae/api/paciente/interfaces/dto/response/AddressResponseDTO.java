package br.org.apae.api.paciente.interfaces.dto.response;

import br.org.apae.api.paciente.domain.model.Address;

import java.util.UUID;


public record AddressResponseDTO(
        UUID id,
        String zipCode,
        String city,
        String state,
        String neighborhood,
        String street,
        String number,
        String complement
) {

    public AddressResponseDTO(Address address) {
        this(
                address.getId(),
                address.getZipCode(),
                address.getCity(),
                address.getState(),
                address.getNeighborhood(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement()
        );
    }
}
