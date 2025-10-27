package br.org.apae.profissional_da_saude.domain.model;

import java.util.UUID;

public class Falta {

    private UUID id;
    private String motivo;

    public UUID getId(){
        return id;
    }
    public void setId(UUID id){
        this.id=id;
    }

    public String getMotivo(){
        return motivo;
    }
    public void setMotivo(String motivo){
        this.motivo=motivo;
    }
    
}