package br.dev.joaobarbosa.domain.character.monster;

import br.dev.joaobarbosa.domain.game.Difficulty;
import java.util.List;
import java.util.Random;

public class MonsterFactory {

  private static final List<String> MONSTER_TYPES = List.of("goblin", "orc", "dragon");
  private static final Random random = new Random();

  public static Monster createMonster(String type, String name, Difficulty difficulty) {
    Monster monster =
        switch (type.toLowerCase()) {
          case "goblin" -> new Goblin(name);
          case "orc" -> new Orc(name);
          case "dragon" -> new Dragon(name);
          default -> throw new IllegalArgumentException("Tipo de monstro desconhecido: " + type);
        };
    monster.applyDifficulty(difficulty);
    return monster;
  }

  public static Monster createRandomMonster(String name, Difficulty difficulty) {
    String randomType = MONSTER_TYPES.get(random.nextInt(MONSTER_TYPES.size()));
    return createMonster(randomType, name, difficulty);
  }
}
