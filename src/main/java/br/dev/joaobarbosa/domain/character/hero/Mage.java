package br.dev.joaobarbosa.domain.character.hero;

import br.dev.joaobarbosa.domain.battle.Attack;
import br.dev.joaobarbosa.domain.character.Entity;

public class Mage extends Hero {
  public Mage(String name) {
    super(name, 80, 30, 5, 18, 14);
  }

  @Override
  public Attack performAttack(Entity target) {
    int spellBonus = 10;
    int baseDamage = this.attackPower + spellBonus;
    return new Attack(this, baseDamage, 0.50);
  }
}
