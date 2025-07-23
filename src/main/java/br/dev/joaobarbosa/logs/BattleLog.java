package br.dev.joaobarbosa.logs;

import java.util.List;

public class BattleLog {
  private final List<BattleLogEntry> entries;

  public BattleLog(List<BattleLogEntry> entries) {
    this.entries = entries;
  }

  public void addEntry(BattleLogEntry entry) {
    entries.add(entry);
  }

  public List<BattleLogEntry> getEntries() {
    return List.copyOf(entries);
  }

  public void print() {
    entries.forEach(System.out::println);
  }
}
