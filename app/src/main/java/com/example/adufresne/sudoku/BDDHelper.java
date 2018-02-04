package com.example.adufresne.sudoku;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDDHelper extends SQLiteOpenHelper {
    BDDHelper(Context context) {
        super(context, "game.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE game (_id INTEGER PRIMARY KEY AUTOINCREMENT, level VARCHAR NOT NULL, pourcentage INTEGER NOT NULL, zeroTotal INTEGER NOT NULL, grille VARCHAR NOT NULL, locked VARCHAR NOT NULL, time VARCHAR NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}