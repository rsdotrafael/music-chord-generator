package br.com.geradoracordes.web;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AcordeControllerTest {
    private final AcordeController controller = new AcordeController();

    @Test
    void deveListarTiposDisponiveis() {
        assertEquals(13, controller.listarTipos().size());
        assertEquals("maior", controller.listarTipos().getFirst().id());
    }

    @Test
    void deveGerarAcordeMenorComSetima() {
        AcordeController.AcordeResponse resposta = controller.gerarAcorde(
            "menor-setima", "Bb"
        );

        assertEquals("Bbm7", resposta.cifra());
        assertEquals("Bb", resposta.tonica());
        assertEquals(
            java.util.List.of("Bb", "Db", "F", "Ab"),
            resposta.notas().stream().map(AcordeController.NotaResponse::nome).toList()
        );
    }

    @Test
    void deveRejeitarTipoInexistente() {
        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> controller.gerarAcorde("inexistente", "C")
        );
        assertEquals(404, erro.getStatusCode().value());
    }

    @Test
    void deveRejeitarTonicaInvalida() {
        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> controller.gerarAcorde("maior", "X")
        );
        assertEquals(400, erro.getStatusCode().value());
    }

    @Test
    void deveGerarAcordePersonalizado() {
        AcordeController.AcordeResponse resposta = controller.gerarAcordePersonalizado(
            "C", "maior", "aumentada", "omitida", "maior",
            "aumentada", "aumentada", "fundamental"
        );

        assertEquals("C(#5)(maj7)(#9)(#11)", resposta.cifra());
        assertEquals(
            java.util.List.of("C", "E", "G#", "B", "D#", "F#"),
            resposta.notas().stream().map(AcordeController.NotaResponse::nome).toList()
        );
    }

    @Test
    void deveRejeitarParametroDeComponenteInvalido() {
        ResponseStatusException erro = assertThrows(
            ResponseStatusException.class,
            () -> controller.gerarAcordePersonalizado(
                "C", "maior", "inexistente", "omitida", "omitida",
                "omitida", "omitida", "fundamental"
            )
        );
        assertEquals(400, erro.getStatusCode().value());
    }
}
