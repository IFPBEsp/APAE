package br.org.apae.api.profissional.da.saude.infra.mapper;

import br.org.apae.api.common.entity.EnderecoEntity;
import br.org.apae.api.common.model.Endereco;
import br.org.apae.api.profissional.da.saude.domain.model.ProfissionalSaude;
import br.org.apae.api.profissional.da.saude.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.api.profissional.da.saude.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.api.profissional.da.saude.infra.entity.ProfissionalSaudeEntity;
import org.springframework.stereotype.Component;

@Component
public final class ProfissionalSaudeMapper {

    public ProfissionalSaudeEntity toEntity(ProfissionalSaude model) {

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

    public ProfissionalSaude toModel(ProfissionalSaudeEntity entity) {

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

    public ProfissionalSaude toDomain(ProfissionalSaudeCreateDTO dto) {

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

    public  ProfissionalSaudeResponseDTO toResponseDTO(ProfissionalSaude model) {
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