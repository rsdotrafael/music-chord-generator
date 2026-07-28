package br.com.geradorescalas.dominio;

public enum Acidente {
    BEMOL("b", -1),
    NATURAL("", 0),
    SUSTENIDO("#", 1);

    private final String simbolo;
    private final int alteracao;

    Acidente(String simbolo, int alteracao) {
        this.simbolo = simbolo;
        this.alteracao = alteracao;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public int getAlteracao() {
        return alteracao;
    }
}
