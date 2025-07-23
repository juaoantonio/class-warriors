package br.dev.joaobarbosa.domain.character;

public class Archer extends Hero {

  public Archer(String name) {
    // Atributos: HP médio, Ataque médio, Defesa média, Destreza muito alta, Velocidade alta.
    super(name, 95, 22, 10, 22, 16);
  }

  @Override
  public int performAttack(Entity target) {
    return this.attackPower;
  }
}
