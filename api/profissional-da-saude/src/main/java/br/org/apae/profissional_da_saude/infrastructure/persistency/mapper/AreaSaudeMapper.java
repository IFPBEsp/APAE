package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.api.dto.AreaSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.AreaSaudeResponseDTO;
import br.org.apae.profissional_da_saude.domain.model.AreaSaude;
import br.org.apae.profissional_da_saude.infrastructure.entity.AreaSaudeEntity;

public final class AreaSaudeMapper {

    private AreaSaudeMapper() {
    }

    public static AreaSaudeEntity toEntity(AreaSaude model){
        AreaSaudeEntity.AreaSaudeEntityBuilder builder = AreaSaudeEntity.builder()
                .area(model.getArea());

        if (model.getId() != null) {
            builder.id(model.getId());
        }

        return builder.build();
    }
    public static AreaSaude toModel(AreaSaudeEntity entity){
        return new AreaSaude(
                entity.getId(),
                entity.getArea()
        );
    }

    public static AreaSaude toDomain(AreaSaudeCreateDTO dto){
        return new AreaSaude(
                dto.getArea()
        );
    }
    public static AreaSaudeResponseDTO toResponseDTO(AreaSaude model){
        AreaSaudeResponseDTO area = new AreaSaudeResponseDTO();
        area.setId(model.getId());
        area.setArea(model.getArea());
        return area;
    }
}
