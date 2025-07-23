# ⚔️ **BattleText RPG** – Turn‑Based Console Adventure

![Linting](https://github.com/juaoantonio/class-warriors/actions/workflows/linting.yml/badge.svg)
![Tests & Coverage](https://github.com/juaoantonio/class-warriors/actions/workflows/testing-and-coverage.yml/badge.svg)

> Trabalho acadêmico da disciplina **Programação Orientada a Objetos (POO)** – UFPA
> **Tech Lead:** João Antônio Barbosa

---

## 🎯 Propósito

* Demonstrar, em um jogo de RPG por turnos, os pilares da POO

    * **Abstração** – domínio de negócio desacoplado de entrada/saída
    * **Encapsulamento** – estado protegido contra acesso externo indevido
    * **Herança & Polimorfismo** – hierarquias de Heróis e Monstros
* Exercitar a arquitetura **Ports & Adapters (Hexagonal)** – núcleo independente, adaptadores de CLI e persistência plug‑áveis.
* Promover **colaboração GitHub** com *branches*, *pull requests* e **CI** (GitHub Actions).

---

## 🕹️ Como o jogo funciona

1. **Escolha a dificuldade** (Easy / Medium / Hard).
2. O **Hero Factory** monta sua equipe; o **Monster Factory** cria inimigos balanceados.
3. O **TurnEngine** processa rodadas automaticamente, pela velocidade (`speed`) de cada entidade.
4. Cada ação gera um `BattleLogEntry`, salvo via **LogPersistancePort**
   (implementação default `InMemoryLogAdapter`; troque facilmente por CSV ou banco).
5. O console exibe o combate em tempo real; ao final, um resumo é mostrado.

---

## 🏗️ Visão geral da arquitetura

```
┌──────────────┐            application.ports.input
│ CLI Adapter  │──┐         (driver)                 ┌───────────────┐
│ (adapter.cli)│  │                                   Core (domain + │
└──────────────┘  │         application.service       │ application)  │
                  └──▶  GameInputPort ─── GameService ─┤ TurnEngine   │
┌──────────────┐            application.ports.output  └───────────────┘
│ Log Adapter  │◀──  LogPersistancePort (driven)                ▲
│ (adapter.persistence)                                       Logs
└──────────────┘
```

* **Domain** – `domain.character`, `domain.battle`, `domain.game`, `domain.logs`
* **Application** – portas (`application.ports.*`) + `GameService`
* **Adapters** – `adapter.cli`, `adapter.persistence`

---

## 🚀 Executando localmente (Gradle Kotlin DSL)

> Requer **Java 21** ou superior.

```bash
git clone https://github.com/juaoantonio/class-warriors.git
cd class-warriors
./gradlew clean build        # compila, roda testes, cobertura Jacoco & Spotless
java -jar build/libs/battletext-rpg-1.0-SNAPSHOT.jar
```

### Execução rápida via CLI

```bash
javac -d bin src/**/*.java
java -cp bin br.dev.joaobarbosa.Main
```

---

## 🔍 Estrutura de diretórios

```text
src/
├─ domain/                ← Entidades e regras de negócio
│  ├─ character/
│  ├─ battle/
│  ├─ game/
│  └─ logs/
├─ application/
│  ├─ ports/
│  │  ├─ input/
│  │  └─ output/
│  └─ service/
├─ adapter/
│  ├─ cli/                ← Interface de linha de comando
│  └─ persistence/        ← Salvamento / leitura de BattleLogs
└─ Main.java              ← Bootstrap do jogo
```

---

## 📜 Scripts Gradle úteis

| Comando                      | Descrição                                               |
| ---------------------------- | ------------------------------------------------------- |
| `./gradlew build`            | Compila, testa, calcula cobertura e verifica formatação |
| `./gradlew test`             | Executa somente a suíte de testes                       |
| `./gradlew jacocoTestReport` | Gera relatórios HTML/XML de cobertura                   |
| `./gradlew spotlessApply`    | Formata o código via Google Java Format                 |
| `./gradlew run`<sup>\*</sup> | Executa o jogo (caso adicione o plugin `application`)   |
| `plantuml docs/model.puml`   | Gera o diagrama UML atualizado                          |

<sup>\*</sup>Para usar `run`, inclua `plugins { application }` e defina `mainClass` no `build.gradle.kts`.


---

## 🤝 Contribuindo

1. Crie um *fork* / *branch*:
   `git checkout -b feature/<seu-nome>-<descricao>`
2. **Commit → push → pull request** – o CI deve estar **verde** antes de solicitar *review*.
3. Respeite as portas: dependa de **interfaces** ou **classes abstratas**, não de implementações concretas.

---

## 🛠️ Tecnologias

* **Java 21**
* **Gradle Kotlin DSL**
* **JUnit 5 + Mockito** (testes)
* **Lombok** (boilerplate)
* **Jacoco** (cobertura)
* **Spotless** (code‑style Google)
* **PlantUML** (modelagem)
* **GitHub Actions** (lint, testes, cobertura)

---

## 🧑‍💻 Créditos

Projeto acadêmico desenvolvido por **Equipe Class Warriors**
Universidade Federal do Pará – Instituto de Ciências Exatas e Naturais
Disciplina: **Programação Orientada a Objetos** – Prof. Reginaldo Santos

```
👑 Tech Lead ........... João Antônio Barbosa  
💻 Colaboradores ....... José Calixto · Joel Henrique · Luiz Nery · Arthur Mendes
```

*Have fun coding, fighting and learning!*
