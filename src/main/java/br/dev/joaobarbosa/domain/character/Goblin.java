package br.dev.joaobarbosa.domain.character;

public class Goblin extends Monster {
  public Goblin(String name) {
    super(name, 50, 10, 5, 15, 18);
  }

  @Override
  public int performAttack(Entity target) {
    return this.attackPower;
  }
}
