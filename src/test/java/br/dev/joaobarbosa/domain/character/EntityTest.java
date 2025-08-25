package br.dev.joaobarbosa.domain.character;

import static org.junit.jupiter.api.Assertions.*;

import br.dev.joaobarbosa.domain.battle.Attack;
import br.dev.joaobarbosa.domain.battle.DefenseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EntityTest {

  private ConcreteEntity character;

  @BeforeEach
  void setUp() {
    character = new ConcreteEntity("Dummy", 100.0, 10, 20, 5, 5);
  }

  @Test
  @DisplayName("Deve receber dano normal quando o ataque é maior que a defesa")
  void testReceiveDamage_NormalDamage() {
    Attack attack = new Attack(null, 30.0, 0.0);
    DefenseResult result = character.receiveDamage(attack);
    character.setHitPoints(character.getHitPoints() - result.getEffectiveDamage());

    assertEquals(10.0, result.getEffectiveDamage(), "O dano efetivo deve ser 10.");
    assertEquals(90.0, character.getHitPoints(), "HP deve ser reduzido pelo dano líquido");
  }

  @Test
  @DisplayName("Não deve receber dano quando a defesa é maior ou igual ao ataque")
  void testReceiveDamage_BlockedByDefense() {
    Attack attack = new Attack(null, 15.0, 0.0);

    DefenseResult result = character.receiveDamage(attack);
    character.setHitPoints(character.getHitPoints() - result.getEffectiveDamage());

    assertEquals(0.0, result.getEffectiveDamage(), "O dano efetivo deve ser 0.");
    assertEquals(
        100.0, character.getHitPoints(), "HP não deve mudar se o dano for totalmente defendido");
  }

  @Test
  @DisplayName("HP deve ser 0 após receber dano fatal")
  void testReceiveDamage_Overkill() {
    Attack attack = new Attack(null, 150.0, 0.0);

    DefenseResult result = character.receiveDamage(attack);
    character.setHitPoints(character.getHitPoints() - result.getEffectiveDamage());

    assertEquals(130.0, result.getEffectiveDamage(), "O dano efetivo deve ser 130.");

    assertEquals(0.0, character.getHitPoints(), "HP deve ser 0 após receber dano fatal");
    assertFalse(character.isAlive(), "Personagem não deve estar vivo após dano fatal");
  }

  @Test
  @DisplayName("Deve retornar true quando HP > 0 e false quando HP = 0")
  void testIsAlive() {
    assertTrue(character.isAlive(), "Deve estar vivo no início");

    Attack fatalAttack = new Attack(null, 120.0, 0.0);
    DefenseResult result = character.receiveDamage(fatalAttack);
    character.setHitPoints(character.getHitPoints() - result.getEffectiveDamage());

    assertFalse(character.isAlive(), "Não deve estar vivo após ter o HP zerado");
  }

  private static class ConcreteEntity extends Entity {
    private ConcreteEntity(
        String name, double hitPoints, int attackPower, int defense, int dexterity, int speed) {
      super(name, hitPoints, attackPower, defense, dexterity, speed);
    }

    @Override
    public Attack performAttack(Entity target) {
      return new Attack(this, this.attackPower, 0.0);
    }
  }
}
