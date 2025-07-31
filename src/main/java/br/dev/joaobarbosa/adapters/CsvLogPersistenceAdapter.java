package br.dev.joaobarbosa.adapters;

import br.dev.joaobarbosa.application.ports.output.LogPersistancePort;
import br.dev.joaobarbosa.domain.AttackResult;
import br.dev.joaobarbosa.domain.logs.BattleLog;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.io.*;
import java.nio.file.*;
import java.time.Instant;
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
        loadFromDisk();
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Erro ao inicializar adaptador CSV", e);
    }
  }

  @Override
  public void save(BattleLog logs) {
    try (BufferedWriter writer = Files.newBufferedWriter(file)) {
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
      reader.readLine(); // skip header
      String line;
      while ((line = reader.readLine()) != null) {
        inMemoryEntries.add(toBattleLogEntry(line));
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Erro ao carregar dados do CSV", e);
    }
  }

  private BattleLogEntry toBattleLogEntry(String line) {
    String[] parts = line.split(",", -1);
    return BattleLogEntry.of(
        unescape(parts[1]),
        unescape(parts[2]),
        Integer.parseInt(parts[3]),
        Integer.parseInt(parts[4]),
        Integer.parseInt(parts[6]),
        Integer.parseInt(parts[7]),
        Integer.parseInt(parts[8]),
        Integer.parseInt(parts[9]),
        AttackResult.valueOf(parts[5]),
        Instant.parse(parts[0]));
  }

  private String unescape(String value) {
    if (value.startsWith("\"") && value.endsWith("\"")) {
      return value.substring(1, value.length() - 1).replace("\"\"", "\"");
    }
    return value;
  }
}
