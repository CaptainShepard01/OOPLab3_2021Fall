package com.example.lab3.maze;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.LayoutInflater;

import com.example.lab3.maze.model.Cell;
import com.example.lab3.maze.model.Direction;

/**
 * Class to paint walls and cells, as well as Player and Exit.
 */
public class MazePainter {
    private Canvas canvas;
    private final int ROWS, COLS;
    private final float WALL_THICKNESS = 7;
    private float cellSize;
    private int hMargin;
    private int vMargin;
    int BGCOLOR = Color.parseColor("#A3A395");

    MazeManager builder;

    public MazePainter(int cols, int rows) {
        this.COLS = cols;
        this.ROWS = rows;

        builder = new MazeManager(cols, rows);
        builder.generateMaze();
    }

    public MazeManager getBuilder() {
        return builder;
    }

    public void paint(Canvas canvas, Paint wallPaint, Paint playerPaint, Paint exitPaint) {
        this.canvas = canvas;
        this.canvas.drawColor(BGCOLOR);
        this.cellSize = calculateCellSize();

        addMarginsToCanvas();

        drawWalls(wallPaint);
        drawExit(exitPaint);
        drawPlayer(playerPaint);
    }

    /**
     * Calculate cell size depending on maze parameters.
     *
     * @return Float (length and width of a cell).
     */
    private float calculateCellSize() {
        float width = canvas.getWidth();
        float height = canvas.getHeight();

        return width / height < COLS / ROWS ? height / (ROWS + 1) : width / (COLS + 1);
    }

    private void addMarginsToCanvas() {
        float width = canvas.getWidth();
        float height = canvas.getHeight();

        this.hMargin = (int) ((width - COLS * cellSize) / 2);
        this.vMargin = (int) ((height - ROWS * cellSize) / 2);

        canvas.translate(hMargin, vMargin);
    }

    private void drawWalls(Paint wallPaint) {
        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                if (builder.hasRightWall(x, y))
                    canvas.drawLine((x + 1) * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, wallPaint);

                if (builder.hasLeftWall(x, y))
                    canvas.drawLine(x * cellSize, y * cellSize, x * cellSize, (y + 1) * cellSize, wallPaint);

                if (builder.hasTopWall(x, y))
                    canvas.drawLine(x * cellSize, y * cellSize, (x + 1) * cellSize, y * cellSize, wallPaint);

                if (builder.hasBottomWall(x, y))
                    canvas.drawLine(x * cellSize, (y + 1) * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, wallPaint);
            }
        }
    }

    private void drawPlayer(Paint playerPaint) {
        Cell player = builder.getPlayerPosition();
        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(WALL_THICKNESS + 1);

        canvas.drawCircle(
                (float) (player.col + 0.5) * cellSize,
                (float) (player.row + 0.5) * cellSize,
                cellSize / 3,
                playerPaint);

        canvas.drawCircle(
                (float) (player.col + 0.5) * cellSize,
                (float) (player.row + 0.5) * cellSize,
                cellSize / 3,
                borderPaint);
    }

    private void drawExit(Paint exitPaint) {
        float margin = cellSize / 10;
        Cell exit = builder.getExitPosition();
        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(WALL_THICKNESS + 1);

        Paint fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setTextSize(60);
        fontPaint.setStyle(Paint.Style.FILL);
        fontPaint.setColor(Color.WHITE);

        canvas.drawRect(
                exit.col * cellSize + margin,
                exit.row * cellSize + margin,
                (exit.col + 1) * cellSize - margin,
                (exit.row + 1) * cellSize - margin,
                exitPaint);
        canvas.drawRect(
                exit.col * cellSize + margin,
                exit.row * cellSize + margin,
                (exit.col + 1) * cellSize - margin,
                (exit.row + 1) * cellSize - margin,
                borderPaint);

        canvas.drawText("Exit", (exit.col + 0.17f) * cellSize, (exit.row + 0.62f) * cellSize, fontPaint);
    }

    /**
     * Method implementing player movements through adding or subtracting 1 position
     * of its current position in corresponding direction.
     *
     * @param direction
     */
    private void movePlayer(Direction direction) {
        switch (direction) {
            case UP:
                builder.movePlayerUp();
                break;
            case DOWN:
                builder.movePlayerDown();
                break;
            case LEFT:
                builder.movePlayerLeft();
                break;
            case RIGHT:
                builder.movePlayerRight();
                break;
        }
    }

    /**
     * Move player to the position checking walls.
     *
     * @param x
     * @param y
     */
    public void movePlayerTowards(float x, float y) {
        float playerCenterX = hMargin + (builder.getPlayerPosition().col + 0.5f) * cellSize;
        float playerCenterY = vMargin + (builder.getPlayerPosition().row + 0.5f) * cellSize;

        float dx = x - playerCenterX;
        float dy = y - playerCenterY;

        float absDX = Math.abs(dx);
        float absDY = Math.abs(dy);

        if (absDX > cellSize || absDY > cellSize) {
            if (absDX > absDY) {
                //move in x-direction
                if (dx > 0) {
                    movePlayer(Direction.RIGHT);
                } else {
                    movePlayer(Direction.LEFT);
                }
            } else {
                //move in y-direction
                if (dy > 0) {
                    movePlayer(Direction.DOWN);
                } else {
                    movePlayer(Direction.UP);
                }
            }
        }
    }

    /**
     * Check if player is on exit cell.
     *
     * @return
     */
    public boolean checkExit() {
        return builder.getPlayerPosition() == builder.getExitPosition();
    }
}
