package br.org.apae.api.professional.infra.entity;

import br.org.apae.api.common.entity.EnderecoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "profissionais_saude") //colocar para inglês?
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthProfessionalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String healthSector;
    private String telephone;
    private String docProfessional;
    private String email;
    private String name;
    private  String generalRegistry;

    @Embedded
    private EnderecoEntity address;

}