package com.example.lab3;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.content.Intent;
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
    private Paint wallPaint, playerPaint, exitPaint;

    MazePainter maze;
    Button button;
    PopupWindow popupWindow;
    Canvas canvas;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createView();
    }

    private void createView(){
        maze = new MazePainter(6, 10);

        wallPaint = new Paint();
        wallPaint.setColor(Color.DKGRAY);
        wallPaint.setStrokeWidth(WALL_THICKNESS);

        playerPaint = new Paint();
        playerPaint.setColor(Color.BLACK);

        exitPaint = new Paint();
        exitPaint.setColor(Color.DKGRAY);
    }

    private void Redraw(){
        canvas.drawColor(Color.LTGRAY);
        maze.paint(canvas, wallPaint, playerPaint, exitPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        Redraw();
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

    public void createPopup(){
        LayoutInflater inflater = (LayoutInflater) getContext().
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, (ViewGroup) getRootView(), false);

        //maze.changeCanvasAtFinish("3B393C");

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it

        popupWindow = new PopupWindow(popupView, width, height, false);
        popupWindow.showAtLocation((View) getParent(), Gravity.CENTER, 0, 0);

        button = popupView.findViewById(R.id.playAgain);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupView.setVisibility(GONE);
                popupWindow.showAsDropDown(GameView.this);
                popupWindow.dismiss();
                invalidate();
                createView();
                popupWindow = null;
                Redraw();
            }
        });
    }

    /**
     * Method to check if player reached an exit.
     */
    public void checkExit() {
        if(maze.checkExit() && popupWindow == null){
            createPopup();
        }
    }
}