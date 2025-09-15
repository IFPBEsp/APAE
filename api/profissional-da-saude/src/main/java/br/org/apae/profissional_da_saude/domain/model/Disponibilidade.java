package br.org.apae.profissional_da_saude.domain.model;

import br.org.apae.profissional_da_saude.domain.model.enums.DiaSemana;
import br.org.apae.profissional_da_saude.domain.model.enums.Turno;

import java.util.UUID;

public class Disponibilidade {
  private UUID id;
  private DiaSemana diaSemana;
  private Turno turno;
  private ProfissionalSaude profissional;

  public Disponibilidade(UUID id, DiaSemana diaSemana, Turno turno) {
    this.id = id;
    this.diaSemana = diaSemana;
    this.turno = turno;
  }

  public Disponibilidade(DiaSemana diaSemana, Turno turno) {
    this.diaSemana = diaSemana;
    this.turno = turno;
  }

  public UUID getId() {
    return id;
  }

  public DiaSemana getDiaSemana() {
    return diaSemana;
  }

  public Turno getTurno() {
    return turno;
  }

  public ProfissionalSaude getProfissional() {
    return profissional;
  }

  public void setProfissional(ProfissionalSaude profissional) {
    this.profissional = profissional;
  }
}
