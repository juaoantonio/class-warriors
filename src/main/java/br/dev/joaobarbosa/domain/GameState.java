package br.dev.joaobarbosa.domain;

import br.dev.joaobarbosa.domain.character.hero.Hero;
import br.dev.joaobarbosa.domain.character.monster.Monster;
import br.dev.joaobarbosa.domain.game.Difficulty;
import java.util.List;
import lombok.Getter;

@Getter
public class GameState {
  private final List<Hero> heroes;
  private final List<Monster> monsters;
  private final Difficulty difficulty;
  private int currentRound = 0;

  public GameState(List<Hero> heroes, List<Monster> monsters, Difficulty difficulty) {
    this.heroes = heroes;
    this.monsters = monsters;
    this.difficulty = difficulty;
  }

  public void incrementRound() {
    this.currentRound++;
  }
}
