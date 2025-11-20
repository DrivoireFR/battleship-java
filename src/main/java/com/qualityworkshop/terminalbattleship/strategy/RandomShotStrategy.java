package com.qualityworkshop.terminalbattleship.strategy;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Component
public class RandomShotStrategy implements ShotStrategy {

    private final Random random;

    public RandomShotStrategy() {
        this(new SecureRandom());
    }

    public RandomShotStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Coordinate pickTarget(Board opponentBoard) {
        List<Coordinate> candidates = opponentBoard.untargetedCells();
        if (candidates.isEmpty()) {
            throw new IllegalStateException("No remaining targets");
        }
        return candidates.get(random.nextInt(candidates.size()));
    }
}
