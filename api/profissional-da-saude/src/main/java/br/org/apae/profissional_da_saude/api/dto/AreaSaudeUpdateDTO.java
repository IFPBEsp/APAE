package br.org.apae.profissional_da_saude.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AreaSaudeUpdateDTO {
    @NotBlank
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[A-Za-zÀ-ÿ ]+$",
            message = "A área de saúde deve conter apenas letras e espaços")
    private String area;
}
