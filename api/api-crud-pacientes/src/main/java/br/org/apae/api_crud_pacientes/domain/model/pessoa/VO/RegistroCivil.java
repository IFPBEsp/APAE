package br.org.apae.api_crud_pacientes.domain.model.pessoa.VO;

public class RegistroCivil {
    private String numRegistroNasc;
    private String fls;
    private String livro;
    private String cartorio;

    public RegistroCivil(String numRegistroNasc, String fls, String livro, String cartorio) {
        this.numRegistroNasc = numRegistroNasc;
        this.fls = fls;
        this.livro = livro;
        this.cartorio = cartorio;
        validate();
    }

    public String getNumRegistroNasc() {
        return numRegistroNasc;
    }

    public String getFls() {
        return fls;
    }

    public String getLivro() {
        return livro;
    }

    public String getCartorio() {
        return cartorio;
    }

    protected void validate() {
        if (numRegistroNasc == null || numRegistroNasc.isBlank()) {
            throw new IllegalArgumentException("Número de registro de nascimento inválido");
        }
        if (cartorio == null || cartorio.isBlank()) {
            throw new IllegalArgumentException("Cartório é obrigatório");
        }
    }
}
