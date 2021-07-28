package fr.shk.thetrashapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClientDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "trash.db";

    public final String SQL_CREATE_TRASH = "CREATE TABLE IF NOT EXISTS trash (color VARCHAR(30) PRIMARY KEY, jourPassage VARCHAR(30), heurePassage TIME);";
    public final String SQL_CREATE_GARBAGE = "CREATE TABLE IF NOT EXISTS garbage (nom VARCHAR(55) PRIMARY KEY, poubelle VARCHAR(30));";
    public final String SQL_DELETE_TRASH = "DROP TABLE IF EXISTS trash;";
    public final String SQL_DELETE_GARBAGE = "DROP TABLE IF EXISTS garbage;";

    public ClientDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TRASH);
        db.execSQL(SQL_CREATE_GARBAGE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TRASH);
        db.execSQL(SQL_DELETE_GARBAGE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
