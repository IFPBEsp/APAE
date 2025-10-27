package br.org.apae.profissional_da_saude.api.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FaltaDTO {

    private UUID id;
    private String motivo;

    public FaltaDTO(UUID id, String motivo) {
        //TODO Auto-generated constructor stub
    }
}