package br.org.apae.profissional_da_saude.infrastructure.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "area_saude")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaSaudeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(unique = true)
    private String area;

}
