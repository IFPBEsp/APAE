package br.org.apae.profissional_da_saude.application.service.exceptions;

public class ProfissionalNaoEncontradoException extends RuntimeException {
  public ProfissionalNaoEncontradoException() {
    super("Profissional não encontrado");
    }
}
