package br.dev.joaobarbosa.domain.game;

import lombok.Getter;

@Getter
public enum Difficulty {
  EASY(1.0, 1.0),
  MEDIUM(1.25, 1.25),
  HARD(1.5, 1.5);

  private final double monsterMultiplier;
  private final double monsterCountFactor;

  Difficulty(double multiplier, double countFactor) {
    this.monsterMultiplier = multiplier;
    this.monsterCountFactor = countFactor;
  }
}
