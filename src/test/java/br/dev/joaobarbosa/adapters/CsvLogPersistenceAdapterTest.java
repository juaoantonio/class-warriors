package br.dev.joaobarbosa.adapters;

import static org.junit.jupiter.api.Assertions.*;

import br.dev.joaobarbosa.domain.AttackResult;
import br.dev.joaobarbosa.domain.logs.BattleLog;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.*;

class CsvLogPersistenceAdapterTest {

  private static final String TEMP_FILE = "test-battle-log.csv";
  private CsvLogPersistenceAdapter adapter;

  @BeforeEach
  void setUp() throws IOException {
    Files.deleteIfExists(Paths.get(TEMP_FILE));
    adapter = new CsvLogPersistenceAdapter(TEMP_FILE);
  }

  @AfterEach
  void cleanUp() throws IOException {
    Files.deleteIfExists(Paths.get(TEMP_FILE));
  }

  private BattleLogEntry createSampleEntry(String attacker, String target) {
    return BattleLogEntry.of(
        attacker,
        target,
        1,
        1,
        10,
        8,
        20,
        12,
        AttackResult.HIT,
        Instant.parse("2024-01-01T12:00:00Z"));
  }

  @Test
  void testAppendDoesNotWriteToFileImmediately() throws IOException {
    adapter.appendOne(createSampleEntry("A", "B"));

    List<String> lines = Files.readAllLines(Paths.get(TEMP_FILE));
    assertEquals(1, lines.size());
    assertTrue(lines.getFirst().startsWith("timestamp"));
  }

  @Test
  void testSaveWritesAllMemoryDataToFile() throws IOException {
    BattleLogEntry entry1 = createSampleEntry("A", "B");
    BattleLogEntry entry2 = createSampleEntry("C", "D");

    adapter.appendOne(entry1);
    adapter.appendOne(entry2);
    adapter.save(new BattleLog(List.of(entry1, entry2)));

    List<String> lines = Files.readAllLines(Paths.get(TEMP_FILE));
    assertEquals(3, lines.size()); // header + 2 entries
    assertTrue(lines.get(1).contains("A"));
    assertTrue(lines.get(2).contains("C"));
  }

  @Test
  void testLoadRestoresFromDisk() {
    BattleLogEntry entry1 = createSampleEntry("A", "B");
    adapter.appendOne(entry1);
    adapter.save(new BattleLog(List.of(entry1)));

    CsvLogPersistenceAdapter reloaded = new CsvLogPersistenceAdapter(TEMP_FILE);
    BattleLog loaded = reloaded.load();

    assertEquals(1, loaded.getEntries().size());
    assertEquals("A", loaded.getEntries().getFirst().getAttackerName());
  }

  @Test
  void testLoadReturnsCurrentInMemoryState() {
    BattleLogEntry entry1 = createSampleEntry("A", "B");
    BattleLogEntry entry2 = createSampleEntry("C", "D");

    adapter.appendOne(entry1);
    adapter.appendOne(entry2);

    BattleLog log = adapter.load();
    assertEquals(2, log.getEntries().size());
    assertEquals("D", log.getEntries().get(1).getTargetName());
  }

  @Test
  void testSaveClearsAndReplacesInMemoryState() {
    adapter.appendOne(createSampleEntry("Old", "Target"));

    BattleLogEntry freshEntry = createSampleEntry("New", "Target");
    BattleLog newLog = new BattleLog(List.of(freshEntry));

    adapter.save(newLog);

    BattleLog loaded = adapter.load();
    assertEquals(1, loaded.getEntries().size());
    assertEquals("New", loaded.getEntries().getFirst().getAttackerName());
  }
}
