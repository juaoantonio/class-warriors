package br.dev.joaobarbosa.domain.character.hero;

import java.util.List;
import java.util.Random;

public class HeroFactory {

  private static final List<String> HERO_TYPES = List.of("warrior", "mage", "archer", "rogue");
  private static final Random random = new Random();

  public static Hero createHero(String type, String name) {
    return switch (type.toLowerCase()) {
      case "warrior" -> new Warrior(name);
      case "mage" -> new Mage(name);
      case "archer" -> new Archer(name);
      case "rogue" -> new Rogue(name);
      default -> throw new IllegalArgumentException("Tipo de herói desconhecido: " + type);
    };
  }

  public static Hero createRandomHero(String name) {
    String randomType = HERO_TYPES.get(random.nextInt(HERO_TYPES.size()));
    return createHero(randomType, name);
  }
}
