package br.com.geradorescalas.dominio;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeradorEscalaGenericaTest {

    @Test
    void deveGerarPentatonicaMaiorComGrafiaCorreta() {
        GeradorEscala gerador = new GeradorEscala(
            TipoEscala.PENTATONICA_MAIOR.getDefinicao()
        );

        assertEquals(
            List.of("C", "D", "E", "G", "A", "C"),
            gerador.gerar(new Nota('C', Acidente.NATURAL)).stream()
                .map(Nota::toString)
                .toList()
        );
    }

    @Test
    void deveCalcularOitavasPeloIntervaloAbsoluto() {
        GeradorEscala gerador = new GeradorEscala(
            new DefinicaoEscala(
                "duas-oitavas",
                "Duas oitavas",
                List.of(0, 12, 24),
                List.of(0, 7, 14)
            )
        );

        List<NotaComOitava> notas = gerador.gerarComOitavas(
            new Nota('B', Acidente.SUSTENIDO),
            4
        );

        assertEquals(List.of(72, 84, 96), notas.stream()
            .map(NotaComOitava::getNumeroMidi)
            .toList());
    }
}
