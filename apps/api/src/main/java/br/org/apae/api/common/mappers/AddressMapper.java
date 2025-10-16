package br.org.apae.api.common.mappers;

import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.address.UpdateAddressDTO;
import br.org.apae.api.common.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(CreateAddressDTO dto) {
        if (dto == null) return null;
        Address address = new Address(dto.city(), dto.cep(), dto.state(), dto.neighborhood(), dto.street(), dto.number());
        address.setComplement(dto.complement());
        return address;
    }

    public void updateEntityFromDto(Address address, UpdateAddressDTO dto) {
        if (dto == null || address == null) return;
        address.setCity(dto.city());
        address.setCep(dto.cep());
        address.setState(dto.state());
        address.setNeighborhood(dto.neighborhood());
        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setComplement(dto.complement());
    }
}
