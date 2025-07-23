package br.dev.joaobarbosa.application.ports.output;

import br.dev.joaobarbosa.domain.GameState;
import br.dev.joaobarbosa.logs.BattleLogEntry;
import java.util.List;

/**
 * Porta de saída única que:
 * 1) Notifica eventos do jogo (rounds e game over).
 * 2) Permite consultas do estado atual e dos logs.
 */
public interface GameOutputPort {
  void presentRoundResult(List<BattleLogEntry> battleLogEntries, GameState gameState);

  void presentGameOver(GameState gameState, String strongestHeroName);

  GameState getGameState();

  String getStrongestHeroName();

  List<BattleLogEntry> getBattleLogs();
}
