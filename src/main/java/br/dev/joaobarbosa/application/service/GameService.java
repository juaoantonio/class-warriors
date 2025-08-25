package br.dev.joaobarbosa.application.service;

import br.dev.joaobarbosa.application.ports.input.GameInputPort;
import br.dev.joaobarbosa.application.ports.output.GameOutputPort;
import br.dev.joaobarbosa.application.ports.output.LogPersistancePort;
import br.dev.joaobarbosa.domain.GameState;
import br.dev.joaobarbosa.domain.battle.TurnEngine;
import br.dev.joaobarbosa.domain.character.Entity;
import br.dev.joaobarbosa.domain.character.hero.Hero;
import br.dev.joaobarbosa.domain.character.hero.HeroFactory;
import br.dev.joaobarbosa.domain.character.monster.Monster;
import br.dev.joaobarbosa.domain.character.monster.MonsterFactory;
import br.dev.joaobarbosa.domain.game.Difficulty;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class GameService implements GameInputPort {
  private final GameOutputPort gameOutputPort;
  private final LogPersistancePort logPersistancePort;
  private final TurnEngine turnEngine;
  private GameState gameState;
  private List<BattleLogEntry> lastTurnLogs = new ArrayList<>();

  public GameService(GameOutputPort gameOutputPort, LogPersistancePort logPersistancePort) {
    this.gameOutputPort = gameOutputPort;
    this.logPersistancePort = logPersistancePort;
    this.turnEngine = new TurnEngine();
  }

  @Override
  public void startGame(int heroCount, Difficulty difficulty) {
    List<Hero> heroes = new ArrayList<>();
    IntStream.range(0, heroCount)
        .forEach(
            i -> {
              Hero hero = HeroFactory.createRandomHero("Herói " + (i + 1));
              hero.setName(hero.getName() + " (" + hero.getClass().getSimpleName() + ")");
              heroes.add(hero);
            });

    int monsterCount = (int) Math.round(heroCount * difficulty.getMonsterCountFactor());
    monsterCount = Math.max(1, monsterCount); // Garante que pelo menos 1 monstro seja criado

    System.out.printf("O desafio será contra %d monstro(s).%n", monsterCount);

    List<Monster> monsters = new ArrayList<>();
    IntStream.range(0, monsterCount)
        .forEach(
            i -> {
              Monster monster =
                  MonsterFactory.createRandomMonster("Monstro " + (i + 1), difficulty);
              monster.setName(monster.getName() + " (" + monster.getClass().getSimpleName() + ")");
              monsters.add(monster);
            });

    this.gameState = new GameState(heroes, monsters, difficulty);
  }

  @Override
  public void playTurn() {
    if (isGameOver()) {
      gameOutputPort.presentGameOver(gameState);
      return;
    }

    gameState.incrementRound();
    lastTurnLogs =
        turnEngine.executeTurn(
            gameState.getHeroes(), gameState.getMonsters(), gameState.getCurrentRound());

    lastTurnLogs.forEach(logPersistancePort::appendOne);
    gameOutputPort.presentRoundResult(lastTurnLogs, gameState);

    if (isGameOver()) {
      gameOutputPort.presentGameOver(gameState);
    }
  }

  @Override
  public void endGame() {
    System.out.println("Salvando histórico de batalha...");
    this.logPersistancePort.save(this.logPersistancePort.load());
    System.out.println("Jogo finalizado. Histórico de batalha salvo com sucesso!");
  }

  @Override
  public GameState getGameState() {
    return this.gameState;
  }

  @Override
  public List<BattleLogEntry> getBattleLogs() {
    return logPersistancePort.load().getEntries();
  }

  @Override
  public List<BattleLogEntry> getLastTurnLogs() {
    return this.lastTurnLogs;
  }

  @Override
  public boolean isGameOver() {
    if (gameState == null) return false;
    boolean heroesAlive = gameState.getHeroes().stream().anyMatch(Entity::isAlive);
    boolean monstersAlive = gameState.getMonsters().stream().anyMatch(Entity::isAlive);
    return !heroesAlive || !monstersAlive;
  }
}
