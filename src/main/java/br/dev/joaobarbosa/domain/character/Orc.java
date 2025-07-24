package br.dev.joaobarbosa.domain.character;

public class Orc extends Monster {
  public Orc(String name) {
    super(name, 130, 25, 20, 8, 8);
  }

  @Override
  public int performAttack(Entity target) {
    return this.attackPower;
  }
}
