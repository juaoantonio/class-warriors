// src/main/java/br/dev/joaobarbosa/domain/logs/BattleLogEntry.java
package br.dev.joaobarbosa.domain.logs;

import br.dev.joaobarbosa.domain.AttackResult;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import lombok.Getter;

@Getter
public final class BattleLogEntry {

  private static final DateTimeFormatter TS_FMT =
      DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT)
          .withZone(ZoneId.of("UTC"));

  private final String attackerName;
  private final String targetName;
  private final int roundNumber;
  private final int turnOrderIndex;
  private final double rawDamage;
  private final double effectiveDamage;
  private final double targetHpBefore;
  private final double targetHpAfter;
  private final AttackResult result;
  private final boolean killingBlow;
  private final String specialAction;
  private final Instant timestamp;

  private BattleLogEntry(
      String attackerName,
      String targetName,
      int roundNumber,
      int turnOrderIndex,
      double rawDamage,
      double effectiveDamage,
      double targetHpBefore,
      double targetHpAfter,
      AttackResult result,
      boolean killingBlow,
      String specialAction,
      Instant timestamp) {
    this.attackerName = attackerName;
    this.targetName = targetName;
    this.roundNumber = roundNumber;
    this.turnOrderIndex = turnOrderIndex;
    this.rawDamage = rawDamage;
    this.effectiveDamage = effectiveDamage;
    this.targetHpBefore = targetHpBefore;
    this.targetHpAfter = targetHpAfter;
    this.result = Objects.requireNonNull(result, "result");
    this.killingBlow = killingBlow;
    this.specialAction = specialAction; // NOVO CAMPO
    this.timestamp = Objects.requireNonNull(timestamp, "timestamp");
  }

  public static BattleLogEntry of(
      String attacker,
      String target,
      int roundNumber,
      int turnOrderIndex,
      double rawDamage,
      double effectiveDamage,
      double targetHpBefore,
      double targetHpAfter,
      AttackResult result,
      String specialAction) {

    boolean kill = (result != AttackResult.MISSED) && targetHpAfter <= 0 && targetHpBefore > 0;
    return new BattleLogEntry(
        attacker,
        target,
        roundNumber,
        turnOrderIndex,
        rawDamage,
        effectiveDamage,
        targetHpBefore,
        targetHpAfter,
        result,
        kill,
        specialAction,
        Instant.now());
  }

  public static String csvHeader() {
    return "timestamp,attacker,target,round,turn,result,rawDamage,effectiveDamage,targetHpBefore,targetHpAfter,killingBlow,specialAction";
  }

  private static String escape(String v) {
    if (v == null) return "";
    if (v.contains(",") || v.contains("\"")) {
      return '"' + v.replace("\"", "\"\"") + '"';
    }
    return v;
  }

  public String toHumanReadable() {
    String special = specialAction.isEmpty() ? "" : String.format(" [%s]", specialAction);

    if (result == AttackResult.MISSED) {
      return String.format(
          Locale.ROOT,
          "R%d#%d %s errou %s%s (HP %.0f)",
          roundNumber,
          turnOrderIndex,
          attackerName,
          targetName,
          special,
          targetHpBefore);
    }
    String crit = (result == AttackResult.CRITICAL_HIT) ? " (CRÍTICO)" : "";
    String kill = killingBlow ? " [KILL]" : "";
    return String.format(
        Locale.ROOT,
        "%dº Turno [%d] - %s atingiu %s%s%s: -%.0f (HP %.0f → %.0f)%s",
        roundNumber,
        turnOrderIndex,
        attackerName,
        targetName,
        crit,
        special,
        effectiveDamage,
        targetHpBefore,
        targetHpAfter,
        kill);
  }

  public String toCsvRow() {
    return String.join(
        ",",
        TS_FMT.format(timestamp),
        escape(attackerName),
        escape(targetName),
        String.valueOf(roundNumber),
        String.valueOf(turnOrderIndex),
        result.name(),
        String.format(Locale.ROOT, "%.2f", rawDamage), // Formata para double
        String.format(Locale.ROOT, "%.2f", effectiveDamage), // Formata para double
        String.format(Locale.ROOT, "%.2f", targetHpBefore), // Formata para double
        String.format(Locale.ROOT, "%.2f", targetHpAfter), // Formata para double
        String.valueOf(killingBlow),
        escape(specialAction) // Adiciona o novo campo
        );
  }

  @Override
  public String toString() {
    return toHumanReadable();
  }
}
