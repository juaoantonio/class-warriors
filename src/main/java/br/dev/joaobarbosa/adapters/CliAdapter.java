package br.dev.joaobarbosa.adapters;

import br.dev.joaobarbosa.application.ports.input.GameInputPort;
import br.dev.joaobarbosa.application.ports.output.GameOutputPort;
import br.dev.joaobarbosa.domain.AttackResult;
import br.dev.joaobarbosa.domain.GameState;
import br.dev.joaobarbosa.domain.game.Difficulty;
import br.dev.joaobarbosa.domain.logs.BattleLogEntry;
import java.util.List;
import java.util.Scanner;

public class CliAdapter implements Runnable, GameOutputPort {

  private final Scanner scanner = new Scanner(System.in);
  private GameInputPort gameService;

  public void setGameService(GameInputPort gameService) {
    this.gameService = gameService;
  }

  @Override
  public void run() {
    clearScreen();
    System.out.println(
        AnsiColor.BOLD + AnsiColor.GREEN + "=== Bem-vindo ao Class Warriors ===" + AnsiColor.RESET);

    System.out.print("Escolha a dificuldade (1=EASY, 2=MEDIUM, 3=HARD): ");
    int diffChoice = scanner.nextInt();
    Difficulty difficulty =
        switch (diffChoice) {
          case 2 -> Difficulty.MEDIUM;
          case 3 -> Difficulty.HARD;
          default -> Difficulty.EASY;
        };

    System.out.print("Escolha o número de heróis: ");
    int heroCount = scanner.nextInt();
    gameService.startGame(heroCount, difficulty);

    boolean playing = true;
    while (playing) {
      printGameState();
      System.out.println(
          AnsiColor.CYAN
              + "\n[1] Jogar turno\n[2] Ver histórico de batalha\n[0] Sair"
              + AnsiColor.RESET);
      System.out.print("Sua escolha: ");
      int cmd = scanner.nextInt();

      switch (cmd) {
        case 1 -> {
          gameService.playTurn();

          if (gameService.isGameOver()) {
            playing = false;
            promptEnterKey();
          } else {
            promptEnterKey();
          }
        }
        case 2 -> {
          printAllLogs();
          promptEnterKey();
        }
        case 0 -> playing = false;

        default -> System.out.println(AnsiColor.RED + "Opção inválida." + AnsiColor.RESET);
      }
    }

    gameService.endGame();
    scanner.close();
    System.out.println(AnsiColor.GREEN + "\nObrigado por jogar!" + AnsiColor.RESET);
  }

  private void printGameState() {
    clearScreen();
    GameState state = gameService.getGameState();
    if (state == null) {
      System.out.println("O jogo ainda não começou.");
      return;
    }
    System.out.println(
        AnsiColor.BOLD
            + "--- ESTADO DO JOGO (Turno "
            + state.getCurrentRound()
            + ") ---"
            + AnsiColor.RESET);

    System.out.println(AnsiColor.BLUE + "Heróis:" + AnsiColor.RESET);
    state
        .getHeroes()
        .forEach(h -> System.out.printf("  - %s (HP: %.0f)%n", h.getName(), h.getHitPoints()));

    System.out.println(AnsiColor.RED + "\nMonstros:" + AnsiColor.RESET);
    state
        .getMonsters()
        .forEach(m -> System.out.printf("  - %s (HP: %.0f)%n", m.getName(), m.getHitPoints()));
  }

  private void printAllLogs() {
    clearScreen();
    System.out.println(AnsiColor.BOLD + "--- HISTÓRICO COMPLETO DE BATALHA ---" + AnsiColor.RESET);
    gameService.getBattleLogs().forEach(this::printFormattedLogEntry);
  }

  @Override
  public void presentRoundResult(List<BattleLogEntry> turnLogs, GameState gameState) {
    System.out.println(
        AnsiColor.BOLD
            + "\n--- Resultados do Turno "
            + gameState.getCurrentRound()
            + " ---"
            + AnsiColor.RESET);
    turnLogs.forEach(this::printFormattedLogEntry);
  }

  private void printFormattedLogEntry(BattleLogEntry log) {
    String attackerColor = log.getAttackerName().contains("Herói") ? AnsiColor.BLUE : AnsiColor.RED;
    String targetColor = log.getTargetName().contains("Herói") ? AnsiColor.BLUE : AnsiColor.RED;

    String formattedAttacker = attackerColor + log.getAttackerName() + AnsiColor.RESET;
    String formattedTarget = targetColor + log.getTargetName() + AnsiColor.RESET;

    String special =
        log.getSpecialAction().isEmpty()
            ? ""
            : String.format(
                " %s[%s]%s", AnsiColor.MAGENTA, log.getSpecialAction(), AnsiColor.RESET);
    String kill =
        log.isKillingBlow()
            ? String.format(" %s[KILL]%s", AnsiColor.BOLD + AnsiColor.RED, AnsiColor.RESET)
            : "";

    if (log.getResult() == AttackResult.MISSED) {
      System.out.printf(
          "R%d#%d %s errou %s%s (HP %.0f)%n",
          log.getRoundNumber(),
          log.getTurnOrderIndex(),
          formattedAttacker,
          formattedTarget,
          special,
          log.getTargetHpBefore());
      return;
    }

    String crit =
        (log.getResult() == AttackResult.CRITICAL_HIT)
            ? String.format(" %s(CRÍTICO)%s", AnsiColor.BOLD + AnsiColor.YELLOW, AnsiColor.RESET)
            : "";
    String damage =
        String.format("%s-%.0f%s", AnsiColor.YELLOW, log.getEffectiveDamage(), AnsiColor.RESET);

    System.out.printf(
        "%dº Turno [%d] - %s atingiu %s%s%s: %s (HP %.0f → %.0f)%s%n",
        log.getRoundNumber(),
        log.getTurnOrderIndex(),
        formattedAttacker,
        formattedTarget,
        crit,
        special,
        damage,
        log.getTargetHpBefore(),
        log.getTargetHpAfter(),
        kill);
  }

  @Override
  public void presentGameOver(GameState gameState) {
    System.out.println(AnsiColor.BOLD + "\n--- FIM DE JOGO ---" + AnsiColor.RESET);
    boolean heroesWon = gameState.getHeroes().stream().anyMatch(h -> h.getHitPoints() > 0);
    if (heroesWon) {
      System.out.println(AnsiColor.GREEN + "OS HERÓIS VENCERAM!" + AnsiColor.RESET);
    } else {
      System.out.println(AnsiColor.RED + "OS MONSTROS VENCERAM!" + AnsiColor.RESET);
    }
    printGameState();
  }

  private void clearScreen() {

    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  private void promptEnterKey() {
    System.out.println(AnsiColor.CYAN + "\nPressione Enter para continuar..." + AnsiColor.RESET);
    try {
      scanner.nextLine();
      scanner.nextLine();
    } catch (Exception ignored) {

    }
  }

  private static final class AnsiColor {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";
  }
}
