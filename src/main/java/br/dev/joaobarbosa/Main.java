package br.dev.joaobarbosa;

import br.dev.joaobarbosa.adapters.CliAdapter;
import br.dev.joaobarbosa.adapters.CsvLogPersistenceAdapter;
import br.dev.joaobarbosa.application.ports.output.LogPersistancePort;
import br.dev.joaobarbosa.application.service.GameService;

public class Main {
  public static void main(String[] args) {
    // Configuração das dependências (Adapters e Service)
    LogPersistancePort persistencePort = new CsvLogPersistenceAdapter("logs.csv");
    CliAdapter cliAdapter = new CliAdapter();
    GameService gameService = new GameService(cliAdapter, persistencePort);
    cliAdapter.setGameService(gameService);

    // Inicia o jogo
    cliAdapter.run();
  }
}
