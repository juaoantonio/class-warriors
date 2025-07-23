package br.dev.joaobarbosa.domain.character;

import lombok.Getter;

@Getter
public abstract class Entity {
  protected String name;
  protected int hitPoints; // Pontos de Vida (HP)
  protected int attackPower; // Força de Ataque
  protected int defense; // Resistência a Danos (Defesa)
  protected int dexterity; // Destreza (Chance de Acerto)
  protected int speed; // Velocidade

  // Construtor de entidades
  public Entity(
      String name, int hitPoints, int attackPower, int defense, int dexterity, int speed) {
    this.name = name;
    this.hitPoints = hitPoints;
    this.attackPower = attackPower;
    this.defense = defense;
    this.dexterity = dexterity;
    this.speed = speed;
  }

  // --- Métodos de Lógica de Batalha ---
  public abstract int performAttack(Entity target);

  public void receiveDamage(int rawDamage) {
    int effectiveDamage = Math.max(0, rawDamage - this.defense);
    this.hitPoints = Math.max(0, this.hitPoints - effectiveDamage);
  }

  public boolean isAlive() {
    return this.hitPoints > 0;
  }

  @Override
  public String toString() {
    return String.format(
        "%s (HP: %d, ATK: %d, DEF: %d)", this.name, this.hitPoints, this.attackPower, this.defense);
  }
}
