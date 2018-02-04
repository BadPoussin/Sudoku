package com.example.adufresne.sudoku;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {
    private String[][] dataSet;
    private boolean[][] dataLocked = new boolean[9][9];
    private int timeElapsed = 0;
    private int zeroCounter = 0;
    private boolean isContinuing = false;
    private com.example.adufresne.sudoku.sudokuGrid myView;
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, deleteButton;
    GridView sudokuGrid;
    TextView typeGame, durationGame, numberSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        String numberGame = getIntent().getStringExtra("typeGame");
        String isAlreadyStarted = getIntent().getStringExtra("isAlreadyStarted");
        Timer timer = new Timer();
        startTimer(timer);

        int lineNumber = 0;
        String labelGame = "";

        //Gérer le cas si la grille est déjà commencée

        if (isAlreadyStarted.equals("true")) {
            BDD dataBase = new BDD();
            lineNumber = Integer.parseInt(numberGame);
            labelGame = "Niveau " + String.valueOf(lineNumber + 1);
            String grille = "";
            String locked = "";
            isContinuing = true;

            try {
                dataBase.open(this);
                Cursor levelInformations = dataBase.getSingleLevel(lineNumber);

                while (levelInformations.moveToNext()) {
                    //int zeroTotal = levelInformations.getInt(levelInformations.getColumnIndex("zeroTotal"));
                    grille = levelInformations.getString(levelInformations.getColumnIndex("grille"));
                    locked = levelInformations.getString(levelInformations.getColumnIndex("locked"));
                    timeElapsed = Integer.parseInt(levelInformations.getString(levelInformations.getColumnIndex("time")));

                    Log.d("TEST", "grille = " + grille);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Log.d("TEST", "grille go");
            dataSet = SetNumberList(grille, isContinuing);
            dataLocked = SetDataLocked(locked);
            Log.d("TEST", "dataLocked ok");
        } else if (isAlreadyStarted.equals("false")) {
            int fileRessourceId = R.raw.list_problem_1;

            InputStream is = this.getResources().openRawResource(fileRessourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String str;

            if (is != null) {
                try {
                    while ((str = reader.readLine()) != null) {
                        if (numberGame.equals(String.valueOf((lineNumber + 1)))) {
                            dataSet = SetNumberList(str, isContinuing);
                            labelGame = "Niveau " + String.valueOf(lineNumber + 1);
                            break;
                        } else lineNumber++;
                    }
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        myView = findViewById(R.id.sudokuGrid);
        myView.context = this;
        myView.setContent(dataSet, dataLocked, zeroCounter, String.valueOf(lineNumber));

        button1 = findViewById(R.id.button1);
        button1.setOnTouchListener(this);
        button2 = findViewById(R.id.button2);
        button2.setOnTouchListener(this);
        button3 = findViewById(R.id.button3);
        button3.setOnTouchListener(this);
        button4 = findViewById(R.id.button4);
        button4.setOnTouchListener(this);
        button5 = findViewById(R.id.button5);
        button5.setOnTouchListener(this);
        button6 = findViewById(R.id.button6);
        button6.setOnTouchListener(this);
        button7 = findViewById(R.id.button7);
        button7.setOnTouchListener(this);
        button8 = findViewById(R.id.button8);
        button8.setOnTouchListener(this);
        button9 = findViewById(R.id.button9);
        button9.setOnTouchListener(this);
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnTouchListener(this);

        numberSelected = findViewById(R.id.numberSelected);
        typeGame = findViewById(R.id.typeGame);
        typeGame.setText(getString(R.string.TypeGame) + " " + labelGame);
        durationGame = findViewById(R.id.durationGame);
    }

    private boolean[][] SetDataLocked(String numberString) {
        boolean[][] locked = new boolean[9][9];

        for (int columns = 0; columns < 9; columns++) {
            for (int lines = 0; lines < 9; lines++) {
                locked[lines][columns] = numberString.equals("0");
            }
        }

        return locked;
    }

    private String[][] SetNumberList(String numberString, boolean isContinuing) {
        int position = 0;
        String[][] dataSet = new String[9][9];

        for (int columns = 0; columns < 9; columns++) {
            for (int lines = 0; lines < 9; lines++) {
                if (!numberString.substring(position, (position + 1)).equals("0")) {
                    dataSet[lines][columns] = numberString.substring(position, (position + 1));
                    Log.d("TEST", "Number : " + numberString.substring(position, (position + 1)) + " AND POSITION " + position);
                    if (!isContinuing) dataLocked[lines][columns] = true;
                } else {
                    dataSet[lines][columns] = "";
                    if (!isContinuing) dataLocked[lines][columns] = false;
                    zeroCounter++;
                }
                position++;
            }
        }

        return dataSet;
    }


    private void startTimer(Timer timer) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        durationGame.setText("Durée : " + timeElapsed + " s.");
                        timeElapsed++;
                        myView.updateTimer(timeElapsed);
                    }
                });
            }
        }, 1000, 1000);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.button1:
                    numberSelected.setText("1");
                    break;
                case R.id.button2:
                    numberSelected.setText("2");
                    break;
                case R.id.button3:
                    numberSelected.setText("3");
                    break;
                case R.id.button4:
                    numberSelected.setText("4");
                    break;
                case R.id.button5:
                    numberSelected.setText("5");
                    break;
                case R.id.button6:
                    numberSelected.setText("6");
                    break;
                case R.id.button7:
                    numberSelected.setText("7");
                    break;
                case R.id.button8:
                    numberSelected.setText("8");
                    break;
                case R.id.button9:
                    numberSelected.setText("9");
                    break;
                case R.id.deleteButton:
                    numberSelected.setText("Gomme");
                    break;
            }
        }
        return true;
    }
}
