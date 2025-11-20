package com.qualityworkshop.terminalbattleship;

import com.qualityworkshop.terminalbattleship.cli.GameRunner;
import com.qualityworkshop.terminalbattleship.config.GameConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class TerminalBattleshipApplication implements CommandLineRunner {

    private final GameRunner gameRunner;

    public TerminalBattleshipApplication(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(TerminalBattleshipApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (Arrays.asList(args).contains("--quiet")) {
            GameConfig.QUIET_MODE = true;
        }
        gameRunner.start();
    }
}
