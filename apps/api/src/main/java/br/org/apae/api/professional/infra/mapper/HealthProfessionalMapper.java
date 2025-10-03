package br.org.apae.api.professional.infra.mapper;

import br.org.apae.api.common.dto.AddressDTO;
import br.org.apae.api.common.entity.AddressEntity;
import br.org.apae.api.common.model.Address;
import br.org.apae.api.professional.dto.HealthProfessionalCreateDTO;
import br.org.apae.api.professional.dto.HealthProfessionalResponseDTO;
import org.springframework.stereotype.Component;

@Component
public final class HealthProfessionalMapper {

    // --- Métodos Auxiliares para Endereço ---

    private AddressEntity toAddressEntity(Address model) {
        return AddressEntity.builder()
                .state(model.getState())
                .city(model.getCity())
                .neighborhood(model.getNeighborhood())
                .street(model.getStreet())
                .number(model.getNumber())
                .cep(model.getCep())
                .complement(model.getComplement())
                .build();
    }

    private Address toAddressModel(AddressEntity entity) {
        return new Address(
                entity.getState(),
                entity.getCity(),
                entity.getNeighborhood(),
                entity.getStreet(),
                entity.getNumber(),
                entity.getCep(),
                entity.getComplement()
        );
    }

    private Address toAddressModelFromDTO(AddressDTO dto) {
        return new Address(
                dto.state(),
                dto.city(),
                dto.neighborhood(),
                dto.street(),
                dto.number(),
                dto.cep(),
                dto.complement()
        );
    }

    // --- Mapeamentos Principais ---

    public br.org.apae.api.professional.infra.entity.HealthProfessionalEntity toEntity(br.org.apae.api.professional.domain.model.HealthProfessional model) {

        return br.org.apae.api.professional.infra.entity.HealthProfessionalEntity.builder()
                .id(model.getId())
                .healthSector(model.getHealthSector())
                .phoneNumber(model.getPhoneNumber())
                .professionalDocument(model.getProfessionalDocument())
                .email(model.getEmail())
                .name(model.getName())
                .identityDocument(model.getIdentityDocument())
                .address(toAddressEntity(model.getAddress()))
                .build();
    }

    public br.org.apae.api.professional.domain.model.HealthProfessional toModel(br.org.apae.api.professional.infra.entity.HealthProfessionalEntity entity) {

        Address address = toAddressModel(entity.getAddress());

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

        Address address = toAddressModelFromDTO(dto.address());

        return new br.org.apae.api.professional.domain.model.HealthProfessional(
                dto.healthSector(),
                dto.phoneNumber(),
                dto.professionalDocument(),
                dto.email(),
                dto.name(),
                dto.identityDocument(),
                address
        );
    }

    public HealthProfessionalResponseDTO toResponseDTO(br.org.apae.api.professional.domain.model.HealthProfessional model) {

        Address address = new Address(
                model.getAddress().getState(),
                model.getAddress().getCity(),
                model.getAddress().getNeighborhood(),
                model.getAddress().getStreet(),
                model.getAddress().getNumber(),
                model.getAddress().getCep(),
                model.getAddress().getComplement()
        );

        return new HealthProfessionalResponseDTO(
                model.getId(),
                model.getHealthSector(),
                model.getPhoneNumber(),
                model.getProfessionalDocument(),
                model.getEmail(),
                model.getName(),
                model.getIdentityDocument(),
                address
        );
    }
}