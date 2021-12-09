package com.example.lab3.maze;

import com.example.lab3.maze.model.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Maze {

    private final Cell[][] allCells;

    private final int COLS;
    private final int ROWS;

    public Maze(int columns, int rows){
        this.COLS = columns;
        this.ROWS = rows;

        this.allCells = new Cell[COLS][ROWS];
        for(int x = 0; x < COLS; x++){
            for(int y = 0; y < ROWS; y++){
                allCells[x][y] = new Cell(x, y);
            }
        }
    }

    public int getCOLS() {
        return COLS;
    }

    public int getROWS() {
        return ROWS;
    }

    public List<Cell> getCellNeighbours(Cell cell){
        List<Cell> neighbours = new ArrayList<>();
        neighbours.add(getLeftNeighbour(cell));
        neighbours.add(getRightNeighbour(cell));
        neighbours.add(getTopNeighbour(cell));
        neighbours.add(getBottomNeighbour(cell));
        return neighbours.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Cell getLeftNeighbour(Cell cell){
        if(cell.col > 0)
            return allCells[cell.col - 1][cell.row];
        else return null;
    }

    private Cell getRightNeighbour(Cell cell){
        if(cell.col < COLS-1)
            return allCells[cell.col + 1][cell.row];
        else return null;
    }

    private Cell getTopNeighbour(Cell cell){
        if(cell.row > 0)
            return allCells[cell.col][cell.row - 1];
        else return null;
    }

    private Cell getBottomNeighbour(Cell cell){
        if(cell.row < ROWS-1)
            return allCells[cell.col][cell.row + 1];
        else return null;
    }

    public Cell getCell(int col, int row){
        return allCells[col][row];
    }
}
