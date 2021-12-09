package com.example.lab3;

import com.example.lab3.maze.Maze;
import com.example.lab3.maze.MazeManager;

import org.junit.Assert;
import org.junit.Test;

public class MazeTest {

    @Test
    public void testDimensions(){
        Maze maze = new Maze(5, 5);
        Assert.assertEquals(maze.getCOLS(), 5);
        Assert.assertEquals(maze.getROWS(), 5);
    }

    @Test
    public void testMazeCells(){
        MazeManager maze = new MazeManager(5, 5);
        Assert.assertEquals(maze.getPlayerPosition().col, 0);
        Assert.assertEquals(maze.getPlayerPosition().row, 0);
        Assert.assertEquals(maze.getExitPosition().col, 4);
        Assert.assertEquals(maze.getExitPosition().row, 4);
    }
}
