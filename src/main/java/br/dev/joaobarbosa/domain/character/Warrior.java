package br.dev.joaobarbosa.domain.character;

public class Warrior extends Hero {

  public Warrior(String name) {
    // Atributos: HP alto, Ataque bom, Defesa alta, baixa Destreza, Velocidade média.
    super(name, 120, 25, 15, 10, 12);
  }

  @Override
  public int performAttack(Entity target) {
    return this.attackPower;
  }
}
