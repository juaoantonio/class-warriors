package br.dev.joaobarbosa.domain.logs;

import java.util.List;

public record BattleLog(List<BattleLogEntry> entries) {

  public List<BattleLogEntry> entries() {
    return List.copyOf(entries);
  }
}
