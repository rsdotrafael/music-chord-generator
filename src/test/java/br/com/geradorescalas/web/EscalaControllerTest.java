package br.com.geradorescalas.web;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EscalaControllerTest {

    private final EscalaController controller = new EscalaController();

    @Test
    void deveGerarFaSustenidoMaior() {
        EscalaController.EscalaResponse resposta =
            controller.gerarEscalaMaior("F#");

        assertEquals("F#", resposta.tonica());
        assertEquals(
            "F# - G# - A# - B - C# - D# - E# - F#",
            String.join(" - ", resposta.notas())
        );
    }

    @Test
    void deveGerarSolBemolMaior() {
        EscalaController.EscalaResponse resposta =
            controller.gerarEscalaMaior("Gb");

        assertEquals(
            "Gb - Ab - Bb - Cb - Db - Eb - F - Gb",
            String.join(" - ", resposta.notas())
        );
    }

    @Test
    void deveRejeitarTonicaInvalida() {
        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> controller.gerarEscalaMaior("X")
        );

        assertEquals(400, erro.getStatusCode().value());
    }
}
