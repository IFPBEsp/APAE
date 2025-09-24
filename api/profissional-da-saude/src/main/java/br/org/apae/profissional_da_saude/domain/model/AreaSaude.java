package br.org.apae.profissional_da_saude.domain.model;

public class AreaSaude {

    private Integer id;

    private String area;

    public AreaSaude(Integer id, String area) {
        this.id = id;
        this.area = area;
    }

    public AreaSaude(String area) {
        this.area = area;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
