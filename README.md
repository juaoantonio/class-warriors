# 🎮 BattleText RPG

Este é um jogo de RPG baseado em texto, desenvolvido como trabalho acadêmico para a disciplina de Programação Orientada a Objetos (POO). O jogo é em turnos e simula batalhas automáticas entre heróis e monstros.

---

## ✅ Objetivo do Projeto

- Criar um jogo simples de batalha por turnos em Java
- Aplicar os conceitos de orientação a objetos: herança, abstração, polimorfismo e encapsulamento
- Utilizar boas práticas de organização de código
- Trabalhar em equipe com divisão de tarefas e integração contínua (CI)

---

## 📚 Como o jogo funciona

- O jogador escolhe a dificuldade e os heróis da equipe
- O jogo gera automaticamente monstros do nível correspondente
- A batalha é simulada automaticamente (os personagens atacam sozinhos com base em seus atributos)
- O resultado é mostrado em tempo real no console
- Todas as ações são registradas em um arquivo de log para visualização posterior

---

## 🏗️ Estrutura do Projeto

O projeto está dividido em camadas para facilitar a organização do código:

```plaintext
src/
├── domain/      ← Onde ficam os personagens, atributos e lógica de ataque
├── app/         ← Onde fica o controle da batalha e execução do jogo
├── adapter/     ← Onde fica a entrada (console) e saída (log em arquivo)
├── infra/       ← Códigos auxiliares como IA e sorteios
└── Main.java    ← Ponto de partida do jogo
```

Cada pasta tem uma responsabilidade clara. Por exemplo:
- `domain/model`: define os heróis e monstros
- `app/GameService`: controla a lógica do jogo
- `adapter/input/ConsoleUI`: mostra o menu e interage com o jogador

---

## ▶️ Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/battletext-rpg.git
   ```

2. Compile e execute com o terminal (Java 21+):
   ```bash
   cd battletext-rpg
   javac -d bin src/**/*.java
   java -cp bin Main
   ```

Ou utilize uma IDE como IntelliJ, Eclipse ou VSCode para rodar o arquivo `Main.java`.

---

## 🤝 Como contribuir

- Crie uma branch com seu nome e sua funcionalidade:
  ```
  git checkout -b feature/seu-nome-funcionalidade
  ```

- Envie um pull request para que o time revise e integre ao projeto.

- Sempre que possível, mantenha os nomes e responsabilidades organizadas.

---

## 🛠️ Tecnologias utilizadas

- Java 21
- Git + GitHub
- Integração Contínua (CI) com GitHub Actions
- Padrões de qualidade: divisão em camadas, revisão de código e testes

---

## 🧠 Conceitos usados no jogo

- **Herança**: os heróis e monstros herdam de uma classe base chamada `Player`
- **Polimorfismo**: cada personagem tem seu próprio jeito de atacar (`realizarAtaque()`)
- **Encapsulamento**: os atributos são protegidos e acessados por métodos
- **Abstração**: a lógica do jogo é separada da interface que o jogador vê

---

## 📁 Créditos

Projeto desenvolvido por [Nome do Time] para fins acadêmicos.

Tech Lead: João Antônio Barbosa 
Disciplina: Programação Orientada a Objetos  
Curso: Ciência da Computação / Universidade Federal do Pará (UFPA)