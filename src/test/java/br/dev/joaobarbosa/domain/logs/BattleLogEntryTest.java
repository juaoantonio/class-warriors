package br.dev.joaobarbosa.domain.logs;

import static org.junit.jupiter.api.Assertions.*;

import br.dev.joaobarbosa.domain.AttackResult;
import org.junit.jupiter.api.Test;

class BattleLogEntryTest {

  // Testes do método de criação de BattleLogEntry .of()
  @Test
  void shouldCreateBattleLogEntryWithAllParameters() {
    BattleLogEntry entry =
        BattleLogEntry.of(
            "Attacker", "Target", 1, 1, 10.0, 8.0, 20.0, 12.0, AttackResult.HIT, "BLOQUEOU");

    assertEquals("Attacker", entry.getAttackerName());
    assertEquals("Target", entry.getTargetName());
    assertEquals(1, entry.getRoundNumber());
    assertEquals(1, entry.getTurnOrderIndex());
    assertEquals(10.0, entry.getRawDamage());
    assertEquals(8.0, entry.getEffectiveDamage());
    assertEquals(20.0, entry.getTargetHpBefore());
    assertEquals(12.0, entry.getTargetHpAfter());
    assertEquals(AttackResult.HIT, entry.getResult());
    assertEquals("BLOQUEOU", entry.getSpecialAction());
    assertFalse(entry.isKillingBlow());
    assertNotNull(entry.getTimestamp());
  }

  @Test
  void shouldBeKillingBlow_WhenTargetHpAfterIsZero() {
    BattleLogEntry entry =
        BattleLogEntry.of("Attacker", "Target", 1, 1, 10.0, 10.0, 10.0, 0.0, AttackResult.HIT, "");

    assertTrue(entry.isKillingBlow());
  }

  @Test
  void shouldNotBeKillingBlow_WhenAttackResultIsMissed() {
    BattleLogEntry entry =
        BattleLogEntry.of(
            "Attacker", "Target", 1, 1, 0.0, 0.0, 10.0, 10.0, AttackResult.MISSED, "");

    assertFalse(entry.isKillingBlow());
  }

  // Testes do método toHumanReadable()
  @Test
  void shouldReturnHumanReadableStringForHit() {
    BattleLogEntry entry =
        BattleLogEntry.of("Attacker", "Target", 1, 1, 10.0, 8.0, 20.0, 12.0, AttackResult.HIT, "");

    String expected = "1º Turno [1] - Attacker atingiu Target: -8 (HP 20 → 12)";
    assertEquals(expected, entry.toHumanReadable());
  }

  @Test
  void shouldReturnHumanReadableStringWithSpecialAction() {
    BattleLogEntry entry =
        BattleLogEntry.of(
            "Attacker", "Target", 1, 1, 10.0, 5.0, 20.0, 15.0, AttackResult.HIT, "BLOQUEOU");

    String expected = "1º Turno [1] - Attacker atingiu Target [BLOQUEOU]: -5 (HP 20 → 15)";
    assertEquals(expected, entry.toHumanReadable());
  }

  @Test
  void shouldReturnHumanReadableStringForCriticalHit() {
    BattleLogEntry entry =
        BattleLogEntry.of(
            "Attacker", "Target", 1, 1, 20.0, 18.0, 20.0, 2.0, AttackResult.CRITICAL_HIT, "");

    String expected = "1º Turno [1] - Attacker atingiu Target (CRÍTICO): -18 (HP 20 → 2)";
    assertEquals(expected, entry.toHumanReadable());
  }

  @Test
  void shouldReturnHumanReadableStringForMissedAttackWithSpecial() {
    BattleLogEntry entry =
        BattleLogEntry.of(
            "Attacker", "Target", 1, 1, 0.0, 0.0, 20.0, 20.0, AttackResult.MISSED, "ESQUIVOU");

    String expected = "R1#1 Attacker errou Target [ESQUIVOU] (HP 20)";
    assertEquals(expected, entry.toHumanReadable());
  }

  @Test
  void shouldReturnHumanReadableStringForKillingBlow() {
    BattleLogEntry entry =
        BattleLogEntry.of("Attacker", "Target", 1, 1, 10.0, 10.0, 10.0, 0.0, AttackResult.HIT, "");

    String expected = "1º Turno [1] - Attacker atingiu Target: -10 (HP 10 → 0) [KILL]";
    assertEquals(expected, entry.toHumanReadable());
  }

  // Testes para o método csvHeader
  @Test
  void shouldReturnCsvHeader() {
    String expectedHeader =
        "timestamp,attacker,target,round,turn,result,rawDamage,effectiveDamage,targetHpBefore,targetHpAfter,killingBlow,specialAction";
    assertEquals(expectedHeader, BattleLogEntry.csvHeader());
  }

  // Testes para o método toCsvRow
  @Test
  void shouldReturnCsvRow() {

    BattleLogEntry entry =
        BattleLogEntry.of(
            "Attacker", "Target", 1, 1, 10.0, 8.0, 20.0, 12.0, AttackResult.HIT, "SPECIAL");

    String expectedEnd = ",Attacker,Target,1,1,HIT,10.00,8.00,20.00,12.00,false,SPECIAL";
    assertTrue(entry.toCsvRow().endsWith(expectedEnd));
  }
}
