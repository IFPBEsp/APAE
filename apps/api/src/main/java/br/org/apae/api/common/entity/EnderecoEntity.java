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
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    private String cep;
    private String complemento;
}