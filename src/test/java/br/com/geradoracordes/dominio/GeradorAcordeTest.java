package br.com.geradoracordes.dominio;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeradorAcordeTest {
    @Test
    void deveGerarDoMaior() {
        GeradorAcorde gerador = new GeradorAcorde(TipoAcorde.MAIOR.getDefinicao());

        assertEquals(
            List.of("C", "E", "G"),
            gerador.gerar(new Nota('C', Acidente.NATURAL), 4).stream()
                .map(nota -> nota.nota().toString())
                .toList()
        );
    }

    @Test
    void devePreservarGrafiaMusical() {
        GeradorAcorde gerador = new GeradorAcorde(TipoAcorde.MAIOR_SETIMA.getDefinicao());

        assertEquals(
            List.of("Gb", "Bb", "Db", "F"),
            gerador.gerar(new Nota('G', Acidente.BEMOL), 4).stream()
                .map(nota -> nota.nota().toString())
                .toList()
        );
        assertEquals("Gbmaj7", gerador.formarCifra(new Nota('G', Acidente.BEMOL)));
    }

    @Test
    void deveCalcularMidiEFrequenciaComComponentesExistentes() {
        NotaComOitava primeira = new GeradorAcorde(TipoAcorde.MENOR.getDefinicao())
            .gerar(new Nota('A', Acidente.NATURAL), 4)
            .getFirst();

        assertEquals(69, primeira.getNumeroMidi());
        assertEquals(440.0, primeira.getFrequencia(), 0.001);
    }

    @Test
    void deveMontarAcordePersonalizadoEAplicarInversao() {
        ConfiguracaoAcorde configuracao = new ConfiguracaoAcorde(
            ConfiguracaoAcorde.Componente.TERCA_MENOR,
            ConfiguracaoAcorde.Componente.QUINTA_DIMINUTA,
            ConfiguracaoAcorde.Componente.OMITIDA,
            ConfiguracaoAcorde.Componente.SETIMA_MENOR,
            ConfiguracaoAcorde.Componente.NONA_MAIOR,
            ConfiguracaoAcorde.Componente.OMITIDA,
            ConfiguracaoAcorde.PosicaoBaixo.TERCA
        );

        GeradorAcordePersonalizado.AcordeGerado acorde =
            new GeradorAcordePersonalizado().gerar(new Nota('C', Acidente.NATURAL), 4, configuracao);

        assertEquals("Cm(b5)(7)(9)/Eb", acorde.cifra());
        assertEquals(
            List.of("Eb", "Gb", "Bb", "D", "C"),
            acorde.notas().stream().map(nota -> nota.nota().toString()).toList()
        );
    }

    @Test
    void deveRejeitarInversaoComNotaOmitida() {
        ConfiguracaoAcorde configuracao = new ConfiguracaoAcorde(
            ConfiguracaoAcorde.Componente.TERCA_MAIOR,
            ConfiguracaoAcorde.Componente.QUINTA_JUSTA,
            ConfiguracaoAcorde.Componente.OMITIDA,
            ConfiguracaoAcorde.Componente.OMITIDA,
            ConfiguracaoAcorde.Componente.OMITIDA,
            ConfiguracaoAcorde.Componente.OMITIDA,
            ConfiguracaoAcorde.PosicaoBaixo.SETIMA
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> new GeradorAcordePersonalizado().gerar(
                new Nota('C', Acidente.NATURAL), 4, configuracao
            )
        );
    }
}
