package br.dev.joaobarbosa.domain.battle;

import static org.junit.jupiter.api.Assertions.*;

import br.dev.joaobarbosa.domain.character.hero.Archer;
import br.dev.joaobarbosa.domain.character.hero.Hero;
import br.dev.joaobarbosa.domain.character.monster.Goblin;
import br.dev.joaobarbosa.domain.character.monster.Monster;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TurnEngineTest {

  private TurnEngine turnEngine;

  @BeforeEach
  void setUp() {
    turnEngine = new TurnEngine();
  }

  @Test
  void getTurnOrderShouldSortEntitiesBySpeed() {
    Hero fastHero = new Archer("Arqueiro Veloz"); // speed 16
    fastHero.setSpeed(20);

    Monster slowMonster = new Goblin("Goblin Lento"); // speed 18
    slowMonster.setSpeed(10);

    List<Hero> heroes = List.of(fastHero);
    List<Monster> monsters = List.of(slowMonster);

    var turnOrder = turnEngine.getTurnOrder(heroes, monsters);

    assertEquals(2, turnOrder.size());
    assertEquals("Arqueiro Veloz", turnOrder.get(0).getName());
    assertEquals("Goblin Lento", turnOrder.get(1).getName());
  }

  @Test
  void executeTurnShouldGenerateLogs() {
    Hero hero = new Archer("Legolas"); // ATK 22, DEX 22
    Monster monster = new Goblin("Snaga"); // HP 50, DEF 5, DEX 15

    List<Hero> heroes = List.of(hero);
    List<Monster> monsters = List.of(monster);

    var logs = turnEngine.executeTurn(heroes, monsters, 1);

    assertFalse(logs.isEmpty());
    assertEquals(2, logs.size()); // Um ataque de cada lado
  }
}
