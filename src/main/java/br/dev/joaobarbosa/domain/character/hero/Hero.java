package br.dev.joaobarbosa.domain.character.hero;

import br.dev.joaobarbosa.domain.character.Entity;

public abstract class Hero extends Entity {
  // CORREÇÃO: hitPoints agora é double para ser compatível com a superclasse Entity
  public Hero(
      String name, double hitPoints, int attackPower, int defense, int dexterity, int speed) {
    super(name, hitPoints, attackPower, defense, dexterity, speed);
  }
}
