package br.dev.joaobarbosa.domain.character.monster;

import br.dev.joaobarbosa.domain.character.Entity;
import br.dev.joaobarbosa.domain.character.hero.Hero;
import java.util.Comparator;
import java.util.List;

public abstract class Monster extends Entity {

  public Monster(
      String name, int hitPoints, int attackPower, int defense, int dexterity, int speed) {
    super(name, hitPoints, attackPower, defense, dexterity, speed);
  }

  /**
   * IA simples para decidir qual herói atacar. Ataca o herói com o menor HP atual.
   *
   * @param heroes A lista de heróis vivos.
   * @return O herói a ser atacado.
   */
  public Entity decideTarget(List<Hero> heroes) {
    return heroes.stream()
        .filter(Entity::isAlive)
        .min(Comparator.comparingDouble(Entity::getHitPoints))
        .orElse(null);
  }
}
