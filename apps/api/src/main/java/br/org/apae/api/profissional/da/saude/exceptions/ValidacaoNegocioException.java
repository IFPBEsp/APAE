package br.org.apae.api.profissional.da.saude.exceptions;

public class ValidacaoNegocioException extends RuntimeException {
    public ValidacaoNegocioException(String menssagem){
        super(menssagem);
    }
}