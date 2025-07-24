package br.dev.joaobarbosa.adapters;

import br.dev.joaobarbosa.domain.AttackResult;
import br.dev.joaobarbosa.domain.logs.BattleLog;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import br.dev.joaobarbosa.domain.logs.FileLogAdapter;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileLogAdapterTest {

    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("battle_log_test", ".csv");
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    void testSaveAndLoad() throws IOException {
        Instant timestamp = Instant.parse("2025-07-23T15:00:00Z");
        BattleLogEntry entry = BattleLogEntry.of(
                "Herói",
                "Monstro",
                1,
                1,
                20,
                15,
                50,
                35,
                AttackResult.HIT,
                timestamp
        );

        BattleLog battleLog = new BattleLog(Collections.singletonList(entry));

        FileLogAdapter adapter = new FileLogAdapter(tempFile);
        adapter.save(battleLog);

        BattleLog loaded = adapter.load();
        assertNotNull(loaded);
        assertEquals(1, loaded.getEntries().size());

        BattleLogEntry loadedEntry = loaded.getEntries().get(0);

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
    void testLoadFromEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("empty", ".csv");

        // Escreve o cabeçalho no arquivo vazio (importante)
        Files.writeString(emptyFile.toPath(), BattleLogEntry.csvHeader() + System.lineSeparator());

        FileLogAdapter adapter = new FileLogAdapter(emptyFile);

        BattleLog log = adapter.load();
        assertNotNull(log);
        assertTrue(log.getEntries().isEmpty());

        emptyFile.delete();
    }


    @Test
    void testLoadFromMalformedLine() throws IOException {
        File malformedFile = File.createTempFile("malformed", ".csv");

        Files.writeString(malformedFile.toPath(), "Herói,Monstro,1");

        FileLogAdapter adapter = new FileLogAdapter(malformedFile);

        assertThrows(IOException.class, adapter::load);

        malformedFile.delete();
    }

    @Test
    void testSaveAndLoadMultipleEntries() throws IOException {
        File multiFile = File.createTempFile("multiple", ".csv");
        FileLogAdapter adapter = new FileLogAdapter(multiFile);

        BattleLogEntry entry1 = BattleLogEntry.of(
                "Herói1", "Monstro1", 1, 1, 100, 90, 120, 30, AttackResult.HIT, Instant.now());
        BattleLogEntry entry2 = BattleLogEntry.of(
                "Herói2", "Monstro2", 2, 2, 110, 85, 100, 50, AttackResult.CRITICAL_HIT, Instant.now());

        BattleLog log = new BattleLog(List.of(entry1, entry2));
        adapter.save(log);

        BattleLog loadedLog = adapter.load();
        assertEquals(2, loadedLog.getEntries().size());

        multiFile.delete();
    }
}
