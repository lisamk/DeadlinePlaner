package com.deadline.kritz.jku.planer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_DEADLINES = "deadlines";
    public static final String D_COLUMN_ID = "_id";
    public static final String D_COLUMN_TITLE = "title";
    public static final String D_COLUMN_GROUP = "_group";
    public static final String D_COLUMN_DESCRIPTION = "description";
    public static final String D_COLUMN_DATE = "date";

    public static final String TABLE_GROUPS = "groups";
    public static final String G_COLUMN_ID = "_id";
    public static final String G_COLUMN_GID = "title";
    public static final String G_COLUMN_TITLE = "gid";
    public static final String G_COLUMN_HIDDEN = "hidden";

    private static final String DATABASE_NAME = "deadline.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String G_DATABASE_CREATE = "create table if not exists "
            + TABLE_GROUPS + "( " + G_COLUMN_ID + " integer primary key autoincrement, "
            + G_COLUMN_TITLE + ", " + G_COLUMN_GID + ", " + G_COLUMN_HIDDEN + ");";

    // Database creation sql statement
    private static final String D_DATABASE_CREATE = "create table if not exists "
            + TABLE_DEADLINES + "( " + D_COLUMN_ID + ", "
            + D_COLUMN_TITLE + ", " + D_COLUMN_GROUP + ", " + D_COLUMN_DESCRIPTION + ", " + D_COLUMN_DATE+");";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(D_DATABASE_CREATE);
        database.execSQL(G_DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEADLINES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        onCreate(db);
    }

}