package br.com.geradorescalas.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class GeradorEscalaMaior {

    private static final int[] INTERVALOS = {0, 2, 4, 5, 7, 9, 11, 12};
    private static final String LETRAS = "ABCDEFG";

    public List<Nota> gerar(Nota tonica) {
        Objects.requireNonNull(tonica, "A tônica não pode ser nula");

        List<Nota> escala = new ArrayList<>();

        for (int grau = 0; grau < INTERVALOS.length; grau++) {
            int alturaEsperada = Math.floorMod(
                tonica.getAltura() + INTERVALOS[grau],
                12
            );

            char letra = calcularLetra(tonica.getLetra(), grau);
            Acidente acidente = escolherAcidente(letra, alturaEsperada);

            escala.add(new Nota(letra, acidente));
        }

        return List.copyOf(escala);
    }

    public List<NotaComOitava> gerarComOitavas(Nota tonica, int oitavaInicial) {
        List<Nota> notas = gerar(tonica);
        List<NotaComOitava> escala = new ArrayList<>();
        int oitava = oitavaInicial;

        for (int grau = 0; grau < notas.size(); grau++) {
            if (grau > 0 && notas.get(grau).getLetra() == 'C') {
                oitava++;
            }

            escala.add(new NotaComOitava(notas.get(grau), oitava));
        }

        return List.copyOf(escala);
    }

    private char calcularLetra(char letraDaTonica, int grau) {
        int indiceInicial = LETRAS.indexOf(letraDaTonica);
        int indiceDoGrau = (indiceInicial + grau) % LETRAS.length();

        return LETRAS.charAt(indiceDoGrau);
    }

    private Acidente escolherAcidente(char letra, int alturaEsperada) {
        Nota notaNatural = new Nota(letra, Acidente.NATURAL);
        int diferenca = Math.floorMod(
            alturaEsperada - notaNatural.getAltura(),
            12
        );

        return switch (diferenca) {
            case 0 -> Acidente.NATURAL;
            case 1 -> Acidente.SUSTENIDO;
            case 2 -> Acidente.SUSTENIDO_DUPLO;
            case 10 -> Acidente.BEMOL_DUPLO;
            case 11 -> Acidente.BEMOL;
            default -> throw new IllegalArgumentException(
                "A escala exige um acidente ainda não suportado"
            );
        };
    }
}
