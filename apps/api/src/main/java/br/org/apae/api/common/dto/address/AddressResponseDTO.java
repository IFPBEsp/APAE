package br.org.apae.api.common.dto.address;

import br.org.apae.api.common.model.Address;

import java.util.UUID;


public record AddressResponseDTO(
        UUID id,
        String cep,
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
                address.getCep(),
                address.getCity(),
                address.getState(),
                address.getNeighborhood(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement()
        );
    }
}
