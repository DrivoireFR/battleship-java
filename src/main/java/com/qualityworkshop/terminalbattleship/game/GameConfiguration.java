package com.qualityworkshop.terminalbattleship.game;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.strategy.ShotStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class GameConfiguration {

    @Value("${game.board.size:6}")
    private int boardSize;

    @Value("${game.ship.count:3}")
    private int shipCount;

    @Bean(name = "playerBoard")
    public Board playerBoard() {
        return Board.randomFleet(boardSize, shipCount);
    }

    @Bean(name = "computerBoard")
    public Board computerBoard() {
        return Board.randomFleet(boardSize, shipCount);
    }

    @Bean
    @Primary
    public GameEngine gameEngine(@Qualifier("playerBoard") Board playerBoard,
                                 @Qualifier("computerBoard") Board computerBoard,
                                 ShotStrategy strategy) {
        return new GameEngine(playerBoard, computerBoard, strategy, boardSize);
    }

    @Bean(name = "boardSize")
    public int boardSize() {
        return boardSize;
    }
}