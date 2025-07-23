package br.dev.joaobarbosa.application.ports.input;

import br.dev.joaobarbosa.domain.GameState;
import br.dev.joaobarbosa.logs.BattleLogEntry;

import java.util.List;

public interface QueryInputPort {
  GameState getGameState();

  String getStrongestHeroName();

  List<BattleLogEntry> getBattleLogs();
}
