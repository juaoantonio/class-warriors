package br.dev.joaobarbosa.domain.character;

public class Mage extends Hero {

  public Mage(String name) {
    // Atributos: HP baixo, Ataque mágico alto, Defesa baixa, Destreza alta, Velocidade alta.
    super(name, 80, 30, 5, 18, 14);
  }

  @Override
  public int performAttack(Entity target) {
    int spellBonus = 10; // Bônus de dano mágico
    return this.attackPower + spellBonus;
  }
}
