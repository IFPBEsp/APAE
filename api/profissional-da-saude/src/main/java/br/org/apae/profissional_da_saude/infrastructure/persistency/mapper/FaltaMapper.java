package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.api.dto.FaltaCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.FaltaResponseDTO;
import br.org.apae.profissional_da_saude.domain.model.Falta;
import br.org.apae.profissional_da_saude.infrastructure.entity.FaltaEntity;

public class FaltaMapper {

    private FaltaMapper() {
    }

    public static FaltaEntity toEntity(Falta falta) {
        return FaltaEntity.builder()
                .id(falta.getId())
                .data(falta.getData())
                .hora(falta.getHora())
                .justificada(falta.getJustificada())
                .motivo(falta.getMotivo())
                .fkAtendimento(falta.getFkAtendimento())
                .fkProfissional(falta.getFkProfissional())
                .fkCadastroAnual(falta.getFkCadastroAnual())
                .dataCriacao(falta.getDataCriacao())
                .build();
    }

    public static Falta toModel(FaltaEntity entity) {
        return new Falta(
                entity.getId(),
                entity.getData(),
                entity.getHora(),
                entity.getJustificada(),
                entity.getMotivo(),
                entity.getFkAtendimento(),
                entity.getFkProfissional(),
                entity.getFkCadastroAnual(),
                entity.getDataCriacao());
    }

    public static FaltaResponseDTO toResponseDTO(Falta falta) {
        return FaltaResponseDTO.builder()
                .id(falta.getId())
                .data(falta.getData())
                .hora(falta.getHora())
                .justificada(falta.getJustificada())
                .motivo(falta.getMotivo())
                .fkAtendimento(falta.getFkAtendimento())
                .fkProfissional(falta.getFkProfissional())
                .fkCadastroAnual(falta.getFkCadastroAnual())
                .dataCriacao(falta.getDataCriacao())
                .build();
    }

    public static Falta toDomain(FaltaCreateDTO dto) {
        return new Falta(
                dto.getData(),
                dto.getHora(),
                dto.getJustificada(),
                dto.getMotivo(),
                dto.getFkAtendimento(),
                dto.getFkProfissional(),
                dto.getFkCadastroAnual());
    }

}
