package br.dev.joaobarbosa.adapters;
import br.dev.joaobarbosa.domain.AttackResult;
import br.dev.joaobarbosa.domain.logs.BattleLog;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FileLogAdapter {

    private final File file;

    public FileLogAdapter(File file) {
        this.file = file;
    }

    /**
     * Salva a lista de entradas do BattleLog em formato CSV no arquivo.
     */
    public void save(BattleLog battleLog) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            writer.write(BattleLogEntry.csvHeader());
            writer.newLine();

            // Escreve cada linha do log
            for (BattleLogEntry entry : battleLog.getEntries()) {
                writer.write(entry.toCsvRow());
                writer.newLine();
            }
        }
    }

    /**
     * Lê o arquivo CSV e retorna uma instância de BattleLog.
     */
    public BattleLog load() throws IOException {
        List<BattleLogEntry> entries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // lê o cabeçalho e ignora
            if (line == null || !line.equals(BattleLogEntry.csvHeader())) {
                throw new IOException("CSV header inválido ou arquivo vazio");
            }

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                BattleLogEntry entry = parseCsvLine(line);
                entries.add(entry);
            }
        }

        return new BattleLog(entries);
    }

    /**
     * Converte uma linha CSV em BattleLogEntry.
     */
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


        BattleLogEntry entry = BattleLogEntry.of(
                attacker,
                target,
                round,
                turn,
                rawDamage,
                effectiveDamage,
                targetHpBefore,
                targetHpAfter,
                result,
                timestamp
        );

        return entry;
    }

    private List<String> parseCsvTokens(String line) throws IOException {
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
                        continue;
                    } else {
                        inQuotes = false;
                        i++;
                        continue;
                    }
                } else {
                    sb.append(c);
                    i++;
                    continue;
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                    i++;
                    continue;
                } else if (c == ',') {
                    tokens.add(sb.toString());
                    sb.setLength(0);
                    i++;
                    continue;
                } else {
                    sb.append(c);
                    i++;
                    continue;
                }
            }
        }
        tokens.add(sb.toString());
        return tokens;
    }

    private String unescape(String v) {

        return v;
    }
}
