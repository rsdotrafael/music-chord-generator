package br.com.geradorescalas.dominio;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum TipoEscala {
    MAIOR("maior", "Maior", heptatonica(0, 2, 4, 5, 7, 9, 11, 12)),
    MENOR_NATURAL("menor-natural", "Menor natural", heptatonica(0, 2, 3, 5, 7, 8, 10, 12)),
    MENOR_MELODICA("menor-melodica", "Menor melódica", heptatonica(0, 2, 3, 5, 7, 9, 11, 12)),
    MENOR_HARMONICA("menor-harmonica", "Menor harmônica", heptatonica(0, 2, 3, 5, 7, 8, 11, 12)),
    PENTATONICA_MAIOR(
        "pentatonica-maior", "Pentatônica maior",
        partes(0, 2, 4, 7, 9, 12).comDeslocamentos(0, 1, 2, 4, 5, 7)
    ),
    PENTATONICA_MENOR(
        "pentatonica-menor", "Pentatônica menor",
        partes(0, 3, 5, 7, 10, 12).comDeslocamentos(0, 2, 3, 4, 6, 7)
    ),
    TONS_INTEIROS(
        "tons-inteiros", "Tons inteiros",
        partes(0, 2, 4, 6, 8, 10, 12).comDeslocamentos(0, 1, 2, 3, 4, 5, 7)
    );

    private final DefinicaoEscala definicao;

    TipoEscala(String id, String nome, PartesDefinicao partes) {
        definicao = new DefinicaoEscala(
            id, nome, partes.intervalos(), partes.deslocamentos()
        );
    }

    public DefinicaoEscala getDefinicao() {
        return definicao;
    }

    public static Optional<TipoEscala> buscar(String id) {
        return Arrays.stream(values())
            .filter(tipo -> tipo.definicao.id().equals(id))
            .findFirst();
    }

    private static PartesDefinicao heptatonica(Integer... intervalos) {
        return new PartesDefinicao(
            List.of(intervalos),
            List.of(0, 1, 2, 3, 4, 5, 6, 7)
        );
    }

    private static ConstrutorDefinicao partes(Integer... intervalos) {
        return new ConstrutorDefinicao(List.of(intervalos));
    }

    private record ConstrutorDefinicao(List<Integer> intervalos) {
        private PartesDefinicao comDeslocamentos(Integer... deslocamentos) {
            return new PartesDefinicao(intervalos, List.of(deslocamentos));
        }
    }

    private record PartesDefinicao(
        List<Integer> intervalos,
        List<Integer> deslocamentos
    ) {
    }
}
