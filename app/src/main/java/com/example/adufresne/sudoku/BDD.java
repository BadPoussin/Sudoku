package com.example.adufresne.sudoku;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BDD {
    private BDDHelper helper;
    private SQLiteDatabase base;

    public void open (Context activity) throws SQLException {
        helper = new BDDHelper(activity);
        base = helper.getWritableDatabase();

    }

    public Cursor isEmpty () throws SQLException {
        return base.rawQuery("SELECT _id FROM game WHERE _id = 0", null);
    }

    public void saveGame (String niveau, int pourcentage, int zeroTotal, String[][] grille, int temps) throws  SQLException {
       //base.execSQL("INSERT INTO game VALUES");
    }
}
