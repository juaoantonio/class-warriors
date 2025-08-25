package br.dev.joaobarbosa.domain.character.monster;

import br.dev.joaobarbosa.domain.battle.Attack;
import br.dev.joaobarbosa.domain.character.Entity;

public class Orc extends Monster {
  public Orc(String name) {
    super(name, 130, 25, 20, 8, 8);
  }

  @Override
  public Attack performAttack(Entity target) {
    int currentDamage = this.attackPower;
    if (this.hitPoints / 130.0 < 0.4) {
      currentDamage *= 2;
    }
    return new Attack(this, currentDamage, 0.0);
  }
}
