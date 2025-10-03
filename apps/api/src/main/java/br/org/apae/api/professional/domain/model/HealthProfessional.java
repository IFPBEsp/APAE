package br.org.apae.api.professional.domain.model;

import br.org.apae.api.common.model.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class HealthProfessional {
    private UUID id;
    private  String healthSector;
    private  String phoneNumber;
    private  String professionalDocument;
    private  String email;
    private  String name;
    private  String identityDocument;
    private Address address;


    public HealthProfessional(UUID id, String healthSector, String phoneNumber,
                              String professionalDocument, String email, String name,
                              String identityDocument, Address address) {
        this.id = id;
        this.healthSector = healthSector;
        this.phoneNumber = phoneNumber;
        this.professionalDocument = professionalDocument;
        this.email = email;
        this.name = name;
        this.identityDocument = identityDocument;
        this.address = address;
    }

    public HealthProfessional(String healthSector, String phoneNumber, String professionalDocument, // Alterado
                              String email, String name, String identityDocument, Address address) { // Alterado
        this.healthSector = healthSector;
        this.phoneNumber = phoneNumber;
        this.professionalDocument = professionalDocument;
        this.email = email;
        this.name = name;
        this.identityDocument = identityDocument;
        this.address = address;
    }
}