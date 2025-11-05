package br.org.apae.api.servicearea.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "areas_de_atendimento")
public class ServiceArea {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "area", nullable = false, unique = true)
    private String area;

    protected ServiceArea() {
    }

    public ServiceArea(String area) {
        this.area = area;
    }

    public ServiceArea(UUID id, String area) {
        this.id = id;
        this.area = area;
    }

    public UUID getId() {
        return id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}

