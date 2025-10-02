package br.org.apae.api.professional.domain.model;



import br.org.apae.api.common.model.Address;

import java.util.UUID;


public class HealthProfessional {
    private UUID id;
    private  String healthSector;
    private  String telephone;
    private  String docProfessional;
    private  String email;
    private  String name;
    private  String generalRegistry;
    private Address address;


    public HealthProfessional(UUID id, String healthSector, String telephone,
                              String docProfessional, String email, String name,
                              String generalRegistry, Address address) {
        this.id = id;
        this.healthSector = healthSector;
        this.telephone = telephone;
        this.docProfessional = docProfessional;
        this.email = email;
        this.name = name;
        this.generalRegistry = generalRegistry;
        this.address = address;
    }

    public HealthProfessional(String healthSector, String telephone, String docProfessional,
                              String email, String name, String generalRegistry, Address address) {
        this.healthSector = healthSector;
        this.telephone = telephone;
        this.docProfessional = docProfessional;
        this.email = email;
        this.name = name;
        this.generalRegistry = generalRegistry;
        this.address = address;

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getHealthSector() {
        return healthSector;
    }

    public void setHealthSector(String healthSector) {
        this.healthSector = healthSector;
    }

    public String getDocProfessional() {
        return docProfessional;
    }

    public void setDocProfessional(String docProfessional) {
        this.docProfessional = docProfessional;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeneralRegistry() {
        return generalRegistry;
    }

    public void setGeneralRegistry(String generalRegistry) {
        this.generalRegistry = generalRegistry;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}