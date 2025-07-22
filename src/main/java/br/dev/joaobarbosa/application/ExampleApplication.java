package br.dev.joaobarbosa.application;

public class ExampleApplication {
    private final OutputPort outputPort;

    public ExampleApplication( OutputPort outputPort) {
        this.outputPort = outputPort;
    }

    public void executarTurno(String logMessage) {
        outputPort.printMessage("Executing turn with log message: " + logMessage);

    }
}
