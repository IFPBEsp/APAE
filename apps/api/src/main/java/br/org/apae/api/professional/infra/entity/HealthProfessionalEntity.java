package br.org.apae.api.professional.infra.entity;

import br.org.apae.api.common.entity.AddressEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "health_professionals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthProfessionalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String healthSector;
    private String phoneNumber;
    private String professionalDocument;
    private String email;
    private String name;
    private String identityDocument;

    @Embedded
    private AddressEntity address;

}