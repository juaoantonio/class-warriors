package br.dev.joaobarbosa.domain.character;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Classe de teste para a classe abstrata Entity, usando uma implementação "dummy".
class EntityTest {

  // Uma implementação concreta de Entity para ser usada nos testes.
  private static class ConcreteEntity extends Entity {
    private ConcreteEntity(
        String name, int hitPoints, int attackPower, int defense, int dexterity, int speed) {
      super(name, hitPoints, attackPower, defense, dexterity, speed);
    }

    // Não precisamos de uma lógica real aqui para os testes de receiveDamage.
    @Override
    public int performAttack(Entity target) {
      return this.attackPower;
    }
  }

  private Entity character;

  // Cria uma instância da nossa entidade de teste antes de cada teste, para cada teste ser
  // independente.
  @BeforeEach
  void setUp() {
    character = new ConcreteEntity("Dummy", 100, 10, 20, 5, 5);
  }

  // Teste de dano normal
  @Test
  @DisplayName("Deve receber dano normal quando o ataque é maior que a defesa")
  void testReceiveDamage_NormalDamage() {
    int rawDamage = 30; // Dano bruto maior que a defesa (20)

    character.receiveDamage(rawDamage);

    // Dano esperado = 30 (dano) - 20 (defesa) = 10
    // HP esperado = 100 (inicial) - 10 (dano causado) = 90
    assertEquals(90, character.getHitPoints(), "HP deve ser reduzido pelo dano líquido");
  }

  // Teste de dano insuficiente
  @Test
  @DisplayName("Não deve receber dano quando a defesa é maior ou igual ao ataque")
  void testReceiveDamage_BlockedByDefense() {
    int rawDamage = 15; // Dano bruto menor que a defesa (20)

    character.receiveDamage(rawDamage);

    // Dano esperado = 15 (dano) - 20 (defesa) = -5, que se torna 0.
    // HP esperado = 100 (inicial) - 0 = 100
    assertEquals(
        100, character.getHitPoints(), "HP não deve mudar se o dano for totalmente defendido");
  }

  // Teste de dano fatal
  @Test
  @DisplayName("HP deve ser 0 após receber dano fatal")
  void testReceiveDamage_Overkill() {
    int rawDamage = 150; // Dano bruto muito alto

    character.receiveDamage(rawDamage);

    // Dano esperado = 150 - 20 = 130
    // HP esperado = 100 - 130 = -30, que se torna 0.
    assertEquals(0, character.getHitPoints(), "HP deve ser 0 após receber dano fatal");
    assertFalse(character.isAlive(), "Personagem não deve estar vivo após dano fatal");
  }

  // Teste de morte após um turno
  @Test
  @DisplayName("Deve retornar true quando HP > 0 e false quando HP = 0")
  void testIsAlive() {
    assertTrue(character.isAlive(), "Deve estar vivo no início");

    character.receiveDamage(120); // Dano suficiente para zerar o HP

    assertFalse(character.isAlive(), "Não deve estar vivo após ter o HP zerado");
  }
}
