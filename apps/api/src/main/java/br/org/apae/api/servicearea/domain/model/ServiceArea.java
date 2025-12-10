package br.org.apae.api.servicearea.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "areas_de_atendimento")
public class ServiceArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "area", nullable = false, unique = true)
    private String area;

    protected ServiceArea() {
    }

    public ServiceArea(String area) {
        this.area = area;
    }

    public ServiceArea(Integer id, String area) {
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
