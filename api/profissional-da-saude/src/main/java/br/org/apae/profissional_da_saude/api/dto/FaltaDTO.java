package br.org.apae.profissional_da_saude.api.dto;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class FaltaDTO {

    private UUID id;
    private String motivo;
}