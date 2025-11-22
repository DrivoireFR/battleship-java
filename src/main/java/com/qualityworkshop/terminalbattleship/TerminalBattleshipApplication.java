package com.qualityworkshop.terminalbattleship;

import com.qualityworkshop.terminalbattleship.cli.GameRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class TerminalBattleshipApplication implements CommandLineRunner {

    private final GameRunner gameRunner;

    public TerminalBattleshipApplication(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(TerminalBattleshipApplication.class, args);
    }

    public void run(String... args) throws IOException {
        gameRunner.setExportGame(false);

        for (String arg : args) {
            if ("--export".equals(arg)) {
                gameRunner.setExportGame(true);
                break;
            }
        }

        gameRunner.start();
    }
}
