package br.com.geradorescalas.dominio;

import java.util.Objects;

public final class Nota {

    private final char letra;
    private final Acidente acidente;

    public Nota(char letra, Acidente acidente) {
        char letraMaiuscula = Character.toUpperCase(letra);

        if (letraMaiuscula < 'A' || letraMaiuscula > 'G') {
            throw new IllegalArgumentException("A letra da nota deve estar entre A e G");
        }

        this.letra = letraMaiuscula;
        this.acidente = Objects.requireNonNull(acidente, "O acidente não pode ser nulo");
    }

    public char getLetra() {
        return letra;
    }

    public Acidente getAcidente() {
        return acidente;
    }

    @Override
    public String toString() {
        return letra + acidente.getSimbolo();
    }
}
