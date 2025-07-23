package br.dev.joaobarbosa.application.ports.output;

import br.dev.joaobarbosa.domain.GameState;
import br.dev.joaobarbosa.logs.BattleLogEntry;
import java.util.List;

public interface GameOutputPort {
  void presentRoundResult(List<BattleLogEntry> battleLogEntries, GameState gameState);

  void presentGameOver(GameState gameState, String strongestHeroName);
}
