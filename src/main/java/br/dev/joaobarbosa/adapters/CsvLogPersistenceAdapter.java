package br.dev.joaobarbosa.adapters;

import br.dev.joaobarbosa.application.ports.output.LogPersistancePort;
import br.dev.joaobarbosa.domain.AttackResult;
import br.dev.joaobarbosa.domain.logs.BattleLog;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CsvLogPersistenceAdapter implements LogPersistancePort {

  private final Path file;
  private final List<BattleLogEntry> inMemoryEntries = new ArrayList<>();

  public CsvLogPersistenceAdapter(String path) {
    this.file = Paths.get(path);
    try {
      if (Files.notExists(file)) {
        Files.createFile(file);
        Files.writeString(file, BattleLogEntry.csvHeader() + System.lineSeparator());
      } else {
        if (Files.size(file) > BattleLogEntry.csvHeader().length()) {
          loadFromDisk();
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Erro ao inicializar adaptador CSV", e);
    }
  }

  // O método save() não precisa de alterações, pois ele já usa o toCsvRow() atualizado.
  @Override
  public void save(BattleLog logs) {
    try (BufferedWriter writer =
        Files.newBufferedWriter(
            file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
      writer.write(BattleLogEntry.csvHeader());
      writer.newLine();
      for (BattleLogEntry entry : logs.getEntries()) {
        writer.write(entry.toCsvRow());
        writer.newLine();
      }
      inMemoryEntries.clear();
      inMemoryEntries.addAll(logs.getEntries());
    } catch (IOException e) {
      throw new UncheckedIOException("Erro ao salvar no CSV", e);
    }
  }

  @Override
  public void append(BattleLog logs) {
    inMemoryEntries.addAll(logs.getEntries());
  }

  @Override
  public void appendOne(BattleLogEntry log) {
    inMemoryEntries.add(log);
  }

  @Override
  public BattleLog load() {
    return new BattleLog(new ArrayList<>(inMemoryEntries));
  }

  private void loadFromDisk() {
    try (BufferedReader reader = Files.newBufferedReader(file)) {
      reader.readLine(); // Pula o cabeçalho
      String line;
      while ((line = reader.readLine()) != null) {
        if (!line.trim().isEmpty()) {
          inMemoryEntries.add(toBattleLogEntry(line));
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Erro ao carregar dados do CSV", e);
    }
  }

  private BattleLogEntry toBattleLogEntry(String line) {
    String[] parts = line.split(",", -1);

    // Assume-se que um método .of() em BattleLogEntry aceite todos os parâmetros do CSV.
    // Se não existir, ele precisará ser criado.
    // Ex: BattleLogEntry.of(attacker, target, ..., specialAction, instant)

    // Por simplicidade, vamos chamar o .of() que já criamos e deixar que ele calcule o killingBlow
    return BattleLogEntry.of(
        unescape(parts[1]), // attacker
        unescape(parts[2]), // target
        Integer.parseInt(parts[3]), // roundNumber
        Integer.parseInt(parts[4]), // turnOrderIndex
        Double.parseDouble(parts[6]), // rawDamage (CORRIGIDO para double)
        Double.parseDouble(parts[7]), // effectiveDamage (CORRIGIDO para double)
        Double.parseDouble(parts[8]), // targetHpBefore (CORRIGIDO para double)
        Double.parseDouble(parts[9]), // targetHpAfter (CORRIGIDO para double)
        AttackResult.valueOf(parts[5]), // result
        unescape(parts[11]) // specialAction (NOVO)
        );
  }

  private String unescape(String value) {
    if (value.startsWith("\"") && value.endsWith("\"")) {
      return value.substring(1, value.length() - 1).replace("\"\"", "\"");
    }
    return value;
  }
}
