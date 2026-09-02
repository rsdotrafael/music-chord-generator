package br.com.geradoracordes.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class GeradorAcorde {
    private static final String LETRAS = "ABCDEFG";
    private final DefinicaoAcorde definicao;

    public GeradorAcorde(DefinicaoAcorde definicao) {
        this.definicao = Objects.requireNonNull(definicao);
    }

    public String formarCifra(Nota tonica) {
        return tonica + definicao.sufixo();
    }

    public List<NotaComOitava> gerar(Nota tonica, int oitavaInicial) {
        Objects.requireNonNull(tonica, "A tônica não pode ser nula");
        List<NotaComOitava> notas = new ArrayList<>();
        int midiDaTonica = new NotaComOitava(tonica, oitavaInicial).getNumeroMidi();

        for (int indice = 0; indice < definicao.intervalos().size(); indice++) {
            int intervalo = definicao.intervalos().get(indice);
            int midiEsperado = midiDaTonica + intervalo;
            int alturaEsperada = Math.floorMod(midiEsperado, 12);
            char letra = calcularLetra(tonica.getLetra(), definicao.graus().get(indice));
            Nota nota = new Nota(letra, escolherAcidente(letra, alturaEsperada));
            int alturaEscrita = alturaNatural(letra) + nota.getAcidente().getAlteracao();
            int oitava = Math.floorDiv(midiEsperado - alturaEscrita, 12) - 1;
            notas.add(new NotaComOitava(nota, oitava));
        }
        return List.copyOf(notas);
    }

    private char calcularLetra(char tonica, int grau) {
        return LETRAS.charAt(Math.floorMod(LETRAS.indexOf(tonica) + grau, LETRAS.length()));
    }

    private int alturaNatural(char letra) {
        return switch (letra) {
            case 'C' -> 0;
            case 'D' -> 2;
            case 'E' -> 4;
            case 'F' -> 5;
            case 'G' -> 7;
            case 'A' -> 9;
            case 'B' -> 11;
            default -> throw new IllegalStateException("Letra inválida: " + letra);
        };
    }

    private Acidente escolherAcidente(char letra, int alturaEsperada) {
        int diferenca = Math.floorMod(alturaEsperada - alturaNatural(letra), 12);
        return switch (diferenca) {
            case 0 -> Acidente.NATURAL;
            case 1 -> Acidente.SUSTENIDO;
            case 2 -> Acidente.SUSTENIDO_DUPLO;
            case 10 -> Acidente.BEMOL_DUPLO;
            case 11 -> Acidente.BEMOL;
            default -> throw new IllegalArgumentException("O acorde exige um acidente não suportado");
        };
    }
}
