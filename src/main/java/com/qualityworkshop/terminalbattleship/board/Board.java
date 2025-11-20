package com.qualityworkshop.terminalbattleship.board;

import com.qualityworkshop.terminalbattleship.game.ShotOutcome;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Board {

    private final int size;
    private final CellState[][] grid;
    private final Set<Coordinate> shipCells;

    private Board(int size) {
        this.size = size;
        this.grid = new CellState[size][size];
        this.shipCells = new HashSet<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col] = CellState.FOG;
            }
        }
    }

    public static Board withShips(int size, List<Coordinate> ships) {
        Board board = new Board(size);
        ships.forEach(board::placeShip);
        return board;
    }

    public static Board randomFleet(int size, int shipCount) {
        return randomFleet(size, shipCount, new SecureRandom());
    }

    public static Board randomFleet(int size, int shipCount, Random random) {
        Board board = new Board(size);
        List<Coordinate> pool = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                pool.add(new Coordinate(row, col));
            }
        }
        Collections.shuffle(pool, random);
        pool.stream().limit(shipCount).forEach(board::placeShip);
        return board;
    }

    public ShotOutcome shoot(Coordinate coordinate) {
        if (!isInside(coordinate)) {
            return ShotOutcome.INVALID;
        }
        CellState current = grid[coordinate.row()][coordinate.column()];
        if (current == CellState.HIT || current == CellState.MISS) {
            return ShotOutcome.ALREADY_TARGETED;
        }
        if (shipCells.contains(coordinate)) {
            grid[coordinate.row()][coordinate.column()] = CellState.HIT;
            shipCells.remove(coordinate);
            return shipCells.isEmpty() ? ShotOutcome.ALL_SUNK : ShotOutcome.HIT;
        }
        grid[coordinate.row()][coordinate.column()] = CellState.MISS;
        return ShotOutcome.MISS;
    }

    public CellState cellAt(Coordinate coordinate) {
        if (!isInside(coordinate)) {
            throw new IllegalArgumentException("Coordinate outside board");
        }
        return grid[coordinate.row()][coordinate.column()];
    }

    public boolean hasRemainingShips() {
        return !shipCells.isEmpty();
    }

    public List<Coordinate> untargetedCells() {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                CellState state = grid[row][col];
                if (state != CellState.HIT && state != CellState.MISS) {
                    coordinates.add(new Coordinate(row, col));
                }
            }
        }
        return coordinates;
    }

    public int size() {
        return size;
    }

    private void placeShip(Coordinate coordinate) {
        if (!isInside(coordinate)) {
            throw new IllegalArgumentException("Ship outside board");
        }
        if (shipCells.contains(coordinate)) {
            throw new IllegalArgumentException("Ship already placed at " + coordinate);
        }
        shipCells.add(coordinate);
        grid[coordinate.row()][coordinate.column()] = CellState.SHIP;
    }

    private boolean isInside(Coordinate coordinate) {
        return coordinate.row() >= 0 && coordinate.row() < size && coordinate.column() >= 0 && coordinate.column() < size;
    }
}
