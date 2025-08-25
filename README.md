# ⚔️ **Class Warriors RPG** – Turn‑Based Console Adventure

![Linting](https://github.com/juaoantonio/class-warriors/actions/workflows/linting.yml/badge.svg)
![Tests & Coverage](https://github.com/juaoantonio/class-warriors/actions/workflows/testing-and-coverage.yml/badge.svg)

> Trabalho acadêmico da disciplina **Programação Orientada a Objetos (POO)** – UFPA  
> **Tech Lead:** João Antônio Barbosa

---

## 🎯 Propósito

- Demonstrar, em um jogo de RPG por turnos, os pilares da POO:
    - **Abstração** – domínio independente de entrada/saída.
    - **Encapsulamento** – estado protegido contra acessos indevidos.
    - **Herança & Polimorfismo** – hierarquias de Heróis/Monstros.
- Exercitar **Ports & Adapters (Arquitetura Hexagonal)** — núcleo de domínio isolado, adaptadores de CLI e persistência
  plugáveis.
- Promover colaboração via **GitHub Flow** (branches, PRs) e **CI** (GitHub Actions).

---

## 🕹️ Como o jogo funciona

1. Escolha a **dificuldade** (Easy / Medium / Hard).
2. O **HeroFactory** monta sua equipe; o **MonsterFactory** cria inimigos balanceados.
3. O **TurnEngine** processa a rodada pela **velocidade (speed)** e resolve **acerto/erro/crítico**, dano e ações
   especiais (bloqueio, esquiva).
4. Cada ação vira um `BattleLogEntry` (timestamp, dano bruto/efetivo, HP antes/depois e "killing blow"), apresentado no
   console pelo **CliAdapter** e persistido via **LogPersistancePort**.
5. O jogo termina quando **todos os heróis ou todos os monstros** caem; o resumo é exibido e o histórico pode ser salvo
   em **CSV**.

---

## 🏗️ Visão geral da arquitetura

```
┌──────────────┐            application.ports.input (driver)
│ CLI Adapter  │──┐
└──────────────┘  │         application.service
                  └──▶  GameInputPort ─── GameService ──┐
                                   ▲                     │
                                   │ application.ports.output
                                   │                     ▼
                            GameOutputPort         LogPersistancePort
                                                      │
                           ┌───────────────────────────┴─────────────┐
                           │       Adapters de Persistência          │
                           │  (InMemory / CSV)                       │
                           └─────────────────────────────────────────┘

                ┌────────────────────── Domínio ──────────────────────┐
                │ Entity, Hero/Monster, Attack/Defense, TurnEngine…   │
                └──────────────────────────────────────────────────────┘
```

- **Application**: `GameService` orquestra turnos, estado e persistência via portas.
- **Domain**: regras puras (combate, entidades, logs). `TurnEngine` define ordem do turno, resolve ataques e produz
  logs.
- **Ports**: `GameInputPort`, `GameOutputPort`, `LogPersistancePort` desacoplam o núcleo de I/O.
- **Adapters**: CLI (apresenta logs e estado) e persistência (em memória/CSV).

### 📚 Documentação complementar (docs/)

- Guia de arquitetura e uso: **[`docs/guide.md`](docs/guide.md)**
- Diagrama PlantUML principal: **[`docs/modeling.puml`](docs/modeling.puml)**
    - Para renderizar: `plantuml docs/modeling.puml` → gera PNG/SVG.

> A documentação detalha camadas, entidades e casos de uso; mantenha esses arquivos atualizados conforme evoluir o
> design.

---

## 🧩 Principais componentes

- **TurnEngine** — ordem por `speed`, chance de **crit** e **miss**, execução de ataques e coleta de `BattleLogEntry`.
- **GameService** — inicia times, roda turnos, verifica fim de jogo, envia logs ao output e persistência.
- **CliAdapter (GameOutputPort)** — imprime estado, logs e telas de fim de jogo.
- **Persistência** — `InMemoryLogPersistenceAdapter` (testes) e `CsvLogPersistenceAdapter` (CSV com cabeçalho + reload).
- **Factories** — `HeroFactory` / `MonsterFactory` criam personagens e aplicam **escalonamento por dificuldade** aos
  monstros.

---

## 🧪 Testes e Cobertura

- **JUnit 5 + Mockito** para testes unitários.
- **JaCoCo**: relatório em `build/reports/jacoco/test/html/index.html`.
- **Regra de qualidade (Gradle)**: `JacocoCoverageVerification` com **>= 80% por classe** (pode ajustar em
  `build.gradle.kts`).

### Rodando localmente

```bash
./gradlew clean build            # compila, testa, gera relatórios JaCoCo e roda Spotless
./gradlew test                   # apenas testes
./gradlew jacocoTestReport       # (re)gera relatórios de cobertura
./gradlew spotlessApply          # formata o código (Google Java Format)
```

> Toolchain: **Java 21** (configurado via `java.toolchain`).

---

## ⚙️ CI — GitHub Actions (Workflows)

### `linting.yml` (Lint/Format)

- Executa **Spotless** para garantir formatação consistente.
- Gatilhos típicos: `push` e `pull_request` (ajuste conforme necessário).

### `testing-and-coverage.yml` (Build, Test, Coverage)

- Roda **build**, **testes** com **JUnit 5**, gera **cobertura** com **JaCoCo** (XML/HTML).
- Pode publicar artefatos (relatórios) e status de sucesso/falha no PR.

---

## 🚀 Execução

```bash
java -jar build/libs/battletext-rpg-1.0-SNAPSHOT.jar
# ou execução direta em dev:
javac -d bin src/**/*.java
java -cp bin br.dev.joaobarbosa.Main
```

---

## 🤝 Contribuindo

1. Crie um branch: `git checkout -b feature/<seu-nome>-<descricao>`
2. Commit → push → pull request (CI deve ficar **verde**).
3. Respeite as portas: dependa de **interfaces** (ports), não de implementações.

---

## 👥 Créditos

Projeto acadêmico desenvolvido por **Equipe Class Warriors** — UFPA  
Disciplina: **Programação Orientada a Objetos** — Prof. Reginaldo Santos

```
👑 Tech Lead ........... João Antônio Barbosa  
💻 Colaboradores ....... José Calixto · Joel Henrique · Luiz Nery · Arthur Mendes
```

*Have fun coding, fighting and learning!*
