package br.dev.joaobarbosa.domain.character;

public abstract class Hero extends Entity {

  public Hero(String name, int hitPoints, int attackPower, int defense, int dexterity, int speed) {
    super(name, hitPoints, attackPower, defense, dexterity, speed);
  }
}
