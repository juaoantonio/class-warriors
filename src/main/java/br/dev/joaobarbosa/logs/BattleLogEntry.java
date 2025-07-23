package br.dev.joaobarbosa.logs;

import br.dev.joaobarbosa.domain.AttackResult;
import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * Representa **um único evento de combate**.
 * <p>Campos registrados:
 * <ul>
 *   <li>attackerName / targetName – nomes capturados no momento do ataque</li>
 *   <li>roundNumber – rodada da batalha (começa em 1)</li>
 *   <li>turnOrderIndex – posição do atacante na ordem daquele round (0‑based ou 1‑based, você escolhe; usamos 1‑based)</li>
 *   <li>rawDamage – dano "bruto" retornado por performAttack()</li>
 *   <li>effectiveDamage – dano após defesa aplicada</li>
 *   <li>targetHpBefore / targetHpAfter – HP do alvo antes/depois do ataque</li>
 *   <li>result – MISSED, HIT, CRITICAL_HIT</li>
 *   <li>killingBlow – true se o ataque reduziu HP do alvo a 0</li>
 *   <li>timestamp – instante em que o evento foi registrado</li>
 * </ul>
 */
@Getter
public final class BattleLogEntry {

    private static final DateTimeFormatter TS_FMT = DateTimeFormatter
            .ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT)
            .withZone(ZoneId.of("UTC"));

    private final String attackerName;
    private final String targetName;
    private final int roundNumber;
    private final int turnOrderIndex; // 1‑based dentro da rodada
    private final int rawDamage;
    private final int effectiveDamage;
    private final int targetHpBefore;
    private final int targetHpAfter;
    private final AttackResult result;
    private final boolean killingBlow;
    private final Instant timestamp;

    private BattleLogEntry(String attackerName,
                           String targetName,
                           int roundNumber,
                           int turnOrderIndex,
                           int rawDamage,
                           int effectiveDamage,
                           int targetHpBefore,
                           int targetHpAfter,
                           AttackResult result,
                           boolean killingBlow,
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
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp");
    }

    /**
     * Cria um log a partir de Entities e valores já calculados pelo motor.
     * <p>Importante: o motor deve capturar HP do alvo <b>antes</b> de aplicar dano
     * e informar o HP final (já atualizado) para registrar corretamente.</p>
     */
    public static BattleLogEntry of(String attacker,
                                    String target,
                                    int roundNumber,
                                    int turnOrderIndex,
                                    int rawDamage,
                                    int effectiveDamage,
                                    int targetHpBefore,
                                    int targetHpAfter,
                                    AttackResult result,
                                    Instant instant) {
        Instant timestamp = (instant != null) ? instant : Instant.now();

        boolean kill = (result != AttackResult.MISSED) && targetHpAfter <= 0 && targetHpBefore > 0;
        return new BattleLogEntry(attacker,
                target,
                roundNumber,
                turnOrderIndex,
                rawDamage,
                effectiveDamage,
                targetHpBefore,
                targetHpAfter,
                result,
                kill,
                timestamp);
    }

    public static BattleLogEntry of(String attacker,
                                    String target,
                                    int roundNumber,
                                    int turnOrderIndex,
                                    int rawDamage,
                                    int effectiveDamage,
                                    int targetHpBefore,
                                    int targetHpAfter,
                                    AttackResult result) {

        boolean kill = (result != AttackResult.MISSED) && targetHpAfter <= 0 && targetHpBefore > 0;
        return new BattleLogEntry(attacker,
                target,
                roundNumber,
                turnOrderIndex,
                rawDamage,
                effectiveDamage,
                targetHpBefore,
                targetHpAfter,
                result,
                kill,
                Instant.now());
    }


    public String toHumanReadable() {
        if (result == AttackResult.MISSED) {
            return String.format(Locale.ROOT,
                    "R%d#%d %s errou %s (HP %d)",
                    roundNumber, turnOrderIndex, attackerName, targetName, targetHpBefore);
        }
        String crit = (result == AttackResult.CRITICAL_HIT) ? " (CRÍTICO)" : "";
        String kill = killingBlow ? " [KILL]" : "";
        return String.format(Locale.ROOT,
                "%dº Turno [%d] - %s atingiu %s%s: -%d (HP %d → %d)%s",
                roundNumber, turnOrderIndex, attackerName, targetName, crit,
                effectiveDamage, targetHpBefore, targetHpAfter, kill);
    }

    /** Linha CSV pronta para persistência. */
    public String toCsvRow() {
        return String.join(",",
                TS_FMT.format(timestamp),
                escape(attackerName),
                escape(targetName),
                String.valueOf(roundNumber),
                String.valueOf(turnOrderIndex),
                result.name(),
                String.valueOf(rawDamage),
                String.valueOf(effectiveDamage),
                String.valueOf(targetHpBefore),
                String.valueOf(targetHpAfter),
                String.valueOf(killingBlow));
    }

    // Cabeçalho CSV (para adapters de arquivo).
    public static String csvHeader() {
        return "timestamp,attacker,target,round,turn,result,rawDamage,effectiveDamage,targetHpBefore,targetHpAfter,killingBlow";
    }

    // ---------------------------------------------------------------------
    // Interno: escaping simples de vírgulas e aspas.
    // ---------------------------------------------------------------------
    private static String escape(String v) {
        if (v == null) return "";
        if (v.contains(",") || v.contains("\"")) {
            return '"' + v.replace("\"", "\"\"") + '"';
        }
        return v;
    }

    @Override
    public String toString() {
        return toHumanReadable();
    }
}
