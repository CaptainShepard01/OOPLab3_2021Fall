package com.example.lab3.maze;

import com.example.lab3.maze.model.Cell;

import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

import lombok.Getter;

/**
 * Class managing all the cells and walls of the maze.
 */
public class MazeManager {

    private final Maze maze;
    private Cell playerCell;
    private final Cell exitCell;
    private final float WALL_THICKNESS = 4;

    private static Random random = new Random();

    public MazeManager(int columns, int rows) {
        maze = new Maze(columns, rows);
        random = new Random();

        playerCell = maze.getCell(0, 0);
        exitCell = maze.getCell(columns - 1, rows - 1);
    }

    /**
     * Generate maze so there is a way from top left to top right cell.
     */
    public void generateMaze() {
        Stack<Cell> passage = new Stack<>();
        Cell current, next;

        current = maze.getCell(0, 0);
        current.visited = true;

        do {
            next = getNextRandomWall(current);
            if (next != null) {
                makePassage(current, next);
                passage.push(current);
                current = next;
                current.visited = true;
            } else {
                current = passage.pop();
            }
        } while (!passage.empty());

    }

    private Cell getNextRandomWall(Cell cell) {
        List<Cell> neighbours = maze.getCellNeighbours(cell).stream()
                .filter(c -> !c.visited).collect(Collectors.toList());

        if (neighbours.size() > 0) {
            int index = random.nextInt(neighbours.size());
            return neighbours.get(index);
        }
        return null;
    }

    public static void makePassage(Cell current, Cell next) {
        //check relative positions of current and next to avoid spawning wall between them
        if (current.col == next.col && current.row == next.row + 1) {
            current.topWall = false;
            next.bottomWall = false;
        } else if (current.col == next.col && current.row == next.row - 1) {
            current.bottomWall = false;
            next.topWall = false;
        } else if (current.col == next.col + 1 && current.row == next.row) {
            current.leftWall = false;
            next.rightWall = false;
        } else if (current.col == next.col - 1 && current.row == next.row) {
            current.rightWall = false;
            next.leftWall = false;
        }
    }

    public boolean hasRightWall(int x, int y) {
        return maze.getCell(x, y).rightWall;
    }

    public boolean hasLeftWall(int x, int y) {
        return maze.getCell(x, y).leftWall;
    }

    public boolean hasTopWall(int x, int y) {
        return maze.getCell(x, y).topWall;
    }

    public boolean hasBottomWall(int x, int y) {
        return maze.getCell(x, y).bottomWall;
    }

    public Cell getPlayerPosition() {
        return playerCell;
    }

    public Cell getExitPosition() {
        return exitCell;
    }

    /**
     * moving player cell up
     */
    public void movePlayerUp() {
        if (!playerCell.topWall)
            playerCell = maze.getCell(playerCell.col, playerCell.row - 1);
    }

    /**
     * moving player cell down
     */
    public void movePlayerDown() {
        if (!playerCell.bottomWall)
            playerCell = maze.getCell(playerCell.col, playerCell.row + 1);
    }

    /**
     * moving player cell right
     */
    public void movePlayerRight() {
        if (!playerCell.rightWall)
            playerCell = maze.getCell(playerCell.col + 1, playerCell.row);
    }

    /**
     * moving player cell left
     */
    public void movePlayerLeft() {
        if (!playerCell.leftWall)
            playerCell = maze.getCell(playerCell.col - 1, playerCell.row);
    }
}
