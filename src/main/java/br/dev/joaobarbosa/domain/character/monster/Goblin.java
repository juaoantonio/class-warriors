package br.dev.joaobarbosa.domain.character.monster;

import br.dev.joaobarbosa.domain.battle.Attack;
import br.dev.joaobarbosa.domain.battle.DefenseResult;
import br.dev.joaobarbosa.domain.character.Entity;

public class Goblin extends Monster {
  public Goblin(String name) {
    super(name, 50, 10, 5, 15, 18);
  }

  @Override
  public Attack performAttack(Entity target) {
    return new Attack(this, this.attackPower, 0.0);
  }

  @Override
  public DefenseResult receiveDamage(Attack attack) {
    double dodgeChance = 0.15;
    if (Math.random() < dodgeChance) {
      return new DefenseResult(0, "ESQUIVOU"); // Retorna 0 de dano e a ação especial
    }
    return super.receiveDamage(attack);
  }
}
