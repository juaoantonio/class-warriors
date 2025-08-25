package br.dev.joaobarbosa.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.dev.joaobarbosa.adapters.InMemoryLogPersistenceAdapter;
import br.dev.joaobarbosa.application.ports.output.GameOutputPort;
import br.dev.joaobarbosa.domain.GameState;
import br.dev.joaobarbosa.domain.character.Entity;
import br.dev.joaobarbosa.domain.game.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class GameServiceTest {

  private GameService gameService;
  private GameOutputPort mockOutputPort;
  private InMemoryLogPersistenceAdapter persistenceAdapter;

  @BeforeEach
  void setUp() {
    mockOutputPort = mock(GameOutputPort.class);
    persistenceAdapter = new InMemoryLogPersistenceAdapter();
    gameService = new GameService(mockOutputPort, persistenceAdapter);
  }

  @Test
  void startGameShouldInitializeGameState() {
    gameService.startGame(2, Difficulty.EASY);
    GameState state = gameService.getGameState();

    assertNotNull(state);
    assertEquals(2, state.getHeroes().size());
    assertEquals(2, state.getMonsters().size());
    assertEquals(Difficulty.EASY, state.getDifficulty());
    assertEquals(0, state.getCurrentRound());
  }

  @Test
  void playTurnShouldIncrementRoundAndGenerateLogs() {
    gameService.startGame(1, Difficulty.EASY);
    gameService.playTurn();

    assertEquals(1, gameService.getGameState().getCurrentRound());
    assertFalse(gameService.getLastTurnLogs().isEmpty());
    assertFalse(gameService.getBattleLogs().isEmpty());

    // Verifica se o output port foi chamado com os logs
    verify(mockOutputPort, times(1)).presentRoundResult(anyList(), any(GameState.class));
  }

  @Test
  void playTurnShouldEndGameWhenAllMonstersAreDefeated() {
    gameService.startGame(1, Difficulty.EASY);
    gameService.getGameState().getMonsters().getFirst().setHitPoints(0);
    gameService.playTurn();
    ArgumentCaptor<GameState> gameStateCaptor = ArgumentCaptor.forClass(GameState.class);

    verify(mockOutputPort, times(1)).presentGameOver(gameStateCaptor.capture());

    verify(mockOutputPort, never()).presentRoundResult(anyList(), any(GameState.class));

    boolean monstersAlive =
        gameStateCaptor.getValue().getMonsters().stream().anyMatch(Entity::isAlive);
    assertFalse(monstersAlive, "Nenhum monstro deveria estar vivo no estado de fim de jogo.");
  }
}
