package br.dev.joaobarbosa.domain.character;

public class Dragon extends Monster {
  public Dragon(String name) {
    super(name, 200, 35, 25, 12, 15);
  }

  @Override
  public int performAttack(Entity target) {
    int fireBonus = 10;
    return this.attackPower + fireBonus;
  }
}
