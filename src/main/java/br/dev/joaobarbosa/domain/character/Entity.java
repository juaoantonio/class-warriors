package br.dev.joaobarbosa.domain.character;

import br.dev.joaobarbosa.domain.game.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter // Adiciona setters para TODOS os campos.
@AllArgsConstructor
@ToString(of = {"name", "hitPoints", "attackPower", "defense"})
public abstract class Entity {
  protected String name;
  protected int hitPoints; // Pontos de Vida (HP)
  protected int attackPower; // Força de Ataque
  protected int defense; // Resistência a Danos (Defesa)
  protected int dexterity; // Destreza (Chance de Acerto)
  protected int speed; // Velocidade

  // --- Métodos de Lógica de Batalha ---
  public abstract int performAttack(Entity target);

  public void receiveDamage(int rawDamage) {
    int effectiveDamage = Math.max(0, rawDamage - this.defense);
    this.hitPoints = Math.max(0, this.hitPoints - effectiveDamage);
  }

  public boolean isAlive() {
    return this.hitPoints > 0;
  }

  public void applyDifficulty(Difficulty difficulty) {
    if (difficulty == null) return;

    double multiplier = difficulty.getMonsterMultiplier();

    this.setHitPoints((int) (this.getHitPoints() * multiplier));
    this.setAttackPower((int) (this.getAttackPower() * multiplier));
    this.setDefense((int) (this.getDefense() * multiplier));
  }
}
