package br.dev.joaobarbosa.domain.character;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import br.dev.joaobarbosa.domain.character.monster.Monster;
import br.dev.joaobarbosa.domain.character.monster.MonsterFactory;
import br.dev.joaobarbosa.domain.game.Difficulty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MonsterFactoryTest {

  @DisplayName("Deve criar monstros com atributos escalados pela dificuldade")
  @ParameterizedTest(name = "Tipo: {0}, Dificuldade: {1}")
  @CsvSource({
    "goblin, EASY, 50, 10",
    "goblin, HARD, 75, 15",
    "orc, MEDIUM, 162.5, 31",
    "dragon, HARD, 300, 52"
  })
  void testCreateMonster_WithDifficultyScaling(
      String type, Difficulty difficulty, double expectedHp, int expectedAttack) {
    String monsterName = "Test " + type;

    Monster createdMonster = MonsterFactory.createMonster(type, monsterName, difficulty);

    assertNotNull(createdMonster);
    assertEquals(monsterName, createdMonster.getName());
    // Agora a comparação funciona, pois ambos os valores são double.
    assertEquals(expectedHp, createdMonster.getHitPoints(), "HP deve ser escalado corretamente");
    assertEquals(
        expectedAttack, createdMonster.getAttackPower(), "Ataque deve ser escalado corretamente");
  }
}
