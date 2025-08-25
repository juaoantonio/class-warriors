package br.dev.joaobarbosa.domain.character.monster;

import br.dev.joaobarbosa.domain.battle.Attack;
import br.dev.joaobarbosa.domain.battle.DefenseResult;
import br.dev.joaobarbosa.domain.character.Entity;

public class Dragon extends Monster {
  public Dragon(String name) {
    super(name, 200, 35, 25, 12, 15);
  }

  @Override
  public Attack performAttack(Entity target) {
    int fireBonus = 20;
    return new Attack(this, this.attackPower + fireBonus, 0.25);
  }

  @Override
  public DefenseResult receiveDamage(Attack attack) {
    double damageReduction = 5;
    attack.setBaseDamage(Math.max(0, attack.getBaseDamage() - damageReduction));

    DefenseResult result = super.receiveDamage(attack);
    return new DefenseResult(result.getEffectiveDamage(), "ESCAMAS RESISTENTES");
  }
}
