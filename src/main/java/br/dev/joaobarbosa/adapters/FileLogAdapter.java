package br.dev.joaobarbosa.adapters;

import br.dev.joaobarbosa.application.ports.output.LogPersistancePort;
import br.dev.joaobarbosa.domain.AttackResult;
import br.dev.joaobarbosa.domain.logs.BattleLog;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FileLogAdapter implements LogPersistancePort {

  private final File file;

  public FileLogAdapter(File file) {
    this.file = file;
  }

  @Override
  public void saveBattleEntries(List<BattleLogEntry> logEntries) {
    try (BufferedWriter writer =
        new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

      writer.write(BattleLogEntry.csvHeader());
      writer.newLine();

      for (BattleLogEntry entry : logEntries) {
        writer.write(entry.toCsvRow());
        writer.newLine();
      }

    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar entradas de batalha", e);
    }
  }

  @Override
  public void saveBattleEntry(BattleLogEntry entry) {
    try {
      boolean writeHeader = !file.exists() || file.length() == 0;

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
        if (writeHeader) {
          writer.write(BattleLogEntry.csvHeader());
          writer.newLine();
        }
        writer.write(entry.toCsvRow()); // <- Aqui corrige de toCsv() para toCsvRow()
        writer.newLine();
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar entrada de batalha", e);
    }
  }

  @Override
  public List<BattleLogEntry> getAllBattleLogs() {
    List<BattleLogEntry> entries = new ArrayList<>();

    try (BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

      String line = reader.readLine(); // Cabeçalho
      if (line == null || !line.equals(BattleLogEntry.csvHeader())) {
        throw new IOException("Cabeçalho CSV inválido ou ausente");
      }

      while ((line = reader.readLine()) != null) {
        if (line.trim().isEmpty()) continue;
        BattleLogEntry entry = parseCsvLine(line);
        entries.add(entry);
      }

    } catch (IOException e) {
      throw new RuntimeException("Erro ao ler logs de batalha", e);
    }

    return entries;
  }

  // ============================ PARSE ============================

  private BattleLogEntry parseCsvLine(String line) throws IOException {
    List<String> tokens = parseCsvTokens(line);

    if (tokens.size() != 11) {
      throw new IOException("Linha CSV com número incorreto de campos: " + line);
    }

    Instant timestamp = Instant.parse(tokens.get(0));
    String attacker = unescape(tokens.get(1));
    String target = unescape(tokens.get(2));
    int round = Integer.parseInt(tokens.get(3));
    int turn = Integer.parseInt(tokens.get(4));
    AttackResult result = AttackResult.valueOf(tokens.get(5));
    int rawDamage = Integer.parseInt(tokens.get(6));
    int effectiveDamage = Integer.parseInt(tokens.get(7));
    int targetHpBefore = Integer.parseInt(tokens.get(8));
    int targetHpAfter = Integer.parseInt(tokens.get(9));
    boolean killingBlow = Boolean.parseBoolean(tokens.get(10));

    return BattleLogEntry.of(
        attacker,
        target,
        round,
        turn,
        rawDamage,
        effectiveDamage,
        targetHpBefore,
        targetHpAfter,
        result,
        timestamp);
  }

  private List<String> parseCsvTokens(String line) {
    List<String> tokens = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    boolean inQuotes = false;
    int i = 0;
    while (i < line.length()) {
      char c = line.charAt(i);
      if (inQuotes) {
        if (c == '"') {
          if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
            sb.append('"');
            i += 2;
          } else {
            inQuotes = false;
            i++;
          }
        } else {
          sb.append(c);
          i++;
        }
      } else {
        if (c == '"') {
          inQuotes = true;
          i++;
        } else if (c == ',') {
          tokens.add(sb.toString());
          sb.setLength(0);
          i++;
        } else {
          sb.append(c);
          i++;
        }
      }
    }
    tokens.add(sb.toString());
    return tokens;
  }

  private String unescape(String v) {
    return v;
  }

  @Override
  public void save(BattleLog logs) {

  }

  @Override
  public void append(BattleLog logs) {

  }

  @Override
  public void appendOne(BattleLogEntry log) {

  }

  @Override
  public BattleLog load() {
    return null;
  }
}
