package br.com.geradorescalas.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.geradorescalas.dominio.Acidente;
import br.com.geradorescalas.dominio.GeradorEscalaMaior;
import br.com.geradorescalas.dominio.Nota;
import br.com.geradorescalas.dominio.NotaComOitava;

@RestController
@RequestMapping("/api/escalas")
public class EscalaController {

    private final GeradorEscalaMaior gerador = new GeradorEscalaMaior();

    @GetMapping("/maior")
    public EscalaResponse gerarEscalaMaior(@RequestParam String tonica) {
        Nota notaTonica = converterTonica(tonica);
        List<NotaResponse> notas = gerador.gerarComOitavas(notaTonica, 4).stream()
            .map(this::criarNotaResponse)
            .toList();

        return new EscalaResponse(notaTonica.toString(), notas);
    }

    private NotaResponse criarNotaResponse(NotaComOitava nota) {
        return new NotaResponse(
            nota.nota().toString(),
            nota.oitava(),
            nota.getNumeroMidi(),
            nota.getFrequencia()
        );
    }

    private Nota converterTonica(String texto) {
        if (texto == null || texto.isBlank()) {
            throw entradaInvalida();
        }

        String valor = texto.trim();
        char letra = Character.toUpperCase(valor.charAt(0));
        String simbolo = valor.substring(1);
        Acidente acidente = switch (simbolo) {
            case "" -> Acidente.NATURAL;
            case "#", "♯" -> Acidente.SUSTENIDO;
            case "b", "♭" -> Acidente.BEMOL;
            default -> throw entradaInvalida();
        };

        try {
            return new Nota(letra, acidente);
        } catch (IllegalArgumentException erro) {
            throw entradaInvalida();
        }
    }

    private ResponseStatusException entradaInvalida() {
        return new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Tônica inválida"
        );
    }

    public record EscalaResponse(String tonica, List<NotaResponse> notas) {
    }

    public record NotaResponse(
        String nome,
        int oitava,
        int midi,
        double frequencia
    ) {
    }
}
