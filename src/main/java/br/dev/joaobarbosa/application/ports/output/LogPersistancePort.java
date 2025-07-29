package br.dev.joaobarbosa.application.ports.output;

import br.dev.joaobarbosa.domain.logs.BattleLog;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;

import java.util.List;

public interface LogPersistancePort {

    void saveBattleEntries(List<BattleLogEntry> logEntries);

    void saveBattleEntry(BattleLogEntry entry);

    List<BattleLogEntry> getAllBattleLogs();

    /**
   * Salva o BattleLog completo, substituindo o conteúdo existente.
   *
   * @param logs O BattleLog a ser salvo.
   */
  void save(BattleLog logs);

  /**
   * Adiciona novas entradas ao BattleLog existente.
   *
   * @param logs As novas entradas a serem adicionadas.
   */
  void append(BattleLog logs);

  /**
   * Adiciona uma única entrada ao BattleLog existente.
   *
   * @param log A entrada a ser adicionada.
   */
  void appendOne(BattleLogEntry log);

  /**
   * Carrega o BattleLog completo do armazenamento.
   *
   * @return O BattleLog carregado.
   */
  BattleLog load();
}
