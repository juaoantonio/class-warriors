package br.dev.joaobarbosa.adapters;

import br.dev.joaobarbosa.application.ports.output.LogPersistancePort;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;

import java.util.ArrayList;
import java.util.List;

/** Adaptador que mantém o BattleLog apenas na memória. */
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
}
