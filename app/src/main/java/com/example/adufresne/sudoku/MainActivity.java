package com.example.adufresne.sudoku;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button continueLevel = findViewById(R.id.continueLevel);
        Button selectLevel = findViewById(R.id.selectLevel);
        selectLevel.setOnClickListener(this);

        BDD bdd = new BDD();
        Cursor idList;
        boolean isDBEmpty = true;

        try {
            bdd.open(this);
            idList = bdd.isEmpty();

            if (idList.getCount() > 0) isDBEmpty = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        continueLevel.setEnabled(!isDBEmpty);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectLevel:
                Intent intent = new Intent(this, SelectLevelActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
