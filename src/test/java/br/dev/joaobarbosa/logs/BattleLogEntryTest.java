package br.dev.joaobarbosa.logs;

import static org.junit.jupiter.api.Assertions.*;

import br.dev.joaobarbosa.domain.AttackResult;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class BattleLogEntryTest {

  // Testes do método de criação de BattleLogEntry .of()
  @Test
  void shouldCreateBattleLogEntryWithOfMethod() {
    BattleLogEntry entry =
        BattleLogEntry.of(
            "Attacker",
            "Target",
            1,
            1,
            10,
            8,
            20,
            12,
            AttackResult.HIT,
            Instant.parse("2023-10-01T12:00:00Z"));

    assertEquals("Attacker", entry.getAttackerName());
    assertEquals("Target", entry.getTargetName());
    assertEquals(1, entry.getRoundNumber());
    assertEquals(1, entry.getTurnOrderIndex());
    assertEquals(10, entry.getRawDamage());
    assertEquals(8, entry.getEffectiveDamage());
    assertEquals(20, entry.getTargetHpBefore());
    assertEquals(12, entry.getTargetHpAfter());
    assertEquals(AttackResult.HIT, entry.getResult());
    assertFalse(entry.isKillingBlow());
    assertEquals("2023-10-01T12:00:00Z", entry.getTimestamp().toString());
  }

  @Test
  void shouldBeKillingBlow_WhenTargetHpAfterIsZero() {
    BattleLogEntry entry =
        BattleLogEntry.of(
            "Attacker",
            "Target",
            1,
            1,
            10,
            10,
            10,
            0,
            AttackResult.HIT,
            Instant.parse("2023-10-01T12:00:00Z"));

    assertTrue(entry.isKillingBlow());
  }

  @Test
  void shouldNotBeKillingBlow_WhenAttackResultIsMissed() {
    BattleLogEntry entry =
        BattleLogEntry.of(
            "Attacker",
            "Target",
            1,
            1,
            0,
            0,
            10,
            10,
            AttackResult.MISSED,
            Instant.parse("2023-10-01T12:00:00Z"));

    assertFalse(entry.isKillingBlow());
  }

  @Test
  void shouldCreateBattleLogWithoutInstant() {
    BattleLogEntry entry =
        BattleLogEntry.of("Attacker", "Target", 1, 1, 10, 8, 20, 12, AttackResult.HIT);

    assertEquals("Attacker", entry.getAttackerName());
    assertEquals("Target", entry.getTargetName());
    assertEquals(1, entry.getRoundNumber());
    assertEquals(1, entry.getTurnOrderIndex());
    assertEquals(10, entry.getRawDamage());
    assertEquals(8, entry.getEffectiveDamage());
    assertEquals(20, entry.getTargetHpBefore());
    assertEquals(12, entry.getTargetHpAfter());
    assertEquals(AttackResult.HIT, entry.getResult());
    assertFalse(entry.isKillingBlow());
    assertNotNull(entry.getTimestamp());
  }

  // Testes do método toHumanReadable()
  @Test
  void shouldReturnHumanReadableStringForHit() {
    BattleLogEntry entry =
        BattleLogEntry.of("Attacker", "Target", 1, 1, 10, 8, 20, 12, AttackResult.HIT);

    String expected = "1º Turno [1] - Attacker atingiu Target: -8 (HP 20 → 12)";
    assertEquals(expected, entry.toHumanReadable());
  }

  @Test
  void shouldReturnHumanReadableStringForCriticalHit() {
    BattleLogEntry entry =
        BattleLogEntry.of("Attacker", "Target", 1, 1, 10, 8, 20, 12, AttackResult.CRITICAL_HIT);

    String expected = "1º Turno [1] - Attacker atingiu Target (CRÍTICO): -8 (HP 20 → 12)";
    assertEquals(expected, entry.toHumanReadable());
  }

  @Test
  void shouldReturnHumanReadableStringForMissedAttack() {
    BattleLogEntry entry =
        BattleLogEntry.of("Attacker", "Target", 1, 1, 0, 0, 20, 20, AttackResult.MISSED);

    String expected = "R1#1 Attacker errou Target (HP 20)";
    assertEquals(expected, entry.toHumanReadable());
  }

  @Test
  void shouldReturnHumanReadableStringForKillingBlow() {
    BattleLogEntry entry =
        BattleLogEntry.of("Attacker", "Target", 1, 1, 10, 10, 10, 0, AttackResult.HIT);

    String expected = "1º Turno [1] - Attacker atingiu Target: -10 (HP 10 → 0) [KILL]";
    assertEquals(expected, entry.toHumanReadable());
  }

  // Testes para o método csvHeader
  @Test
  void shouldReturnCsvHeader() {
    String expectedHeader =
        "timestamp,attacker,target,round,turn,result,rawDamage,effectiveDamage,targetHpBefore,targetHpAfter,killingBlow";
    assertEquals(expectedHeader, BattleLogEntry.csvHeader());
  }

  // Testes para o método toCsvRow
  @Test
  void shouldReturnCsvRow() {
    BattleLogEntry entry =
        BattleLogEntry.of(
            "Attacker",
            "Target",
            1,
            1,
            10,
            8,
            20,
            12,
            AttackResult.HIT,
            Instant.parse("2023-10-01T12:00:00Z"));

    String expectedCsv = "2023-10-01T12:00:00Z,Attacker,Target,1,1,HIT,10,8,20,12,false";
    assertEquals(expectedCsv, entry.toCsvRow());
  }

  @Test
  void shouldEscapeCsvValues() {
    BattleLogEntry entry =
        BattleLogEntry.of(
            "Attacker, with comma",
            "Target \"with quotes\"",
            1,
            1,
            10,
            8,
            20,
            12,
            AttackResult.HIT,
            Instant.parse("2023-10-01T12:00:00Z"));

    String expectedCsv =
        "2023-10-01T12:00:00Z,\"Attacker, with comma\",\"Target \"\"with quotes\"\"\",1,1,HIT,10,8,20,12,false";
    assertEquals(expectedCsv, entry.toCsvRow());
  }
}