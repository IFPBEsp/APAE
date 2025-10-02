package br.org.apae.api.professional.dto;

import br.org.apae.api.common.model.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class HealthProfessionalResponseDTO {

    private UUID id;
    private String healthSector;
    private String telephone;
    private String docProfessional;
    private String email;
    private String name;
    private  String generalRegistry;
    private Address address;
}