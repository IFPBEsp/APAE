package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.profissional_da_saude.domain.model.ProfissionalSaude;
import br.org.apae.profissional_da_saude.infrastructure.entity.ProfissionalSaudeEntity;

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
            .rg(model.getRg())
            .estado(model.getEstado())
            .cidade(model.getCidade())
            .bairro(model.getBairro())
            .rua(model.getRua())
            .numero(model.getNumero())
            .cep(model.getCep())
            .complemento(model.getComplemento())
            .build();
  }

  public static ProfissionalSaude toModel(ProfissionalSaudeEntity entity) {
    return new ProfissionalSaude(
            entity.getId(),
            entity.getAreaDaSaude(),
            entity.getTelefone(),
            entity.getDocProfissional(),
            entity.getEmail(),
            entity.getNome(),
            entity.getRg(),
            entity.getEstado(),
            entity.getCidade(),
            entity.getBairro(),
            entity.getRua(),
            entity.getNumero(),
            entity.getCep(),
            entity.getComplemento());
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
