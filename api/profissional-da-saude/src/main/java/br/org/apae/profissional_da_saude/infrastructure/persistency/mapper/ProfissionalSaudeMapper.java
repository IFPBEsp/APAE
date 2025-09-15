package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.api.dto.DisponibilidadeDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.profissional_da_saude.domain.model.Disponibilidade;
import br.org.apae.profissional_da_saude.domain.model.Endereco;
import br.org.apae.profissional_da_saude.domain.model.ProfissionalSaude;
import br.org.apae.profissional_da_saude.infrastructure.entity.DisponibilidadeEntity;
import br.org.apae.profissional_da_saude.infrastructure.entity.EnderecoEntity;
import br.org.apae.profissional_da_saude.infrastructure.entity.ProfissionalSaudeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ProfissionalSaudeMapper {

        private ProfissionalSaudeMapper() {
        }

        public static ProfissionalSaudeEntity toEntity(ProfissionalSaude model) {
                EnderecoEntity enderecoEntity = Optional.ofNullable(model.getEndereco())
                                .map(endereco -> new EnderecoEntity(
                                                endereco.getEstado(),
                                                endereco.getCidade(),
                                                endereco.getBairro(),
                                                endereco.getRua(),
                                                endereco.getNumero(),
                                                endereco.getCep(),
                                                endereco.getComplemento()))
                                .orElse(null);

                ProfissionalSaudeEntity entity = ProfissionalSaudeEntity.builder()
                                .id(model.getId())
                                .areaDaSaude(model.getAreaDaSaude())
                                .telefone(model.getTelefone())
                                .docProfissional(model.getDocProfissional())
                                .email(model.getEmail())
                                .nome(model.getNome())
                                .rg(model.getRg())
                                .endereco(enderecoEntity)
                                .build();

                if (model.getDisponibilidades() != null) {
                        model.getDisponibilidades().forEach(d -> {
                                DisponibilidadeEntity dispEntity = new DisponibilidadeEntity(
                                                d.getDiaSemana(),
                                                d.getTurno(),
                                                entity);
                                entity.addDisponibilidade(dispEntity);
                        });
                }

                return entity;
        }

        public static ProfissionalSaude toModel(ProfissionalSaudeEntity entity) {
                Endereco endereco = Optional.ofNullable(entity.getEndereco())
                                .map(endEntity -> new Endereco(
                                                endEntity.getEstado(),
                                                endEntity.getCidade(),
                                                endEntity.getBairro(),
                                                endEntity.getRua(),
                                                endEntity.getNumero(),
                                                endEntity.getCep(),
                                                endEntity.getComplemento()))
                                .orElse(null);

                List<Disponibilidade> disponibilidades = entity.getDisponibilidades() != null
                                ? entity.getDisponibilidades().stream()
                                                .map(d -> {
                                                        Disponibilidade disp = new Disponibilidade(d.getDiaSemana(),
                                                                        d.getTurno());
                                                        return disp;
                                                }).collect(Collectors.toList())
                                : new ArrayList<>();

                ProfissionalSaude profissional = new ProfissionalSaude(
                                entity.getId(),
                                entity.getAreaDaSaude(),
                                entity.getTelefone(),
                                entity.getDocProfissional(),
                                entity.getEmail(),
                                entity.getNome(),
                                entity.getRg(),
                                endereco,
                                disponibilidades);

                profissional.getDisponibilidades().forEach(d -> d.setProfissional(profissional));

                return profissional;
        }

        public static ProfissionalSaude toDomain(ProfissionalSaudeCreateDTO dto) {
                Endereco endereco = Optional.ofNullable(dto.getEndereco())
                                .map(endDto -> new Endereco(
                                                endDto.getEstado(),
                                                endDto.getCidade(),
                                                endDto.getBairro(),
                                                endDto.getRua(),
                                                endDto.getNumero(),
                                                endDto.getCep(),
                                                endDto.getComplemento()))
                                .orElse(null);

                List<Disponibilidade> disponibilidades = dto.getDisponibilidades() != null
                                ? dto.getDisponibilidades().stream()
                                                .map(d -> new Disponibilidade(d.getDia(), d.getTurno()))
                                                .collect(Collectors.toList())
                                : new ArrayList<>();

                ProfissionalSaude profissional = new ProfissionalSaude(
                                dto.getAreaDaSaude(),
                                dto.getTelefone(),
                                dto.getDocProfissional(),
                                dto.getEmail(),
                                dto.getNome(),
                                dto.getRg(),
                                endereco,
                                disponibilidades);

                profissional.getDisponibilidades().forEach(d -> d.setProfissional(profissional));

                return profissional;
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

                Optional.ofNullable(model.getEndereco())
                                .ifPresent(enderecoModel -> {
                                        Endereco enderecoDto = new Endereco();
                                        enderecoDto.setEstado(enderecoModel.getEstado());
                                        enderecoDto.setCidade(enderecoModel.getCidade());
                                        enderecoDto.setBairro(enderecoModel.getBairro());
                                        enderecoDto.setRua(enderecoModel.getRua());
                                        enderecoDto.setNumero(enderecoModel.getNumero());
                                        enderecoDto.setCep(enderecoModel.getCep());
                                        enderecoDto.setComplemento(enderecoModel.getComplemento());
                                        dto.setEndereco(enderecoDto);
                                });

                if (model.getDisponibilidades() != null) {
                        List<DisponibilidadeDTO> disponibilidades = model.getDisponibilidades()
                                        .stream()
                                        .map(d -> new DisponibilidadeDTO(d.getDiaSemana(), d.getTurno()))
                                        .collect(Collectors.toList());
                        dto.setDisponibilidades(disponibilidades);
                }

                return dto;
        }
}
