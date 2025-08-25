# 📘 Documentação da Arquitetura — *Class Warriors*

Este documento explica, camada por camada, como a aplicação **Class Warriors** está estruturada.  
A arquitetura segue princípios de **Clean Architecture** e **Ports & Adapters (Hexagonal Architecture)**, separando
responsabilidades entre **domínio**, **aplicação** e **infraestrutura**.

---

## 🏛️ Visão Geral da Arquitetura

```text
┌────────────────────┐
│    Interface CLI   │  ←── Entrada do usuário
└────────┬───────────┘
         │ implementa GameOutputPort
┌────────▼───────────┐
│   GameService      │  ←── Camada de Aplicação
└────────┬───────────┘
         │ usa
         ▼
┌────────────────────┐
│      Domínio       │  ←── Regras de negócio puras
│ (TurnEngine, Hero, │
│ Monster, BattleLog)│
└────────┬───────────┘
         │ via portas
┌────────▼───────────┐
│  Persistência CSV  │  ←── Adaptador de saída
└────────────────────┘
```

---

## 🧩 Camadas da Aplicação

### **1. Camada de Entrada (Interface / CLI)**

**Pacote:** `br.dev.joaobarbosa.adapters`  
**Principal classe:** `CliAdapter`

- Fornece a **interface interativa** no terminal.
- Recebe comandos do jogador, exibe informações do jogo e envia instruções para a camada de aplicação.
- Implementa a interface `GameOutputPort`.

**Responsabilidades principais:**

- Mostrar o estado atual do jogo (heróis, monstros, HP, turno).
- Exibir logs detalhados da batalha.
- Gerenciar fluxo do jogo (iniciar, jogar turno, finalizar).
- Interagir com o **GameService**.

```java
CliAdapter cli = new CliAdapter();
cli.

setGameService(gameService);
cli.

run();
```

---

### **2. Camada de Aplicação (Serviço / Orquestração)**

**Pacote:** `br.dev.joaobarbosa.application.service`  
**Principal classe:** `GameService`

- Contém a **lógica de orquestração do jogo**, mas não as regras de negócio.
- É onde ocorre a interação entre heróis, monstros e o motor de turnos.

**Principais responsabilidades:**

- Criar heróis via `HeroFactory`.
- Criar monstros via `MonsterFactory`.
- Controlar o estado do jogo (`GameState`).
- Executar turnos usando o **TurnEngine**.
- Persistir logs via `LogPersistancePort`.

**Principais métodos:**

- `startGame()` → Inicializa heróis e monstros.
- `playTurn()` → Roda um turno completo e gera logs.
- `endGame()` → Salva histórico final.
- `isGameOver()` → Verifica se alguém venceu.

---

### **3. Camada de Domínio (Regras de Negócio)**

**Pacote:** `br.dev.joaobarbosa.domain`

Esta é a **alma da aplicação**: contém toda a lógica de combate, criação de personagens e cálculos.

#### **3.1. Engine de Batalha**

**Classe principal:** `TurnEngine`

- Calcula a ordem dos turnos com base na velocidade (`speed`).
- Resolve ataques, incluindo **chance de crítico** e **evasão**.
- Define o alvo automaticamente:
    - Heróis atacam monstros aleatórios vivos.
    - Monstros escolhem o herói com **menos HP**.
- Retorna os logs (`BattleLogEntry`) do turno.

```java
List<BattleLogEntry> logs = turnEngine.executeTurn(heroes, monsters, round);
```

---

#### **3.2. Entidades do Jogo**

**Pacote:** `br.dev.joaobarbosa.domain.character`

Base: **Entity**

- Representa qualquer personagem (herói ou monstro).
- Possui atributos como:
    - `hitPoints`
    - `attackPower`
    - `defense`
    - `dexterity`
    - `speed`
- Métodos principais:
    - `performAttack(Entity target)`
    - `receiveDamage(Attack attack)`
    - `isAlive()`

##### **Heróis (`hero`)**

- `Warrior` → Alta defesa, chance de **bloquear ataques**.
- `Mage` → Alto dano mágico com **penetração de defesa**.
- `Archer` → Dano baseado em **destreza**.
- `Rogue` → Alta velocidade, chance de **esquivar ataques**.

##### **Monstros (`monster`)**

- `Goblin` → Pequeno, chance de **esquiva**.
- `Orc` → Dano aumenta quando perde HP.
- `Dragon` → Defesa natural alta e **ataque flamejante**.

---

#### **3.3. Ataques e Defesas**

**Pacote:** `br.dev.joaobarbosa.domain.battle`

- `Attack` → Representa um ataque.
- `DefenseResult` → Resultado da defesa (dano sofrido + ação especial).
- `AttackResult` → Enum com três possibilidades:
    - `HIT`
    - `CRITICAL_HIT`
    - `MISSED`

---

#### **3.4. Logs da Batalha**

**Pacote:** `br.dev.joaobarbosa.domain.logs`

- `BattleLog` → Estrutura para armazenar múltiplos logs.
- `BattleLogEntry` → Registro detalhado de cada ataque:
    - Atacante / Alvo
    - Dano bruto / efetivo
    - Ação especial (esquiva, bloqueio, crítico)
    - HP antes e depois
    - Indica se houve **golpe fatal**.

---

### **4. Camada de Saída (Persistência)**

**Pacote:** `br.dev.joaobarbosa.adapters`

#### **4.1. Persistência CSV**

**Classe:** `CsvLogPersistenceAdapter`

- Implementa `LogPersistancePort`.
- Salva os registros da batalha em `logs.csv`.
- Recupera históricos anteriores.

#### **4.2. Persistência em Memória**

**Classe:** `InMemoryLogPersistenceAdapter`

- Usada nos testes.
- Simula persistência sem salvar em disco.

---

### **5. Portas e Adapters**

Para manter a aplicação desacoplada, usamos **interfaces**:

#### **Portas de Entrada**

- `GameInputPort` → Controla o fluxo do jogo.

#### **Portas de Saída**

- `GameOutputPort` → Interface para exibir resultados.
- `LogPersistancePort` → Interface para salvar e carregar logs.

---

## 🧪 Testes

A aplicação possui testes unitários cobrindo:

- **Batalha** → `TurnEngineTest`
- **Logs** → `BattleLogEntryTest`
- **Entidades** → `EntityTest`
- **Factories** → `HeroFactoryTest` e `MonsterFactoryTest`
- **Serviço** → `GameServiceTest`
- **Persistência** → `CsvLogPersistenceAdapterTest` e `InMemoryLogPersistenceAdapterTest`

---

## 🔹 Resumo Final

| Camada          | Pacote                | Responsabilidade Principal            |
|-----------------|-----------------------|---------------------------------------|
| Interface (CLI) | `adapters`            | Receber comandos e exibir resultados  |
| Aplicação       | `application.service` | Orquestra lógica e gerencia estado    |
| Domínio         | `domain`              | Regras puras de combate e personagens |
| Persistência    | `adapters`            | Armazenar e recuperar logs            |
| Portas          | `application.ports`   | Contratos para entrada e saída        |
