package br.dev.joaobarbosa.application;

public interface LogPersistancePort {
    void saveLog(String logMessage);

    String getLogById(String logId);

    void deleteLog(String logId);
}
