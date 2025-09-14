package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.api.dto.DisponibilidadeDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.profissional_da_saude.domain.model.ProfissionalSaude;
import br.org.apae.profissional_da_saude.infrastructure.entity.DisponibilidadeEntity;
import br.org.apae.profissional_da_saude.infrastructure.entity.ProfissionalSaudeEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class ProfissionalSaudeMapper {

    private ProfissionalSaudeMapper() {}

    public static ProfissionalSaudeEntity toEntity(ProfissionalSaude model) {
        return ProfissionalSaudeEntity.builder()
                .id(model.getId())
                .areaDaSaude(model.getAreaDaSaude())
                .telefone(model.getTelefone())
                .docProfissional(model.getDocProfissional())
                .email(model.getEmail())
                .nome(model.getNome())
                .build();
    }

    public static ProfissionalSaude toModel(ProfissionalSaudeEntity entity) {
        ProfissionalSaude model = new ProfissionalSaude(
                entity.getId(),
                entity.getAreaDaSaude(),
                entity.getTelefone(),
                entity.getDocProfissional(),
                entity.getEmail(),
                entity.getNome());

        if (entity.getDisponibilidades() != null) {
            List<DisponibilidadeEntity> disponibilidades = entity.getDisponibilidades()
            .stream()
            .map(dispEntity -> new DisponibilidadeEntity(
            dispEntity.getDiaSemana(),
            dispEntity.getTurno(),
            entity.getId()
            ))
            .collect(Collectors.toList());
            model.setDisponibilidades(disponibilidades);
        }
        return model;
    }

    public static ProfissionalSaude toDomain(ProfissionalSaudeCreateDTO dto) {
        ProfissionalSaude model = new ProfissionalSaude(
                dto.getAreaDaSaude(),
                dto.getTelefone(),
                dto.getDocProfissional(),
                dto.getEmail(),
                dto.getNome());

        // Mapear disponibilidades se existirem
        if (dto.getDisponibilidades() != null) {
            List<DisponibilidadeEntity> disponibilidades = dto.getDisponibilidades()
                    .stream()
                    .map(dispDto -> new DisponibilidadeEntity(
                            dispDto.getDia(),
                            dispDto.getTurno(),
                            null // ID será setado depois no service
                    ))
                    .collect(Collectors.toList());
            model.setDisponibilidades(disponibilidades);
        }

        return model;
    }

    public static ProfissionalSaudeResponseDTO toResponseDTO(ProfissionalSaude model) {
        ProfissionalSaudeResponseDTO dto = new ProfissionalSaudeResponseDTO();
        dto.setId(model.getId());
        dto.setAreaDaSaude(model.getAreaDaSaude());
        dto.setTelefone(model.getTelefone());
        dto.setDocProfissional(model.getDocProfissional());
        dto.setEmail(model.getEmail());
        dto.setNome(model.getNome());

        // Mapear disponibilidades se existirem
        if (model.getDisponibilidades() != null) {
            List<DisponibilidadeDTO> disponibilidades = model.getDisponibilidades()
                    .stream()
                    .map(disp -> new DisponibilidadeDTO(disp.getDiaSemana(), disp.getTurno()))
                    .collect(Collectors.toList());
            dto.setDisponibilidades(disponibilidades);
        }

        return dto;
    }
}