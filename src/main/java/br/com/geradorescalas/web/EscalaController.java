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

@RestController
@RequestMapping("/api/escalas")
public class EscalaController {

    private final GeradorEscalaMaior gerador = new GeradorEscalaMaior();

    @GetMapping("/maior")
    public EscalaResponse gerarEscalaMaior(@RequestParam String tonica) {
        Nota notaTonica = converterTonica(tonica);
        List<String> notas = gerador.gerar(notaTonica).stream()
            .map(Nota::toString)
            .toList();

        return new EscalaResponse(notaTonica.toString(), notas);
    }

    private Nota converterTonica(String texto) {
        if (texto == null || texto.isBlank()) {
            throw entradaInvalida();
        }

        String valor = texto.trim();
        if (valor.length() > 2) {
            throw entradaInvalida();
        }

        char letra = Character.toUpperCase(valor.charAt(0));
        Acidente acidente = Acidente.NATURAL;

        if (valor.length() == 2) {
            acidente = switch (valor.charAt(1)) {
                case '#', '♯' -> Acidente.SUSTENIDO;
                case 'b', '♭' -> Acidente.BEMOL;
                default -> throw entradaInvalida();
            };
        }

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

    public record EscalaResponse(String tonica, List<String> notas) {
    }
}
