package br.dev.joaobarbosa.domain.character;

import static org.junit.jupiter.api.Assertions.*;

import br.dev.joaobarbosa.domain.character.hero.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HeroFactoryTest {

  @DisplayName("Deve criar o tipo correto de herói com o nome correto")
  @ParameterizedTest
  @CsvSource({
    "warrior, Aragorn, Warrior",
    "mage, Gandalf, Mage",
    "archer, Legolas, Archer",
    "rogue, Bilbo, Rogue"
  })
  void testCreateHero_SpecificTypes(String type, String name, String expectedClass) {
    Hero createdHero = HeroFactory.createHero(type, name);

    assertNotNull(createdHero, "O herói criado não deve ser nulo.");
    assertEquals(name, createdHero.getName(), "O nome do herói deve ser o fornecido.");
    assertEquals(
        expectedClass,
        createdHero.getClass().getSimpleName(),
        "O tipo de classe do herói deve corresponder ao esperado.");
  }

  @Test
  @DisplayName("Deve lançar exceção para tipo de herói desconhecido")
  void testCreateHero_UnknownType() {
    String unknownType = "bard";
    String name = "Jaskier";

    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              HeroFactory.createHero(unknownType, name);
            });

    assertTrue(
        exception.getMessage().contains(unknownType),
        "A mensagem de erro deve conter o tipo desconhecido.");
  }

  @Test
  @DisplayName("Deve criar um herói aleatório válido")
  void testCreateRandomHero() {
    String name = "Random Hero";

    Hero randomHero = HeroFactory.createRandomHero(name);

    assertNotNull(randomHero, "O herói aleatório não deve ser nulo.");
    assertEquals(name, randomHero.getName(), "O nome do herói aleatório deve ser o fornecido.");
    assertTrue(
        randomHero instanceof Warrior
            || randomHero instanceof Mage
            || randomHero instanceof Archer
            || randomHero instanceof Rogue,
        "O herói aleatório deve ser de um tipo válido.");
  }
}
