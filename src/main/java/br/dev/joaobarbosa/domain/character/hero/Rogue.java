package br.dev.joaobarbosa.domain.character.hero;

import br.dev.joaobarbosa.domain.battle.Attack;
import br.dev.joaobarbosa.domain.battle.DefenseResult;
import br.dev.joaobarbosa.domain.character.Entity;

public class Rogue extends Hero {
  public Rogue(String name) {
    super(name, 85, 18, 8, 20, 20);
  }

  @Override
  public Attack performAttack(Entity target) {
    int bonusDamage = this.speed / 2;
    return new Attack(this, this.attackPower + bonusDamage, 0.10);
  }

  @Override
  public DefenseResult receiveDamage(Attack attack) {
    double dodgeChance = this.dexterity / 75.0;
    if (Math.random() < dodgeChance) {
      return new DefenseResult(0, "ESQUIVOU"); // Retorna 0 de dano e a ação especial
    }
    return super.receiveDamage(attack);
  }
}
