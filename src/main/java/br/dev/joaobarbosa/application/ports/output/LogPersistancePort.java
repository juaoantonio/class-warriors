package br.dev.joaobarbosa.application.ports.output;

import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.util.List;

public interface LogPersistancePort {
  void saveBattleEntries(List<BattleLogEntry> log);

  void saveBattleEntry(BattleLogEntry log);

  List<BattleLogEntry> getAllBattleLogs();
}
