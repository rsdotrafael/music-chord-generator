# Gerador de Acordes Musicais

Aplicação web em Java para construir, visualizar e ouvir acordes personalizados.

## Funcionalidades

- escolha de 21 grafias de tônica;
- controle independente de terça, quinta, sexta ou 13ª, sétima, nona e 11ª;
- omissão de notas opcionais e inversões;
- grafia com sustenidos, bemóis e acidentes duplos;
- cálculo de oitava, MIDI e frequência;
- reprodução sequencial ou simultânea;
- timbres de piano e violino, volume e interrupção.

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

## API HTTP

### Construir um acorde

```http
GET /api/acordes?tonica=C&terca=maior&quinta=justa&sexta=omitida&setima=menor&nona=menor&decimaPrimeira=aumentada&baixo=fundamental
```

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
