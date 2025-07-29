package br.dev.joaobarbosa.adapters;

import static org.junit.jupiter.api.Assertions.*;

import br.dev.joaobarbosa.application.ports.output.LogPersistancePort;
import br.dev.joaobarbosa.domain.AttackResult;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.*;

class InMemoryLogAdapterTest {

  private LogPersistancePort adapter;

  @BeforeEach
  void setup() {
    adapter = new InMemoryLogAdapter();
  }

  @Test
  void testSaveAndLoadSingleEntry() {
    Instant timestamp = Instant.parse("2025-07-23T15:00:00Z");
    BattleLogEntry entry =
            BattleLogEntry.of("Hero", "Monster", 1, 1, 20, 15, 50, 35, AttackResult.HIT, timestamp);

    adapter.saveBattleEntry(entry);

    List<BattleLogEntry> loadedEntries = adapter.getAllBattleLogs();

    assertNotNull(loadedEntries);
    assertEquals(1, loadedEntries.size());

    BattleLogEntry loadedEntry = loadedEntries.get(0);

    assertEquals(entry.getAttackerName(), loadedEntry.getAttackerName());
    assertEquals(entry.getTargetName(), loadedEntry.getTargetName());
    assertEquals(entry.getRoundNumber(), loadedEntry.getRoundNumber());
    assertEquals(entry.getTurnOrderIndex(), loadedEntry.getTurnOrderIndex());
    assertEquals(entry.getRawDamage(), loadedEntry.getRawDamage());
    assertEquals(entry.getEffectiveDamage(), loadedEntry.getEffectiveDamage());
    assertEquals(entry.getTargetHpBefore(), loadedEntry.getTargetHpBefore());
    assertEquals(entry.getTargetHpAfter(), loadedEntry.getTargetHpAfter());
    assertEquals(entry.getResult(), loadedEntry.getResult());
    assertEquals(entry.isKillingBlow(), loadedEntry.isKillingBlow());
    assertEquals(entry.getTimestamp(), loadedEntry.getTimestamp());
  }

  @Test
  void testLoadBeforeSaveReturnsEmptyList() {
    List<BattleLogEntry> loadedEntries = adapter.getAllBattleLogs();
    assertNotNull(loadedEntries);
    assertTrue(loadedEntries.isEmpty());
  }

  @Test
  void testSaveNullThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> adapter.saveBattleEntry(null));
  }
}
