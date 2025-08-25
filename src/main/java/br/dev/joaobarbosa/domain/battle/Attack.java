package br.dev.joaobarbosa.domain.battle;

import br.dev.joaobarbosa.domain.character.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Attack {
  private final Entity attacker;
  private final double defensePenetration;
  @Setter private double baseDamage;

  /**
   * @param attacker O atacante.
   * @param baseDamage O dano bruto do ataque.
   * @param defensePenetration O percentual de penetração de defesa.
   */
  public Attack(Entity attacker, double baseDamage, double defensePenetration) {
    this.attacker = attacker;
    this.baseDamage = baseDamage;
    this.defensePenetration = defensePenetration;
  }
}
