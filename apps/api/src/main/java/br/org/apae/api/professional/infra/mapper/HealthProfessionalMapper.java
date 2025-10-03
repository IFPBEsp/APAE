package br.org.apae.api.professional.infra.mapper;

import br.org.apae.api.common.entity.AddressEntity;
import br.org.apae.api.common.model.Address;
import br.org.apae.api.professional.dto.HealthProfessionalCreateDTO;
import org.springframework.stereotype.Component;

@Component
public final class HealthProfessionalMapper {

    public br.org.apae.api.professional.infra.entity.HealthProfessionalEntity toEntity(br.org.apae.api.professional.domain.model.HealthProfessional model) {

        return br.org.apae.api.professional.infra.entity.HealthProfessionalEntity.builder()
                .id(model.getId())
                .healthSector(model.getHealthSector())
                .phoneNumber(model.getPhoneNumber())
                .professionalDocument(model.getProfessionalDocument())
                .email(model.getEmail())
                .name(model.getName())
                .identityDocument(model.getIdentityDocument())
                .address(new AddressEntity( model.getAddress().getState(),
                        model.getAddress().getCity(),
                        model.getAddress().getNeighborhood(),
                        model.getAddress().getStreet(),
                        model.getAddress().getNumber(),
                        model.getAddress().getCep(),
                        model.getAddress().getComplement()))
                .build();
    }

    public br.org.apae.api.professional.domain.model.HealthProfessional toModel(br.org.apae.api.professional.infra.entity.HealthProfessionalEntity entity) {

        Address address = new Address( entity.getAddress().getState(),
                entity.getAddress().getCity(),
                entity.getAddress().getNeighborhood(),
                entity.getAddress().getStreet(),
                entity.getAddress().getNumber(),
                entity.getAddress().getCep(),
                entity.getAddress().getComplement());

        return new br.org.apae.api.professional.domain.model.HealthProfessional(
                entity.getId(),
                entity.getHealthSector(),
                entity.getPhoneNumber(),
                entity.getProfessionalDocument(),
                entity.getEmail(),
                entity.getName(),
                entity.getIdentityDocument(),
                address
        );
    }

    public br.org.apae.api.professional.domain.model.HealthProfessional toDomain(HealthProfessionalCreateDTO dto) {

        Address address = new Address(
                dto.getAddress().getState(),
                dto.getAddress().getCity(),
                dto.getAddress().getNeighborhood(),
                dto.getAddress().getStreet(),
                dto.getAddress().getNumber(),
                dto.getAddress().getCep(),
                dto.getAddress().getComplement()
        );


        return new br.org.apae.api.professional.domain.model.HealthProfessional(
                dto.getHealthSector(),
                dto.getPhoneNumber(),
                dto.getProfessionalDocument(),
                dto.getEmail(),
                dto.getName(),
                dto.getIdentityDocument(),
                address
        );
    }

    public br.org.apae.api.professional.dto.HealthProfessionalResponseDTO toResponseDTO(br.org.apae.api.professional.domain.model.HealthProfessional model) {
        br.org.apae.api.professional.dto.HealthProfessionalResponseDTO dto = new br.org.apae.api.professional.dto.HealthProfessionalResponseDTO();
        dto.setId(model.getId());
        dto.setHealthSector(model.getHealthSector());
        dto.setPhoneNumber(model.getPhoneNumber());
        dto.setProfessionalDocument(model.getProfessionalDocument());
        dto.setEmail(model.getEmail());
        dto.setName(model.getName());
        dto.setIdentityDocument(model.getIdentityDocument());

        Address address = new Address();
        address.setState(model.getAddress().getState());
        address.setCity(model.getAddress().getCity());
        address.setNeighborhood(model.getAddress().getNeighborhood());
        address.setStreet(model.getAddress().getStreet());
        address.setNumber(model.getAddress().getNumber());
        address.setCep(model.getAddress().getCep());
        address.setComplement(model.getAddress().getComplement());

        dto.setAddress(address);

        return dto;
    }
}