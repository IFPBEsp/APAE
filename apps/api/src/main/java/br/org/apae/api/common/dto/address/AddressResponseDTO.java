package br.org.apae.api.common.dto.address;

import java.util.UUID;

import br.org.apae.api.address.domain.model.Address;

public record AddressResponseDTO(
        UUID id,
        String cep,
        String city,
        String state,
        String neighborhood,
        String street,
        String number,
        String complement) {

    public AddressResponseDTO(Address address) {
        this(
                address.getId(),
                address.getCep(),
                address.getCity(),
                address.getState(),
                address.getNeighborhood(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement());
    }
}
