package br.org.apae.api.servicetype.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "areas_de_atendimento")
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "area", nullable = false, unique = true)
    private String area;

    protected ServiceType() {
    }

    public ServiceType(String area) {
        this.area = area;
    }

    public ServiceType(Integer id, String area) {
        this.id = id;
        this.area = area;
    }

    public Integer getId() {
        return id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
