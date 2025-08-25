package br.dev.joaobarbosa.adapters;

import static org.junit.jupiter.api.Assertions.*;

import br.dev.joaobarbosa.domain.AttackResult;
import br.dev.joaobarbosa.domain.logs.BattleLog;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryLogPersistenceAdapterTest {

  private InMemoryLogPersistenceAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new InMemoryLogPersistenceAdapter();
  }

  /** Método auxiliar corrigido para usar a nova assinatura de BattleLogEntry.of() */
  private BattleLogEntry createEntry(String attacker, String target) {
    return BattleLogEntry.of(
        attacker,
        target,
        1,
        1,
        10.0, // CORRIGIDO: Agora é double
        8.0, // CORRIGIDO: Agora é double
        20.0, // CORRIGIDO: Agora é double
        12.0, // CORRIGIDO: Agora é double
        AttackResult.HIT,
        "" // NOVO: Adicionado specialAction (vazio para este teste)
        );
  }

  @Test
  void testAppendOneAddsEntryToMemory() {
    BattleLogEntry entry = createEntry("A", "B");

    adapter.appendOne(entry);

    BattleLog loaded = adapter.load();
    assertEquals(1, loaded.getEntries().size());
    assertEquals("A", loaded.getEntries().getFirst().getAttackerName());
  }

  @Test
  void testAppendAddsMultipleEntries() {
    BattleLogEntry e1 = createEntry("A", "B");
    BattleLogEntry e2 = createEntry("C", "D");

    adapter.append(new BattleLog(List.of(e1, e2)));

    BattleLog loaded = adapter.load();
    assertEquals(2, loaded.getEntries().size());
    assertEquals("C", loaded.getEntries().get(1).getAttackerName());
  }

  @Test
  void testSaveClearsPreviousEntries() {
    BattleLogEntry oldEntry = createEntry("Old", "Target");
    adapter.appendOne(oldEntry);

    BattleLogEntry newEntry = createEntry("New", "Target");
    adapter.save(new BattleLog(List.of(newEntry)));

    BattleLog loaded = adapter.load();
    assertEquals(1, loaded.getEntries().size());
    assertEquals("New", loaded.getEntries().getFirst().getAttackerName());
  }

  @Test
  void testLoadReturnsCopy() {
    BattleLogEntry entry = createEntry("A", "B");
    adapter.appendOne(entry);

    BattleLog loaded1 = adapter.load();
    BattleLog loaded2 = adapter.load();

    assertNotSame(loaded1.getEntries(), loaded2.getEntries()); // defensive copy
  }

  @Test
  void testEntriesAreInInsertionOrder() {
    BattleLogEntry e1 = createEntry("A", "B");
    BattleLogEntry e2 = createEntry("C", "D");

    adapter.appendOne(e1);
    adapter.appendOne(e2);

    List<BattleLogEntry> list = adapter.load().getEntries();
    assertEquals("A", list.get(0).getAttackerName());
    assertEquals("C", list.get(1).getAttackerName());
  }
}
