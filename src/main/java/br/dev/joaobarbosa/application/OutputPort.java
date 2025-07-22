package br.dev.joaobarbosa.application;

public interface OutputPort {
    void printMessage(String message);
    void printError(String errorMessage);
    void printWarning(String warningMessage);
    void printInfo(String infoMessage);
}
