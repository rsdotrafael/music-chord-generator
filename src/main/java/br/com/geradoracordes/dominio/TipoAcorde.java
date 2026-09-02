package br.com.geradoracordes.dominio;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum TipoAcorde {
    MAIOR("maior", "Maior", "", notas(List.of(0, 4, 7), 0, 2, 4)),
    MENOR("menor", "Menor", "m", notas(List.of(0, 3, 7), 0, 2, 4)),
    DIMINUTO("diminuto", "Diminuto", "dim", notas(List.of(0, 3, 6), 0, 2, 4)),
    AUMENTADO("aumentado", "Aumentado", "aug", notas(List.of(0, 4, 8), 0, 2, 4)),
    SUS2("sus2", "Suspenso 2 (sus2)", "sus2", notas(List.of(0, 2, 7), 0, 1, 4)),
    SUS4("sus4", "Suspenso 4 (sus4)", "sus4", notas(List.of(0, 5, 7), 0, 3, 4)),
    MAIOR_SETIMA("maior-setima", "Maior com 7ª maior (maj7)", "maj7", notas(List.of(0, 4, 7, 11), 0, 2, 4, 6)),
    SETIMA("setima", "Dominante com 7ª (7)", "7", notas(List.of(0, 4, 7, 10), 0, 2, 4, 6)),
    MENOR_SETIMA("menor-setima", "Menor com 7ª (m7)", "m7", notas(List.of(0, 3, 7, 10), 0, 2, 4, 6)),
    MEIO_DIMINUTO("meio-diminuto", "Meio-diminuto (m7♭5)", "m7♭5", notas(List.of(0, 3, 6, 10), 0, 2, 4, 6)),
    DIMINUTO_SETIMA("diminuto-setima", "Diminuto com 7ª (dim7)", "dim7", notas(List.of(0, 3, 6, 9), 0, 2, 4, 6)),
    SEXTA("sexta", "Maior com 6ª (6)", "6", notas(List.of(0, 4, 7, 9), 0, 2, 4, 5)),
    MENOR_SEXTA("menor-sexta", "Menor com 6ª (m6)", "m6", notas(List.of(0, 3, 7, 9), 0, 2, 4, 5));

    private final DefinicaoAcorde definicao;

    TipoAcorde(String id, String nome, String sufixo, Partes partes) {
        definicao = new DefinicaoAcorde(id, nome, sufixo, partes.intervalos(), partes.graus());
    }

    public DefinicaoAcorde getDefinicao() {
        return definicao;
    }

    public static Optional<TipoAcorde> buscar(String id) {
        return Arrays.stream(values()).filter(tipo -> tipo.definicao.id().equals(id)).findFirst();
    }

    private static Partes notas(List<Integer> intervalos, Integer... graus) {
        return new Partes(intervalos, List.of(graus));
    }

    private record Partes(List<Integer> intervalos, List<Integer> graus) {
    }
}
