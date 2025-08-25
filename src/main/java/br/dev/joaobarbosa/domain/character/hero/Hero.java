package br.dev.joaobarbosa.domain.character.hero;

import br.dev.joaobarbosa.domain.character.Entity;

public abstract class Hero extends Entity {
  public Hero(
      String name, double hitPoints, int attackPower, int defense, int dexterity, int speed) {
    super(name, hitPoints, attackPower, defense, dexterity, speed);
  }
}
