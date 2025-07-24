package br.dev.joaobarbosa.domain.character;

public abstract class Monster extends Entity {

  public Monster(
      String name, int hitPoints, int attackPower, int defense, int dexterity, int speed) {
    super(name, hitPoints, attackPower, defense, dexterity, speed);
  }
}
