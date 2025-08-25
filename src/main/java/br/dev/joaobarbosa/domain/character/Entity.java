// src/main/java/br/dev/joaobarbosa/domain/character/Entity.java
package br.dev.joaobarbosa.domain.character;

import br.dev.joaobarbosa.domain.battle.Attack;
import br.dev.joaobarbosa.domain.battle.DefenseResult;
import br.dev.joaobarbosa.domain.game.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString(of = {"name", "hitPoints", "attackPower", "defense"})
public abstract class Entity {

  @Setter protected String name;
  protected double hitPoints;
  @Setter protected int attackPower;
  @Setter protected int defense;
  @Setter protected int dexterity;
  @Setter protected int speed;

  /**
   * Setter customizado para garantir que o HP nunca seja menor que zero.
   *
   * @param hitPoints O novo valor para o HP.
   */
  public void setHitPoints(double hitPoints) {
    this.hitPoints = Math.max(0, hitPoints);
  }

  // --- Métodos de Lógica de Batalha ---

  public abstract Attack performAttack(Entity target);

  public DefenseResult receiveDamage(Attack attack) {
    double effectiveDefense = this.defense * (1.0 - attack.getDefensePenetration());
    double effectiveDamage = Math.max(0, attack.getBaseDamage() - effectiveDefense);
    return new DefenseResult(effectiveDamage);
  }

  public boolean isAlive() {
    return this.hitPoints > 0;
  }

  public void applyDifficulty(Difficulty difficulty) {
    if (difficulty == null) return;
    double multiplier = difficulty.getMonsterMultiplier();
    this.setHitPoints(this.getHitPoints() * multiplier);
    this.setAttackPower((int) (this.getAttackPower() * multiplier));
    this.setDefense((int) (this.getDefense() * multiplier));
  }
}
