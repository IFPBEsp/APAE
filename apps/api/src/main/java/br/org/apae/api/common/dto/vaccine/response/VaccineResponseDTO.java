package br.org.apae.api.common.dto.vaccine.response;

public class VaccineResponseDTO {

    private Long id;
    private String name;

    public VaccineResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}