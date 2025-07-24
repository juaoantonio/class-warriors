package br.dev.joaobarbosa.adapters;

import br.dev.joaobarbosa.domain.logs.BattleLog;
import java.io.IOException;
import java.util.Collections;

/** Adaptador que mantém o BattleLog apenas na memória. */
public class InMemoryLogAdapter {

  private BattleLog storedLog;

  public InMemoryLogAdapter() {

    this.storedLog = new BattleLog(Collections.emptyList());
  }

  public void save(BattleLog log) throws IOException {
    if (log == null) {
      throw new IllegalArgumentException("log cannot be null");
    }
    this.storedLog = log;
  }

  public BattleLog load() throws IOException {
    return storedLog;
  }
}
