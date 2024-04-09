package com.example.maze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class MazeView extends View {

    private Paint wallPaint;

    private MazeGenerator mMazeGenerator;

    public MazeView(Context context) {
        super(context);
        setFocusable(true);

        wallPaint = new Paint();
        wallPaint.setColor(0xFF000000);
        wallPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mMazeGenerator == null)
            return;

        int cellSize = getWidth() / mMazeGenerator.getMazeSize() - 10; // Assuming square view for simplicity

        MazeGenerator.Cell[][] maze = mMazeGenerator.getMaze();
        for (int x = 0; x < mMazeGenerator.getMazeSize(); x++) {
            for (int y = 0; y < mMazeGenerator.getMazeSize(); y++) {
                int cellX = x * cellSize;
                int cellY = y * cellSize;

                // Draw walls based on the cell's wall status
                if (maze[x][y].walls[MazeGenerator.Directions.UP.ordinal()]) {
                    canvas.drawLine(cellX, cellY, cellX + cellSize, cellY, wallPaint);
                }
                if (maze[x][y].walls[MazeGenerator.Directions.RIGHT.ordinal()]) {
                    canvas.drawLine(cellX + cellSize, cellY, cellX + cellSize, cellY + cellSize, wallPaint);
                }
                if (maze[x][y].walls[MazeGenerator.Directions.DOWN.ordinal()]) {
                    canvas.drawLine(cellX, cellY + cellSize, cellX + cellSize, cellY + cellSize, wallPaint);
                }
                if (maze[x][y].walls[MazeGenerator.Directions.LEFT.ordinal()]) {
                    canvas.drawLine(cellX, cellY, cellX, cellY + cellSize, wallPaint);
                }
            }
        }
    }

    void setMazeGenerator(MazeGenerator generator)
    {
        this.mMazeGenerator = generator;
        this.invalidate();
    }
}
