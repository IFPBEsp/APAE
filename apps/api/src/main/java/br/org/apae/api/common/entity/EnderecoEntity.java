package br.org.apae.api.common.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoEntity {
    private String state;
    private String city;
    private String neighborhood;
    private String road;
    private String number;
    private String cep;
    private String complement;

}