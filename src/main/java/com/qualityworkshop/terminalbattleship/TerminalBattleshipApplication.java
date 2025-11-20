package com.qualityworkshop.terminalbattleship;

import com.qualityworkshop.terminalbattleship.cli.GameRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TerminalBattleshipApplication implements CommandLineRunner {

    private final GameRunner gameRunner;

    public TerminalBattleshipApplication(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(TerminalBattleshipApplication.class, args);
    }

    @Value("${app.disableRunner:false}")
    private boolean disableRunner;

    @Override
    public void run(String... args) {
        gameRunner.setQuietMode(false);
        for (String arg : args) {
            if ("--quiet".equals(arg)) {
                gameRunner.setQuietMode(true);
                break;
            }
        }

        if (!disableRunner) {
            gameRunner.start();
        }
    }

}
