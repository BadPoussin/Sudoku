package com.example.adufresne.sudoku;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class SelectContinueLevelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_continue_level);

        final GridView grid = findViewById(R.id.gridview);
        BDD dataBase = new BDD();
        Cursor levels;
        List<String> listLevels = new ArrayList<>();

        try {
            dataBase.open(this);
            levels = dataBase.getLevels();

            while (levels.moveToNext()) {
                listLevels.add("Grille n°" + levels.getString(levels.getColumnIndex("level")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ArrayAdapter<String> gridViewArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listLevels);
        grid.setAdapter(gridViewArrayAdapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(SelectContinueLevelActivity.this, GameActivity.class);
                intent.putExtra("typeGame", grid.getItemAtPosition(position).toString().substring(9, grid.getItemAtPosition(position).toString().length()));
                intent.putExtra("isAlreadyStarted", "true");
                startActivity(intent);
            }
        });
    }
}
