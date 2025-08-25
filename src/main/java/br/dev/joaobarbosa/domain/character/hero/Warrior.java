package br.dev.joaobarbosa.domain.character.hero;

import br.dev.joaobarbosa.domain.battle.Attack;
import br.dev.joaobarbosa.domain.battle.DefenseResult;
import br.dev.joaobarbosa.domain.character.Entity;

public class Warrior extends Hero {
  private static final double BLOCK_CHANCE = 0.25;
  private static final double BLOCK_REDUCTION = 0.5;

  public Warrior(String name) {
    super(name, 120, 25, 15, 10, 12);
  }

  @Override
  public Attack performAttack(Entity target) {
    return new Attack(this, this.attackPower, 0.0);
  }

  @Override
  public DefenseResult receiveDamage(Attack attack) {
    if (Math.random() < BLOCK_CHANCE) {
      double blockedDamage = attack.getBaseDamage() * (1 - BLOCK_REDUCTION);
      attack.setBaseDamage(blockedDamage);

      DefenseResult result = super.receiveDamage(attack);
      return new DefenseResult(result.getEffectiveDamage(), "BLOQUEOU");
    }
    return super.receiveDamage(attack);
  }
}
