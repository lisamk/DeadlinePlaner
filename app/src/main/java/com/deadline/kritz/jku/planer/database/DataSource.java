package com.deadline.kritz.jku.planer.database;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.deadline.kritz.jku.planer.Deadline;
import com.deadline.kritz.jku.planer.Group;

import static com.deadline.kritz.jku.planer.Planer.SDF;

public class DataSource {

    // Database fields
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumnsDeadline = { SQLiteHelper.D_COLUMN_ID, SQLiteHelper.D_COLUMN_TITLE,
            SQLiteHelper.D_COLUMN_DESCRIPTION, SQLiteHelper.D_COLUMN_GROUP, SQLiteHelper.D_COLUMN_DATE };

    private String[] allColumnsGroup = { SQLiteHelper.G_COLUMN_ID, SQLiteHelper.G_COLUMN_TITLE, SQLiteHelper.G_COLUMN_HIDDEN };

    public DataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Deadline createDeadline(Deadline deadline) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.D_COLUMN_TITLE, deadline.getTitle());
        values.put(SQLiteHelper.D_COLUMN_DATE, SDF.format(deadline.getDate()));
        values.put(SQLiteHelper.D_COLUMN_DESCRIPTION, deadline.getDescription());
        values.put(SQLiteHelper.D_COLUMN_GROUP, deadline.getGroup().getTitle());
        long insertId = database.insert(SQLiteHelper.TABLE_DEADLINES, null,
                values);
        Cursor cursor = database.query(SQLiteHelper.TABLE_DEADLINES,
                allColumnsDeadline, SQLiteHelper.D_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Deadline newdeadline = cursorToDeadline(cursor);
        cursor.close();
        return newdeadline;
    }

    public void deleteDeadline(Deadline deadline) {
        long id = deadline.getId();
        System.out.println("deadline deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_DEADLINES, SQLiteHelper.D_COLUMN_ID
                + " = " + id, null);
    }

    public List<Deadline> getAllDeadlines() {
        List<Deadline> deadlines = new ArrayList<Deadline>();
        Cursor cursor = database.query(SQLiteHelper.TABLE_DEADLINES,
                allColumnsDeadline, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Deadline deadline = cursorToDeadline(cursor);
            deadlines.add(deadline);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return deadlines;
    }

    private Deadline cursorToDeadline(Cursor cursor) {
        try {
            Deadline deadline = new Deadline(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), SDF.parse(cursor.getString(4)));
            return deadline;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // GROUPS

    public Group createGroup(Group group) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.G_COLUMN_TITLE, group.getTitle());
        values.put(SQLiteHelper.G_COLUMN_HIDDEN, group.isHidden());
        long insertId = database.insert(SQLiteHelper.TABLE_GROUPS, null, values);
        Cursor cursor = database.query(SQLiteHelper.TABLE_GROUPS,
                allColumnsGroup, SQLiteHelper.G_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Group newGroup = cursorToGroup(cursor);
        cursor.close();
        return newGroup;
    }

    public void deleteGroup(Group group) {
        long id = group.getId();
        System.out.println("Group deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_GROUPS, SQLiteHelper.G_COLUMN_ID + " = " + id, null);
    }

    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<Group>();
        Cursor cursor = database.query(SQLiteHelper.TABLE_GROUPS,
                allColumnsGroup, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Group group = cursorToGroup(cursor);
            groups.add(group);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return groups;
    }

    private Group cursorToGroup(Cursor cursor) {
        return new Group(cursor.getLong(0), cursor.getString(1), Boolean.parseBoolean(cursor.getString(2)));
    }
}
