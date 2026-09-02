package br.com.geradoracordes.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.geradoracordes.dominio.Acidente;
import br.com.geradoracordes.dominio.ConfiguracaoAcorde;
import br.com.geradoracordes.dominio.ConfiguracaoAcorde.Componente;
import br.com.geradoracordes.dominio.ConfiguracaoAcorde.PosicaoBaixo;
import br.com.geradoracordes.dominio.GeradorAcorde;
import br.com.geradoracordes.dominio.GeradorAcordePersonalizado;
import br.com.geradoracordes.dominio.Nota;
import br.com.geradoracordes.dominio.NotaComOitava;
import br.com.geradoracordes.dominio.TipoAcorde;

@RestController
@RequestMapping("/api/acordes")
public class AcordeController {
    @GetMapping
    public AcordeResponse gerarAcordePersonalizado(
        @RequestParam String tonica,
        @RequestParam(defaultValue = "maior") String terca,
        @RequestParam(defaultValue = "justa") String quinta,
        @RequestParam(defaultValue = "omitida") String sexta,
        @RequestParam(defaultValue = "omitida") String setima,
        @RequestParam(defaultValue = "omitida") String nona,
        @RequestParam(defaultValue = "omitida") String decimaPrimeira,
        @RequestParam(defaultValue = "fundamental") String baixo
    ) {
        Nota notaTonica = converterTonica(tonica);
        ConfiguracaoAcorde configuracao;
        try {
            configuracao = new ConfiguracaoAcorde(
                converterTerca(terca), converterQuinta(quinta), converterSexta(sexta),
                converterSetima(setima), converterNona(nona),
                converterDecimaPrimeira(decimaPrimeira), converterBaixo(baixo)
            );
            GeradorAcordePersonalizado.AcordeGerado acorde =
                new GeradorAcordePersonalizado().gerar(notaTonica, 4, configuracao);
            return new AcordeResponse(
                notaTonica.toString(), "personalizado", acorde.cifra(),
                acorde.notas().stream().map(this::criarNotaResponse).toList()
            );
        } catch (IllegalArgumentException erro) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, erro.getMessage());
        }
    }

    @GetMapping("/tipos")
    public List<TipoAcordeResponse> listarTipos() {
        return Arrays.stream(TipoAcorde.values())
            .map(tipo -> new TipoAcordeResponse(
                tipo.getDefinicao().id(), tipo.getDefinicao().nome()
            ))
            .toList();
    }

    @GetMapping("/{tipo}")
    public AcordeResponse gerarAcorde(
        @PathVariable String tipo,
        @RequestParam String tonica
    ) {
        TipoAcorde tipoAcorde = TipoAcorde.buscar(tipo).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de acorde inválido")
        );
        Nota notaTonica = converterTonica(tonica);
        GeradorAcorde gerador = new GeradorAcorde(tipoAcorde.getDefinicao());
        List<NotaResponse> notas = gerador.gerar(notaTonica, 4).stream()
            .map(this::criarNotaResponse)
            .toList();

        return new AcordeResponse(
            notaTonica.toString(), tipoAcorde.getDefinicao().id(),
            gerador.formarCifra(notaTonica), notas
        );
    }

    private NotaResponse criarNotaResponse(NotaComOitava nota) {
        return new NotaResponse(
            nota.nota().toString(), nota.oitava(),
            nota.getNumeroMidi(), nota.getFrequencia()
        );
    }

    private Componente converterTerca(String valor) {
        return switch (valor) {
            case "omitida" -> Componente.OMITIDA;
            case "menor" -> Componente.TERCA_MENOR;
            case "maior" -> Componente.TERCA_MAIOR;
            case "sus2" -> Componente.SUS2;
            case "sus4" -> Componente.SUS4;
            default -> throw new IllegalArgumentException("Terça inválida");
        };
    }

    private Componente converterQuinta(String valor) {
        return switch (valor) {
            case "omitida" -> Componente.OMITIDA;
            case "diminuta" -> Componente.QUINTA_DIMINUTA;
            case "justa" -> Componente.QUINTA_JUSTA;
            case "aumentada" -> Componente.QUINTA_AUMENTADA;
            default -> throw new IllegalArgumentException("Quinta inválida");
        };
    }

    private Componente converterSexta(String valor) {
        return switch (valor) {
            case "omitida" -> Componente.OMITIDA;
            case "menor" -> Componente.SEXTA_MENOR;
            case "maior" -> Componente.SEXTA_MAIOR;
            case "b13" -> Componente.DECIMA_TERCEIRA_MENOR;
            case "13" -> Componente.DECIMA_TERCEIRA_MAIOR;
            default -> throw new IllegalArgumentException("Sexta ou décima terceira inválida");
        };
    }

    private Componente converterSetima(String valor) {
        return switch (valor) {
            case "omitida" -> Componente.OMITIDA;
            case "diminuta" -> Componente.SETIMA_DIMINUTA;
            case "menor" -> Componente.SETIMA_MENOR;
            case "maior" -> Componente.SETIMA_MAIOR;
            default -> throw new IllegalArgumentException("Sétima inválida");
        };
    }

    private Componente converterNona(String valor) {
        return switch (valor) {
            case "omitida" -> Componente.OMITIDA;
            case "menor" -> Componente.NONA_MENOR;
            case "maior" -> Componente.NONA_MAIOR;
            case "aumentada" -> Componente.NONA_AUMENTADA;
            default -> throw new IllegalArgumentException("Nona inválida");
        };
    }

    private Componente converterDecimaPrimeira(String valor) {
        return switch (valor) {
            case "omitida" -> Componente.OMITIDA;
            case "justa" -> Componente.DECIMA_PRIMEIRA_JUSTA;
            case "aumentada" -> Componente.DECIMA_PRIMEIRA_AUMENTADA;
            default -> throw new IllegalArgumentException("Décima primeira inválida");
        };
    }

    private PosicaoBaixo converterBaixo(String valor) {
        return switch (valor) {
            case "fundamental" -> PosicaoBaixo.FUNDAMENTAL;
            case "terca" -> PosicaoBaixo.TERCA;
            case "quinta" -> PosicaoBaixo.QUINTA;
            case "sexta" -> PosicaoBaixo.SEXTA;
            case "setima" -> PosicaoBaixo.SETIMA;
            case "nona" -> PosicaoBaixo.NONA;
            case "decima-primeira" -> PosicaoBaixo.DECIMA_PRIMEIRA;
            default -> throw new IllegalArgumentException("Inversão inválida");
        };
    }

    private Nota converterTonica(String texto) {
        if (texto == null || texto.isBlank()) {
            throw entradaInvalida();
        }
        String valor = texto.trim();
        char letra = Character.toUpperCase(valor.charAt(0));
        Acidente acidente = switch (valor.substring(1)) {
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
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tônica inválida");
    }

    public record TipoAcordeResponse(String id, String nome) {
    }

    public record AcordeResponse(
        String tonica, String tipo, String cifra, List<NotaResponse> notas
    ) {
    }

    public record NotaResponse(String nome, int oitava, int midi, double frequencia) {
    }
}
