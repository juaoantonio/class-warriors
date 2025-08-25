package br.dev.joaobarbosa.domain.battle;

import lombok.Getter;

@Getter
public class DefenseResult {
  private final double effectiveDamage;
  private final String specialAction;

  public DefenseResult(double effectiveDamage, String specialAction) {
    this.effectiveDamage = effectiveDamage;
    this.specialAction = specialAction;
  }

  public DefenseResult(double effectiveDamage) {
    this(effectiveDamage, "");
  }
}
