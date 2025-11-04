package br.org.apae.api.common.dto.Avaliability.Request;

import jakarta.validation.constraints.NotBlank;

public record AvaliabilityRequestCreateDTO(
  @NotBlank(message = "O dia não pode estar em branco")
  String day,
  
  @NotBlank(message = "O turno não pode estar em branco")
  String shift
) {
}

