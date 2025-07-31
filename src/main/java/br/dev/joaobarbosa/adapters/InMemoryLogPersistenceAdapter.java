package br.dev.joaobarbosa.adapters;

import br.dev.joaobarbosa.application.ports.output.LogPersistancePort;
import br.dev.joaobarbosa.domain.logs.BattleLog;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.util.ArrayList;
import java.util.List;

public class InMemoryLogPersistenceAdapter implements LogPersistancePort {

  private final List<BattleLogEntry> inMemoryEntries = new ArrayList<>();

  @Override
  public void save(BattleLog logs) {
    inMemoryEntries.clear();
    inMemoryEntries.addAll(logs.getEntries());
  }

  @Override
  public void append(BattleLog logs) {
    inMemoryEntries.addAll(logs.getEntries());
  }

  @Override
  public void appendOne(BattleLogEntry log) {
    inMemoryEntries.add(log);
  }

  @Override
  public BattleLog load() {
    return new BattleLog(new ArrayList<>(inMemoryEntries));
  }
}
