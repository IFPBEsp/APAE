package br.org.apae.profissional_da_saude.domain.exception;

public class ValidacaoNegocioException extends RuntimeException {
    public ValidacaoNegocioException(String menssagem){
        super(menssagem);
    }
}
