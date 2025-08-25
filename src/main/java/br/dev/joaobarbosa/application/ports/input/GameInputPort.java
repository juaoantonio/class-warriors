package br.dev.joaobarbosa.application.ports.input;

import br.dev.joaobarbosa.domain.GameState;
import br.dev.joaobarbosa.domain.game.Difficulty;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.util.List;

public interface GameInputPort {
  void startGame(int heroCount, Difficulty difficulty);

  void playTurn();

  void endGame();

  GameState getGameState();

  List<BattleLogEntry> getBattleLogs();

  List<BattleLogEntry> getLastTurnLogs();

  boolean isGameOver();
}
