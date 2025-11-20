package com.qualityworkshop.terminalbattleship.game;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.strategy.ShotStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class GameConfiguration {

    private static final int BOARD_SIZE = getBoardSize();
    private static final int SHIP_COUNT = getShipCount();

    private static int getBoardSize() {
        String miniBoard = System.getProperty("mini-board");
        if ("true".equalsIgnoreCase(miniBoard)) {
            return 4;
        }
        return 6;
    }

    private static int getShipCount() {
        return BOARD_SIZE == 4 ? 2 : 3;
    }

    @Bean(name = "playerBoard")
    public Board playerBoard() {
        return Board.randomFleet(BOARD_SIZE, SHIP_COUNT);
    }

    @Bean(name = "computerBoard")
    public Board computerBoard() {
        return Board.randomFleet(BOARD_SIZE, SHIP_COUNT);
    }

    @Bean
    @Primary
    public GameEngine gameEngine(@Qualifier("playerBoard") Board playerBoard,
                                 @Qualifier("computerBoard") Board computerBoard,
                                 ShotStrategy strategy) {
        return new GameEngine(playerBoard, computerBoard, strategy);
    }
}