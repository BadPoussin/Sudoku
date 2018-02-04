package com.example.adufresne.sudoku;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BDD {
    private BDDHelper helper;
    private SQLiteDatabase base;

    public void open(Context activity) throws SQLException {
        helper = new BDDHelper(activity);
        base = helper.getWritableDatabase();
    }

    public Cursor isEmpty() throws SQLException {
        return base.rawQuery("SELECT _id FROM game", null);
    }

    public void saveGame(String niveau, int pourcentage, int zeroTotal, String grille, String locked, int temps) throws SQLException {
        base.execSQL("INSERT INTO game (`level`, `pourcentage`, `zeroTotal`, `grille`, `locked`, `time`) VALUES (" + niveau + ", " + pourcentage + ", " + zeroTotal + ", " + grille + ", " + locked + ", " + temps + ")");
    }

    public Cursor getSingleLevel(int id) throws SQLException {
        return base.rawQuery("SELECT zeroTotal, grille, locked, time FROM game WHERE _id = " + id, null);
    }

    public Cursor getLevels() throws SQLException {
        return base.rawQuery("SELECT DISTINCT level FROM game", null);
    }
}
