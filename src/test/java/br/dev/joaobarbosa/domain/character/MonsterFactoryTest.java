package br.dev.joaobarbosa.domain.character;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import br.dev.joaobarbosa.domain.game.Difficulty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MonsterFactoryTest {

  @DisplayName("Deve criar monstros com atributos escalados pela dificuldade")
  @ParameterizedTest(name = "Tipo: {0}, Dificuldade: {1}")
  @CsvSource({
    "goblin, EASY, 50, 10", // Goblin no Fácil: 50 * 1.0 = 50 HP, 10 * 1.0 = 10 ATK
    "goblin, HARD, 75, 15", // Goblin no Difícil: 50 * 1.5 = 75 HP, 10 * 1.5 = 15 ATK
    "orc, MEDIUM, 162, 31", // Orc no Médio: 130 * 1.25 -> 162 HP, 25 * 1.25 -> 31 ATK
    "dragon, HARD, 300, 52" // Dragão no Difícil: 200 * 1.5 = 300 HP, 35 * 1.5 = 52.5 -> 52 ATK
  })
  void testCreateMonster_WithDifficultyScaling(
      String type, Difficulty difficulty, int expectedHp, int expectedAttack) {
    String monsterName = "Test " + type;

    Monster createdMonster = MonsterFactory.createMonster(type, monsterName, difficulty);

    assertNotNull(createdMonster);
    assertEquals(monsterName, createdMonster.getName());
    assertEquals(expectedHp, createdMonster.getHitPoints(), "HP deve ser escalado corretamente");
    assertEquals(
        expectedAttack, createdMonster.getAttackPower(), "Ataque deve ser escalado corretamente");
  }
}
