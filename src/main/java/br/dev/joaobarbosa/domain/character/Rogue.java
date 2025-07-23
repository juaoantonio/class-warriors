package br.dev.joaobarbosa.domain.character;

public class Rogue extends Hero {

  public Rogue(String name) {
    // Atributos: HP baixo, Ataque baixo, Defesa baixa, Destreza alta, Velocidade muito alta.
    super(name, 85, 18, 8, 20, 20);
  }

  /**
   * O Ladino é rápido. Seu ataque pode incorporar um bônus baseado na sua velocidade. Por enquanto,
   * será um ataque simples.
   */
  @Override
  public int performAttack(Entity target) {
    // Lógica futura: poderia ter uma chance maior de crítico baseada na velocidade ou destreza.
    return this.attackPower;
  }
}
