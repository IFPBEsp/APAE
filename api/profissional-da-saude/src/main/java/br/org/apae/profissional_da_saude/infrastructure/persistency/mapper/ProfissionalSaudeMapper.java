package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.api.dto.DisponibilidadeDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.profissional_da_saude.domain.model.Endereco;
import br.org.apae.profissional_da_saude.domain.model.ProfissionalSaude;
import br.org.apae.profissional_da_saude.infrastructure.entity.DisponibilidadeEntity;
import br.org.apae.profissional_da_saude.infrastructure.entity.EnderecoEntity;
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
  public static ProfissionalSaudeEntity toEntity(ProfissionalSaude model) {

    return ProfissionalSaudeEntity.builder()
        .id(model.getId())
        .areaDaSaude(model.getAreaDaSaude())
        .telefone(model.getTelefone())
        .docProfissional(model.getDocProfissional())
        .email(model.getEmail())
        .nome(model.getNome())
        .rg(model.getRg())
        .endereco(new EnderecoEntity( model.getEndereco().getEstado(),
                model.getEndereco().getCidade(),
                model.getEndereco().getBairro(),
                model.getEndereco().getRua(),
                model.getEndereco().getNumero(),
                model.getEndereco().getCep(),
                model.getEndereco().getComplemento()))
        .build();
  }

  public static ProfissionalSaude toModel(ProfissionalSaudeEntity entity) {

    Endereco endereco = new Endereco( entity.getEndereco().getEstado(),
            entity.getEndereco().getCidade(),
            entity.getEndereco().getBairro(),
            entity.getEndereco().getRua(),
            entity.getEndereco().getNumero(),
            entity.getEndereco().getCep(),
            entity.getEndereco().getComplemento());

    return new ProfissionalSaude(
            entity.getId(),
            entity.getAreaDaSaude(),
            entity.getTelefone(),
            entity.getDocProfissional(),
            entity.getEmail(),
            entity.getNome(),
            entity.getRg(),
            endereco
    );
  }

  public static ProfissionalSaude toDomain(ProfissionalSaudeCreateDTO dto) {

    Endereco endereco = new Endereco(
            dto.getEndereco().getEstado(),
            dto.getEndereco().getCidade(),
            dto.getEndereco().getBairro(),
            dto.getEndereco().getRua(),
            dto.getEndereco().getNumero(),
            dto.getEndereco().getCep(),
            dto.getEndereco().getComplemento()
    );


    return new ProfissionalSaude(
            dto.getAreaDaSaude(),
            dto.getTelefone(),
            dto.getDocProfissional(),
            dto.getEmail(),
            dto.getNome(),
            dto.getRg(),
            endereco
    );
  }

  public static ProfissionalSaudeResponseDTO toResponseDTO(ProfissionalSaude model) {
    ProfissionalSaudeResponseDTO dto = new ProfissionalSaudeResponseDTO();
    dto.setId(model.getId());
    dto.setAreaDaSaude(model.getAreaDaSaude());
    dto.setTelefone(model.getTelefone());
    dto.setDocProfissional(model.getDocProfissional());
    dto.setEmail(model.getEmail());
    dto.setNome(model.getNome());
    dto.setRg(model.getRg());

    Endereco endereco = new Endereco();
    endereco.setEstado(model.getEndereco().getEstado());
    endereco.setCidade(model.getEndereco().getCidade());
    endereco.setBairro(model.getEndereco().getBairro());
    endereco.setRua(model.getEndereco().getRua());
    endereco.setNumero(model.getEndereco().getNumero());
    endereco.setCep(model.getEndereco().getCep());
    endereco.setComplemento(model.getEndereco().getComplemento());

    dto.setEndereco(endereco);

    return dto;
  }
}
