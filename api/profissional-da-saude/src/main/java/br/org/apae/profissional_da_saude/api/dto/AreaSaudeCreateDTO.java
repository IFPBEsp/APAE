package br.org.apae.profissional_da_saude.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AreaSaudeCreateDTO {

    @NotBlank
    @Size(min = 3, max = 100)
    private String area;
}
