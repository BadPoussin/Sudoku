package com.example.adufresne.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SelectLevelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);

        final GridView grid = findViewById(R.id.gridview);
        int fileRessourceId = R.raw.list_problem_1;

        InputStream is = this.getResources().openRawResource(fileRessourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String str = "";
        int lineNumber = 1;
        List<String> gameList = new ArrayList<>();

        if (is != null) {
            try {
                while ((str = reader.readLine()) != null) {
                    gameList.add(String.valueOf("Grille n°" + lineNumber));
                    lineNumber++;
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final ArrayAdapter<String> gridViewArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gameList);
        grid.setAdapter(gridViewArrayAdapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(SelectLevelActivity.this, GameActivity.class);
                intent.putExtra("typeGame", grid.getItemAtPosition(position).toString().substring(9, grid.getItemAtPosition(position).toString().length()));
                intent.putExtra("isAlreadyStarted", "false");
                startActivity(intent);
            }
        });
    }
}
