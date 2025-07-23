package br.dev.joaobarbosa.application.ports.input;

import br.dev.joaobarbosa.domain.GameState;
import br.dev.joaobarbosa.logs.BattleLogEntry;
import java.util.List;

public interface GameInputPort {
  void startGame();

  void playTurn();

  void endGame();

  GameState getGameState();

  public List<BattleLogEntry> getBattleLogs();

  public List<BattleLogEntry> getLastTurnLogs();
}
