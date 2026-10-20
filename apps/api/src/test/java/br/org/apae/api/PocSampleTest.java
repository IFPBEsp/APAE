package br.org.apae.api;

public class PocSampleTest {

    // 1. Função para dar "Olá" com nome em maiúsculo
    public String saudar(String nome) {
        // Se alguém passar 'nome' vazio ou nulo (null), o sistema vai travar aqui!
        return "Olá, " + nome.toUpperCase();
    }

    // 2. Função para saber se a pessoa pode dirigir
    public boolean podeDirigir(int idade) {
        // O que acontece se alguém passar uma idade negativa, tipo -5?
        return idade >= 18;
    }
}