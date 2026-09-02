package br.com.geradoracordes.dominio;

import java.util.List;

public record ConfiguracaoAcorde(
    Componente terca,
    Componente quinta,
    Componente sexta,
    Componente setima,
    Componente nona,
    Componente decimaPrimeira,
    PosicaoBaixo baixo
) {
    public ConfiguracaoAcorde {
        if (terca == null || quinta == null || sexta == null || setima == null
            || nona == null || decimaPrimeira == null || baixo == null) {
            throw new IllegalArgumentException("Todos os componentes do acorde devem ser informados");
        }
    }

    public List<Componente> componentes() {
        return List.of(terca, quinta, sexta, setima, nona, decimaPrimeira);
    }

    public enum Componente {
        OMITIDA("", -1, -1, PosicaoBaixo.FUNDAMENTAL),
        TERCA_MENOR("m", 3, 2, PosicaoBaixo.TERCA),
        TERCA_MAIOR("", 4, 2, PosicaoBaixo.TERCA),
        SUS2("sus2", 2, 1, PosicaoBaixo.TERCA),
        SUS4("sus4", 5, 3, PosicaoBaixo.TERCA),
        QUINTA_DIMINUTA("b5", 6, 4, PosicaoBaixo.QUINTA),
        QUINTA_JUSTA("", 7, 4, PosicaoBaixo.QUINTA),
        QUINTA_AUMENTADA("#5", 8, 4, PosicaoBaixo.QUINTA),
        SEXTA_MENOR("b6", 8, 5, PosicaoBaixo.SEXTA),
        SEXTA_MAIOR("6", 9, 5, PosicaoBaixo.SEXTA),
        DECIMA_TERCEIRA_MENOR("b13", 20, 12, PosicaoBaixo.SEXTA),
        DECIMA_TERCEIRA_MAIOR("13", 21, 12, PosicaoBaixo.SEXTA),
        SETIMA_DIMINUTA("dim7", 9, 6, PosicaoBaixo.SETIMA),
        SETIMA_MENOR("7", 10, 6, PosicaoBaixo.SETIMA),
        SETIMA_MAIOR("maj7", 11, 6, PosicaoBaixo.SETIMA),
        NONA_MENOR("b9", 13, 8, PosicaoBaixo.NONA),
        NONA_MAIOR("9", 14, 8, PosicaoBaixo.NONA),
        NONA_AUMENTADA("#9", 15, 8, PosicaoBaixo.NONA),
        DECIMA_PRIMEIRA_JUSTA("11", 17, 10, PosicaoBaixo.DECIMA_PRIMEIRA),
        DECIMA_PRIMEIRA_AUMENTADA("#11", 18, 10, PosicaoBaixo.DECIMA_PRIMEIRA);

        private final String simbolo;
        private final int intervalo;
        private final int grau;
        private final PosicaoBaixo posicao;

        Componente(String simbolo, int intervalo, int grau, PosicaoBaixo posicao) {
            this.simbolo = simbolo;
            this.intervalo = intervalo;
            this.grau = grau;
            this.posicao = posicao;
        }

        public String simbolo() { return simbolo; }
        public int intervalo() { return intervalo; }
        public int grau() { return grau; }
        public PosicaoBaixo posicao() { return posicao; }
        public boolean incluida() { return this != OMITIDA; }
    }

    public enum PosicaoBaixo {
        FUNDAMENTAL, TERCA, QUINTA, SEXTA, SETIMA, NONA, DECIMA_PRIMEIRA
    }
}
