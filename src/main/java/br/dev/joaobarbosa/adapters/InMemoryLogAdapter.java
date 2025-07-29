package br.dev.joaobarbosa.adapters;

import br.dev.joaobarbosa.application.ports.output.LogPersistancePort;
import br.dev.joaobarbosa.domain.logs.BattleLog;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.util.ArrayList;
import java.util.List;

public class InMemoryLogAdapter implements LogPersistancePort {

  private final List<BattleLogEntry> storedEntries = new ArrayList<>();

  @Override
  public void saveBattleEntries(List<BattleLogEntry> log) {
    if (log == null) throw new IllegalArgumentException("log cannot be null");
    storedEntries.clear();
    storedEntries.addAll(log);
  }

  @Override
  public void saveBattleEntry(BattleLogEntry log) {
    if (log == null) throw new IllegalArgumentException("log cannot be null");
    storedEntries.add(log);
  }

  @Override
  public List<BattleLogEntry> getAllBattleLogs() {
    return new ArrayList<>(storedEntries); // retorna cópia para evitar modificação externa
  }

  @Override
  public void save(BattleLog logs) {}

  @Override
  public void append(BattleLog logs) {}

  @Override
  public void appendOne(BattleLogEntry log) {}

  @Override
  public BattleLog load() {
    return null;
  }
}
