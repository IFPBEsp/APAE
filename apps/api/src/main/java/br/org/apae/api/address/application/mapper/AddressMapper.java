package br.org.apae.api.address.application.mapper;

import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.address.UpdateAddressDTO;

import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(CreateAddressDTO dto) {
        return new Address(
                dto.city(),
                dto.cep(),
                dto.state(),
                dto.neighborhood(),
                dto.street(),
                dto.number(),
                (dto.complement() != null && !dto.complement().isBlank()) ? dto.complement() : null);
    }
    
    public Address toEntityFromResponse(AddressResponseDTO dto) {
        return new Address(
                dto.id(),
                dto.city(),
                dto.cep(),
                dto.state(),
                dto.neighborhood(),
                dto.street(),
                dto.number(),
                (dto.complement() != null && !dto.complement().isBlank()) ? dto.complement() : null);
    }

    public Address updateEntityFromDto(Address address, UpdateAddressDTO dto) {
        if (address == null) {
            return new Address(
                    dto.city(),
                    dto.cep(),
                    dto.state(),
                    dto.neighborhood(),
                    dto.street(),
                    dto.number(),
                    dto.complement());
        }

        address.update(
                dto.city(),
                dto.cep(),
                dto.state(),
                dto.neighborhood(),
                dto.street(),
                dto.number(),
                dto.complement());
        return address;
    }

    public AddressResponseDTO toResponseDTO(Address address) {
        return new AddressResponseDTO(address);
    }
}
