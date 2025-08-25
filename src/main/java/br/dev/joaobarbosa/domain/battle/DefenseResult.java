package br.dev.joaobarbosa.domain.battle;

import lombok.Getter;

@Getter
public class DefenseResult {
  private final double effectiveDamage;
  private final String specialAction; // Ex: "BLOQUEOU", "ESQUIVOU"

  public DefenseResult(double effectiveDamage, String specialAction) {
    this.effectiveDamage = effectiveDamage;
    this.specialAction = specialAction;
  }

  public DefenseResult(double effectiveDamage) {
    this(effectiveDamage, ""); // Construtor para quando não há ação especial
  }
}
