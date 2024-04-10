package com.example.maze;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MazeView extends View {

    private Paint wallPaint;
    private MazeGenerator mMazeGenerator = null;

    private ArrayList<Point> mPath;

    public MazeView(Context context) {
        super(context);
        setFocusable(true);

        wallPaint = new Paint();
        wallPaint.setColor(0xFF000000);
        wallPaint.setStrokeWidth(5);

        mPath = new ArrayList<Point>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mMazeGenerator == null)
            return;

        int cellSize = getWidth() / mMazeGenerator.getMazeSize(); // Assuming square view for simplicity

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

        for (Point point: mPath) {
            canvas.drawCircle((point.x + 0.5f) * cellSize, (point.y + 0.5f) * cellSize, cellSize / 4f, wallPaint);
        }
    }

    void setMazeGenerator(MazeGenerator generator)
    {
        this.mMazeGenerator = generator;
        mPath.clear();
        mPath.add(new Point(0, 0));
        this.invalidate();
    }

    void addPointToPath(float x, float y)
    {
        if (mPath.isEmpty())
            return;

        Point lastPoint = new Point(mPath.get(mPath.size() - 1));
        Point newPoint = new Point(lastPoint);

        if (Math.abs(x) > Math.abs(y)) {
            newPoint.x += x > 0 ? 1 : -1;
        } else {
            newPoint.y += y > 0 ? 1 : -1;
        }

        newPoint.x = Math.max(0, Math.min(newPoint.x, mMazeGenerator.getMazeSize() - 1));
        newPoint.y = Math.max(0, Math.min(newPoint.y, mMazeGenerator.getMazeSize() - 1));

        if (!mPath.contains(newPoint) && isMoveAllowed(lastPoint, newPoint))
            mPath.add(newPoint);
        this.invalidate();
    }

    private boolean isMoveAllowed(Point from, Point to) {
        MazeGenerator.Cell[][] maze = mMazeGenerator.getMaze();
        MazeGenerator.Cell cFrom = maze[from.x][from.y];

        if (to.x > from.x) {
            return !cFrom.walls[MazeGenerator.Directions.RIGHT.ordinal()];
        } else if (to.x < from.x) {
            return !cFrom.walls[MazeGenerator.Directions.LEFT.ordinal()];
        } else if (to.y > from.y) {
            return !cFrom.walls[MazeGenerator.Directions.DOWN.ordinal()];
        } else if (to.y < from.y) {
            return !cFrom.walls[MazeGenerator.Directions.UP.ordinal()];
        }

        return false;
    }
}
