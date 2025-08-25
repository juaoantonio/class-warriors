package br.dev.joaobarbosa.domain.character.hero;

import br.dev.joaobarbosa.domain.battle.Attack;
import br.dev.joaobarbosa.domain.character.Entity;

public class Archer extends Hero {
  public Archer(String name) {
    super(name, 95, 22, 10, 22, 16);
  }

  @Override
  public Attack performAttack(Entity target) {
    int bonusDamage = this.dexterity / 2;
    return new Attack(this, this.attackPower + bonusDamage, 0);
  }
}
