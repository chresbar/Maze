package com.example.maze;

import java.util.Arrays;
import java.util.Collections;

public class MazeGenerator {

    public enum Directions {
        UP, RIGHT, DOWN, LEFT
    }
    public static class Cell {
        boolean[] walls = {true, true, true, true}; // Up, Right, Down, Left
        boolean visited = false;
    }

    private int mSize;
    private Cell[][] mMaze;

    public MazeGenerator(int size) {
        this.mSize = size;
        this.mMaze = new Cell[mSize][mSize];

        for (int x = 0; x < mSize; ++x)
            for (int y = 0; y < mSize; ++y)
                this.mMaze[x][y] = new Cell();

        generateMaze(0, 0);
    }

    public void generateMaze(int x, int y)
    {
        Cell current = mMaze[x][y];
        current.visited = true;

        Cell[] neighbors = new Cell[4];
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0}; // Up, Right, Down, Left
        Directions[] directions = {Directions.UP, Directions.RIGHT, Directions.DOWN, Directions.LEFT};

        Collections.shuffle(Arrays.asList(directions));
        for (Directions direction : directions) {
            int nx = x + dx[direction.ordinal()];
            int ny = y + dy[direction.ordinal()];

            if (nx >= 0 && nx < mSize && ny >= 0 && ny < mSize && !mMaze[nx][ny].visited) {
                // Remove walls between current cell and the chosen cell
                current.walls[direction.ordinal()] = false;
                mMaze[nx][ny].walls[(direction.ordinal() + 2) % 4] = false;
                generateMaze(nx, ny);
            }
        }
    }

    public int getMazeSize() {
        return mSize;
    }

    public Cell[][] getMaze() {
        return mMaze;
    }
}
