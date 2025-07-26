package br.dev.joaobarbosa.domain.character;

import br.dev.joaobarbosa.domain.game.Difficulty;

public class MonsterFactory {

  public static Monster createMonster(String type, String name, Difficulty difficulty) {
    Monster monster =
        switch (type.toLowerCase()) {
          case "goblin" -> new Goblin(name);
          case "orc" -> new Orc(name);
          case "dragon" -> new Dragon(name);
          default -> throw new IllegalArgumentException("Tipo de monstro desconhecido: " + type);
        };

    // 2. Aplica o modificador de dificuldade ao monstro criado
    monster.applyDifficulty(difficulty);

    // 3. Retorna o monstro já modificado
    return monster;
  }
}
