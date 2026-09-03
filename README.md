# Gerador de Acordes Musicais

Aplicação web em Java para construir, visualizar e ouvir acordes personalizados.

## Funcionalidades

- modo simples com seleção da fundamental e de 20 tipos comuns de acordes;
- modo avançado com controle independente de terça, quinta, sexta ou 13ª, sétima, nona e 11ª;
- escolha entre 21 grafias da fundamental;
- omissão de notas opcionais e inversões;
- grafia com sustenidos, bemóis e acidentes duplos;
- cálculo de oitava, MIDI e frequência;
- reprodução sequencial ou simultânea;
- timbres de piano e violino, volume e interrupção.

## Modos de geração

### Modo Simples

Permite escolher a fundamental e gerar rapidamente um dos 20 tipos mais comuns:

- maior, menor, diminuto e aumentado;
- sus2 e sus4;
- 6 e m6;
- maj7, 7, m7, m7♭5 e dim7;
- add9, madd9, maj9, 9, m9, 7♭9 e 7♯9.

### Modo Avançado

Permite montar o acorde componente por componente, selecionar omissões, extensões,
alterações e a nota do baixo para criar inversões. Os controles de reprodução
são compartilhados pelos dois modos.

## Tecnologias

- Java 25 e Spring Boot 4.1;
- Spring Web MVC e Maven Wrapper;
- HTML, CSS, JavaScript e Web Audio API;
- JUnit 5.

## Como executar

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
./mvnw spring-boot:run
```

Depois, acesse [http://localhost:8080](http://localhost:8080).

Para executar os testes:

```powershell
.\mvnw.cmd test
```

## API HTTP

### Construir um acorde

```http
GET /api/acordes?tonica=C&terca=maior&quinta=justa&sexta=omitida&setima=menor&nona=menor&decimaPrimeira=aumentada&baixo=fundamental
```

Na interface, a nota-base é apresentada como **Fundamental**. Por compatibilidade, o
nome do parâmetro correspondente na API permanece `tonica`.

Valores aceitos:

- `terca`: `omitida`, `menor`, `maior`, `sus2` ou `sus4`;
- `quinta`: `omitida`, `diminuta`, `justa` ou `aumentada`;
- `sexta`: `omitida`, `menor`, `maior`, `b13` ou `13`;
- `setima`: `omitida`, `diminuta`, `menor` ou `maior`;
- `nona`: `omitida`, `menor`, `maior` ou `aumentada`;
- `decimaPrimeira`: `omitida`, `justa` ou `aumentada`;
- `baixo`: `fundamental`, `terca`, `quinta`, `sexta`, `setima`, `nona` ou `decima-primeira`.

O baixo escolhido precisa estar presente no acorde.

### Acordes predefinidos

```http
GET /api/acordes/tipos
GET /api/acordes/{tipo}?tonica=C
```

## Arquitetura

- `ConfiguracaoAcorde` representa as escolhas independentes;
- `DefinicaoAcorde` descreve acordes predefinidos;
- `GeradorAcorde` calcula notas, grafia, oitavas, MIDI e frequências;
- `GeradorAcordePersonalizado` combina componentes e aplica inversões;
- `TipoAcorde` mantém o catálogo predefinido;
- `AcordeController` expõe a API;
- o frontend coleta escolhas e reproduz as frequências recebidas.

O projeto não exige banco de dados, autenticação ou serviço externo de áudio.
