package br.dev.joaobarbosa.domain.game;

import lombok.Getter;

@Getter
public enum Difficulty {
  EASY(1.0),
  MEDIUM(1.25),
  HARD(1.5);

  private final double monsterMultiplier;

  Difficulty(double multiplier) {
    this.monsterMultiplier = multiplier;
  }
}
