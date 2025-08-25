package br.dev.joaobarbosa.domain.battle;

import br.dev.joaobarbosa.domain.AttackResult;
import br.dev.joaobarbosa.domain.character.Entity;
import br.dev.joaobarbosa.domain.character.hero.Hero;
import br.dev.joaobarbosa.domain.character.monster.Monster;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TurnEngine {
  private static final Random RANDOM = new Random();
  private static final double CRITICAL_HIT_CHANCE = 0.1;

  public List<Entity> getTurnOrder(List<Hero> heroes, List<Monster> monsters) {
    return Stream.concat(heroes.stream(), monsters.stream())
        .filter(Entity::isAlive)
        .sorted(Comparator.comparingInt(Entity::getSpeed).reversed())
        .collect(Collectors.toList());
  }

  public List<BattleLogEntry> executeTurn(
      List<Hero> heroes, List<Monster> monsters, int roundNumber) {
    List<BattleLogEntry> turnLogs = new ArrayList<>();
    List<Entity> turnOrder = getTurnOrder(heroes, monsters);

    for (int i = 0; i < turnOrder.size(); i++) {
      Entity attacker = turnOrder.get(i);
      if (!attacker.isAlive()) continue;

      Entity target = chooseTarget(attacker, heroes, monsters);
      if (target == null) continue;

      double hpBefore = target.getHitPoints();
      AttackResult result = resolveAttack(attacker, target);
      Attack attack = attacker.performAttack(target);
      double rawDamage = attack.getBaseDamage();

      DefenseResult defenseResult;

      if (result != AttackResult.MISSED) {
        if (result == AttackResult.CRITICAL_HIT) {
          attack.setBaseDamage(attack.getBaseDamage() * 2);
          rawDamage *= 2;
        }

        defenseResult = target.receiveDamage(attack);

        target.setHitPoints(Math.max(0, hpBefore - defenseResult.getEffectiveDamage()));

      } else {

        defenseResult = new DefenseResult(0);
      }

      double hpAfter = target.getHitPoints();

      if (result == AttackResult.HIT
          && defenseResult.getEffectiveDamage() == 0
          && !defenseResult.getSpecialAction().isEmpty()) {
        result = AttackResult.MISSED;
      }

      turnLogs.add(
          BattleLogEntry.of(
              attacker.getName(),
              target.getName(),
              roundNumber,
              i + 1,
              rawDamage,
              defenseResult.getEffectiveDamage(),
              hpBefore,
              hpAfter,
              result,
              defenseResult.getSpecialAction()));
    }
    return turnLogs;
  }

  private Entity chooseTarget(Entity attacker, List<Hero> heroes, List<Monster> monsters) {
    if (attacker instanceof Hero) {
      List<Monster> aliveMonsters = monsters.stream().filter(Entity::isAlive).toList();
      if (aliveMonsters.isEmpty()) return null;
      return aliveMonsters.get(RANDOM.nextInt(aliveMonsters.size()));
    } else if (attacker instanceof Monster) {
      return ((Monster) attacker).decideTarget(heroes);
    }
    return null;
  }

  private AttackResult resolveAttack(Entity attacker, Entity target) {
    double hitChance =
        (double) attacker.getDexterity() / (attacker.getDexterity() + target.getDexterity());
    double roll = RANDOM.nextDouble();
    if (roll > hitChance) {
      return AttackResult.MISSED;
    }
    return (RANDOM.nextDouble() < CRITICAL_HIT_CHANCE)
        ? AttackResult.CRITICAL_HIT
        : AttackResult.HIT;
  }
}
