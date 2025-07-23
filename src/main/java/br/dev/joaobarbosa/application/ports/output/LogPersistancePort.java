package br.dev.joaobarbosa.application.ports.output;

import br.dev.joaobarbosa.logs.BattleLogEntry;
import java.util.List;

public interface LogPersistancePort {
  void saveBattleEntry(BattleLogEntry battleLogEntry);

  List<BattleLogEntry> getAllBattleLogs();
}
