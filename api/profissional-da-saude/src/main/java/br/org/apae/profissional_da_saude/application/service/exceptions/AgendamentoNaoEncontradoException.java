package br.org.apae.profissional_da_saude.application.service.exceptions;

public class AgendamentoNaoEncontradoException extends RuntimeException{

    public AgendamentoNaoEncontradoException() {
        super("Agendamento não encontrado");
    }
}
