package br.com.geradorescalas.dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotaTest {

    @Test
    void deveRepresentarNotaNatural() {
        Nota nota = new Nota('F', Acidente.NATURAL);

        assertEquals("F", nota.toString());
    }

    @Test
    void deveRepresentarNotaComSustenido() {
        Nota nota = new Nota('F', Acidente.SUSTENIDO);

        assertEquals("F#", nota.toString());
    }

    @Test
    void deveRepresentarNotaComBemol() {
        Nota nota = new Nota('B', Acidente.BEMOL);

        assertEquals("Bb", nota.toString());
    }

    @Test
    void deveConverterLetraMinusculaParaMaiuscula() {
        Nota nota = new Nota('f', Acidente.NATURAL);

        assertEquals('F', nota.getLetra());
        assertEquals("F", nota.toString());
    }

    @Test
    void deveRejeitarLetraInvalida() {
        IllegalArgumentException erro = assertThrows(
            IllegalArgumentException.class,
            () -> new Nota('X', Acidente.NATURAL)
        );

        assertEquals(
            "A letra da nota deve estar entre A e G",
            erro.getMessage()
        );
    }

    @Test
    void deveRejeitarAcidenteNulo() {
        NullPointerException erro = assertThrows(
            NullPointerException.class,
            () -> new Nota('F', null)
        );

        assertEquals(
            "O acidente não pode ser nulo",
            erro.getMessage()
        );
    }
}