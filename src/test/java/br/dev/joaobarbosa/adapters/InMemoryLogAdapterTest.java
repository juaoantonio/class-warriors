package br.dev.joaobarbosa.adapters;

import br.dev.joaobarbosa.domain.AttackResult;
import br.dev.joaobarbosa.domain.logs.BattleLog;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import br.dev.joaobarbosa.domain.logs.InMemoryLogAdapter;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryLogAdapterTest {

    private InMemoryLogAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new InMemoryLogAdapter();
    }

    @Test
    void testSaveAndLoad() throws IOException {
        Instant timestamp = Instant.parse("2025-07-23T15:00:00Z");
        BattleLogEntry entry = BattleLogEntry.of(
                "Hero",
                "Monster",
                1,
                1,
                20,
                15,
                50,
                35,
                AttackResult.HIT,
                timestamp
        );

        BattleLog log = new BattleLog(Collections.singletonList(entry));
        adapter.save(log);

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
    void testLoadBeforeSaveReturnsEmptyLog() throws IOException {
        BattleLog loaded = adapter.load();
        assertNotNull(loaded);
        assertTrue(loaded.getEntries().isEmpty());
    }

    @Test
    void testSaveNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> adapter.save(null));
    }
}
