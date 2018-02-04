package com.example.adufresne.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.sql.SQLException;

public class sudokuGrid extends View implements View.OnTouchListener {
    private Paint rectColor = new Paint();
    private Paint writeText = new Paint();
    private String[][] dataSet;
    private String level;
    private boolean[][] dataLocked;
    private boolean isValuesReady = false;
    private int largeurCell = 150;
    private int longueurCell = 150;
    private int separator = 10;
    private int xDepart = 10;
    private int yDepart = 10;
    private int zeroCounter = 0;
    private int zeroLeft = 0;
    private int timeCounter = 0;
    public GameActivity context;

    private BDD dataBase;

    public sudokuGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
        dataBase = new BDD();
    }

    public void onDraw(Canvas canvas) {
        rectColor.setColor(Color.WHITE);
        writeText.setTextSize(100);
        canvas.drawColor(Color.BLACK);

        int columnNumber = 9;
        for (int columnCount = 0; columnCount < columnNumber; columnCount++) {
            int rawNumber = 9;
            for (int rawCount = 0; rawCount < rawNumber; rawCount++) {

                Rect rectangle = new Rect((xDepart + columnCount * (largeurCell + separator)), (yDepart + rawCount * (longueurCell + separator)), xDepart + columnCount * (largeurCell + separator) + largeurCell, yDepart + rawCount * (longueurCell + separator) + longueurCell);
                canvas.drawRect(rectangle, rectColor);
                if (isValuesReady) {
                    if (dataSet[columnCount][rawCount].equals("")) zeroLeft++;
                    if (breakRules(dataSet, rawCount, columnCount)) writeText.setColor(Color.RED);
                    else writeText.setColor(Color.BLACK);
                    canvas.drawText(dataSet[columnCount][rawCount], xDepart + columnCount * (largeurCell + separator) + (largeurCell / 2) - 25, yDepart + rawCount * (longueurCell + separator) + (longueurCell / 2) + 30, writeText);
                    updateProgression(dataSet, zeroLeft, zeroCounter, timeCounter);
                }
            }
        }
    }

    private void updateProgression(String[][] dataSet, int zeroLeft, int zeroCounter, int timer) {
        int progression = (zeroLeft * 100) / zeroCounter;
        StringBuilder data = new StringBuilder();
        StringBuilder locked = new StringBuilder();

        for (int columnCount = 0; columnCount < 9; columnCount++) {
            for (int rawCount = 0; rawCount < 9; rawCount++) {
                if (!dataSet[rawCount][columnCount].equals("")) data.append(dataSet[rawCount][columnCount]);
                else data.append("0");
                if (dataLocked[rawCount][columnCount]) locked.append("1");
                else locked.append("0");
            }
        }

        dataBase.open(context);
        dataBase.saveGame(level, progression, zeroCounter, data.toString(), locked.toString(), timer);
    }

    public void updateTimer(int counter) {
        timeCounter = counter;
    }

    public void setContent(String[][] data, boolean[][] dataL, int counter, String niveau) {
        dataSet = data;
        dataLocked = dataL;
        isValuesReady = true;
        zeroCounter = counter;
        level = niveau;
        invalidate();
    }

    private void changeValue(int x, int y, String button) {
        int xChanged = (x - xDepart) / (largeurCell + separator);
        int yChanged = (y - yDepart) / (longueurCell + separator);
        if (!dataLocked[xChanged][yChanged]) {
            if (!button.equals("Gomme"))
                dataSet[xChanged][yChanged] = button;
            else dataSet[xChanged][yChanged] = "";
            invalidate();
        }
    }

    private boolean breakRules(String[][] data, int raw, int column) {
        int posX = 0;
        int posY = 0;

        for (int columnCount = 0; columnCount < 9; columnCount++) {
            if (data[column][raw].equals(data[columnCount][raw]) && column != columnCount) return true;
        }

        for (int lineCount = 0; lineCount < 9; lineCount++) {
            if (data[column][raw].equals(data[column][lineCount]) && raw != lineCount) return true;
        }

        switch (column) {
            case 0:
            case 1:
            case 2:
                posX = 0;
                break;
            case 3:
            case 4:
            case 5:
                posX = 3;
                break;
            case 6:
            case 7:
            case 8:
                posX = 6;
                break;
            default:
                break;
        }

        switch (raw) {
            case 0:
            case 1:
            case 2:
                posY = 0;
                break;
            case 3:
            case 4:
            case 5:
                posY = 3;
                break;
            case 6:
            case 7:
            case 8:
                posY = 6;
                break;
            default:
                break;
        }

        for (int startSquareY = 0; startSquareY < 3; startSquareY++) {
            for (int startSquareX = 0; startSquareX < 3; startSquareX++) {
                if ((startSquareY + posY != raw) || (startSquareX + posX != column))
                    if (data[column][raw].equals(data[startSquareX + posX][startSquareY + posY])) return true;
            }
        }

        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        TextView text = context.findViewById(R.id.numberSelected);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (!text.getText().equals("")) {
                    changeValue(x, y, text.getText().toString());
                    text.setText("");
                }
                break;
            default:
                break;
        }
        return true;
    }
}
