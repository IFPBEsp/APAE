package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.profissional_da_saude.domain.model.ProfissionalSaude;
import br.org.apae.profissional_da_saude.infrastructure.entity.ProfissionalSaudeEntity;

public class ProfissionalSaudeMapper {

  public static ProfissionalSaudeEntity toEntity(ProfissionalSaude model) {
    return ProfissionalSaudeEntity.builder()
        .areaDaSaude(model.getAreaDaSaude())
        .telefone(model.getTelefone())
        .docProfissional(model.getDocProfissional())
        .email(model.getEmail())
        .nome(model.getNome())
        .build();
  }

  public static ProfissionalSaude toModel(ProfissionalSaudeEntity entity) {
    return new ProfissionalSaude(
        entity.getId(),
        entity.getAreaDaSaude(),
        entity.getTelefone(),
        entity.getDocProfissional(),
        entity.getEmail(),
        entity.getNome());
  }

  public static ProfissionalSaude toDomain(ProfissionalSaudeCreateDTO dto) {
    return new ProfissionalSaude(
        dto.getAreaDaSaude(),
        dto.getTelefone(),
        dto.getDocProfissional(),
        dto.getEmail(),
        dto.getNome());
  }

  public static ProfissionalSaudeResponseDTO toResponseDTO(ProfissionalSaude model) {
    ProfissionalSaudeResponseDTO dto = new ProfissionalSaudeResponseDTO();
    dto.setId(model.getId());
    dto.setAreaDaSaude(model.getAreaDaSaude());
    dto.setTelefone(model.getTelefone());
    dto.setDocProfissional(model.getDocProfissional());
    dto.setEmail(model.getEmail());
    dto.setNome(model.getNome());
    return dto;
  }
}
