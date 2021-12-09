package com.example.lab3;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.lab3.maze.MazePainter;
import com.example.lab3.maze.model.Cell;
import com.example.lab3.maze.model.Direction;

import java.util.Random;

/**
 * Class to generate new maze and paint walls, cells, player and exit.
 */
public class GameView extends View {
    private static final float WALL_THICKNESS = 6;
    /**
     * Paints for different objects.
     */
    private final Paint wallPaint, playerPaint, exitPaint;

    MazePainter maze;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        maze = new MazePainter(10, 17);

        wallPaint = new Paint();
        wallPaint.setColor(Color.RED);
        wallPaint.setStrokeWidth(WALL_THICKNESS);

        playerPaint = new Paint();
        playerPaint.setColor(Color.BLACK);

        exitPaint = new Paint();
        exitPaint.setColor(Color.LTGRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);
        maze.paint(canvas, wallPaint, playerPaint, exitPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            return true;
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();

            maze.movePlayerTowards(x, y);
            checkExit();
            invalidate();

            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Method to check if player reached an exit.
     */
    public void checkExit() {
        if (maze.checkExit()) {
            LayoutInflater inflater = (LayoutInflater) getContext().
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup, (ViewGroup) getRootView(), false);

            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.showAtLocation((View) getParent(), Gravity.CENTER, 0, 0);
            final Button button = (Button) findViewById(R.id.playAgain);
            System.out.println(button);

            /*button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/
        }
    }
}