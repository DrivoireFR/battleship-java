package com.qualityworkshop.terminalbattleship;

import com.qualityworkshop.terminalbattleship.cli.GameRunner;
import com.qualityworkshop.terminalbattleship.game.GameEngine;
import com.qualityworkshop.terminalbattleship.score.ScoreService;
import com.qualityworkshop.terminalbattleship.strategy.RandomShotStrategy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // Importation nécessaire pour @Bean

@SpringBootApplication
public class TerminalBattleshipApplication implements CommandLineRunner {

    private final GameRunner gameRunner;

    // Le constructeur de TerminalBattleshipApplication est modifié pour injecter les dépendances nécessaires à GameRunner
    // et ensuite créer GameRunner manuellement via une méthode @Bean
    public TerminalBattleshipApplication(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(TerminalBattleshipApplication.class, args);
    }

    @Override
    public void run(String... args) {
        gameRunner.start();
    }

    // Méthode @Bean pour créer et configurer GameRunner avec ses dépendances
    // Spring Boot va automatiquement trouver cette méthode et l'utiliser pour créer l'instance de GameRunner
    @Bean
    public GameRunner gameRunner(GameEngine gameEngine, ScoreService scoreService, RandomShotStrategy randomShotStrategy) {
        return new GameRunner(gameEngine, scoreService, randomShotStrategy);
    }
}
