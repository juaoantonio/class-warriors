package br.dev.joaobarbosa.adapters;

import br.dev.joaobarbosa.domain.AttackResult;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileLogAdapterTest {

  private File tempFile;
  private FileLogAdapter adapter;

  @BeforeEach
  void setUp() throws IOException {
    tempFile = File.createTempFile("battle-log", ".csv");
    tempFile.deleteOnExit(); // limpa depois
    adapter = new FileLogAdapter(tempFile);
  }

  @Test
  void testSaveAndLoadSingleEntry() {
    BattleLogEntry entry = BattleLogEntry.of(
            "Hero", "Monster", 1, 0, 10, 8, 100, 92,
            AttackResult.HIT,
            Instant.now()
    );

    adapter.saveBattleEntry(entry);

    List<BattleLogEntry> entries = adapter.getAllBattleLogs();

    assertEquals(1, entries.size());
    BattleLogEntry loaded = entries.get(0);

    assertEquals(entry.getAttackerName(), loaded.getAttackerName());
    assertEquals(entry.getTargetName(), loaded.getTargetName());
    assertEquals(entry.getResult(), loaded.getResult());
  }
}
