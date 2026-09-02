package br.com.geradoracordes.dominio;

import java.util.List;

public record DefinicaoAcorde(
    String id,
    String nome,
    String sufixo,
    List<Integer> intervalos,
    List<Integer> graus
) {
    public DefinicaoAcorde {
        if (id == null || id.isBlank() || nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Identificador e nome do acorde são obrigatórios");
        }
        sufixo = sufixo == null ? "" : sufixo;
        intervalos = List.copyOf(intervalos);
        graus = List.copyOf(graus);
        if (intervalos.isEmpty() || intervalos.size() != graus.size()) {
            throw new IllegalArgumentException("Intervalos e graus devem ter o mesmo tamanho");
        }
        if (intervalos.getFirst() != 0 || graus.getFirst() != 0) {
            throw new IllegalArgumentException("O acorde deve começar pela tônica");
        }
    }
}
