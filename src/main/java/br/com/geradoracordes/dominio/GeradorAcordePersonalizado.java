package br.com.geradoracordes.dominio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.com.geradoracordes.dominio.ConfiguracaoAcorde.Componente;
import br.com.geradoracordes.dominio.ConfiguracaoAcorde.PosicaoBaixo;

public final class GeradorAcordePersonalizado {
    public AcordeGerado gerar(Nota tonica, int oitava, ConfiguracaoAcorde configuracao) {
        List<Componente> componentes = configuracao.componentes().stream()
            .filter(Componente::incluida)
            .sorted(Comparator.comparingInt(Componente::intervalo))
            .toList();

        validarBaixo(configuracao.baixo(), componentes);

        List<Integer> intervalos = new ArrayList<>(List.of(0));
        List<Integer> graus = new ArrayList<>(List.of(0));
        componentes.forEach(componente -> {
            intervalos.add(componente.intervalo());
            graus.add(componente.grau());
        });

        GeradorAcorde gerador = new GeradorAcorde(new DefinicaoAcorde(
            "personalizado", "Personalizado", "", intervalos, graus
        ));
        List<NotaComOitava> notas = new ArrayList<>(gerador.gerar(tonica, oitava));
        aplicarInversao(notas, configuracao.baixo(), componentes);

        return new AcordeGerado(criarCifra(tonica, configuracao, notas.getFirst()), List.copyOf(notas));
    }

    private void validarBaixo(PosicaoBaixo baixo, List<Componente> componentes) {
        if (baixo != PosicaoBaixo.FUNDAMENTAL
            && componentes.stream().noneMatch(item -> item.posicao() == baixo)) {
            throw new IllegalArgumentException("A nota escolhida para o baixo foi omitida do acorde");
        }
    }

    private void aplicarInversao(
        List<NotaComOitava> notas, PosicaoBaixo baixo, List<Componente> componentes
    ) {
        if (baixo == PosicaoBaixo.FUNDAMENTAL) return;
        int indice = 1;
        for (int i = 0; i < componentes.size(); i++) {
            if (componentes.get(i).posicao() == baixo) {
                indice = i + 1;
                break;
            }
        }
        List<NotaComOitava> anteriores = new ArrayList<>(notas.subList(0, indice));
        notas.subList(0, indice).clear();
        int midiDoBaixo = notas.getFirst().getNumeroMidi();
        anteriores.forEach(nota -> {
            NotaComOitava elevada = nota;
            while (elevada.getNumeroMidi() <= midiDoBaixo) {
                elevada = new NotaComOitava(elevada.nota(), elevada.oitava() + 1);
            }
            notas.add(elevada);
        });
    }

    private String criarCifra(Nota tonica, ConfiguracaoAcorde configuracao, NotaComOitava baixo) {
        StringBuilder cifra = new StringBuilder(tonica.toString());
        if (configuracao.terca() == Componente.TERCA_MENOR) cifra.append('m');
        else if (configuracao.terca() == Componente.SUS2) cifra.append("sus2");
        else if (configuracao.terca() == Componente.SUS4) cifra.append("sus4");
        else if (configuracao.terca() == Componente.OMITIDA) cifra.append("(no3)");

        configuracao.componentes().stream()
            .filter(Componente::incluida)
            .filter(item -> item != configuracao.terca() && item != Componente.QUINTA_JUSTA)
            .forEach(item -> cifra.append('(').append(item.simbolo()).append(')'));
        if (configuracao.quinta() == Componente.OMITIDA) cifra.append("(no5)");
        if (configuracao.baixo() != PosicaoBaixo.FUNDAMENTAL) {
            cifra.append('/').append(baixo.nota());
        }
        return cifra.toString();
    }

    public record AcordeGerado(String cifra, List<NotaComOitava> notas) {
    }
}
