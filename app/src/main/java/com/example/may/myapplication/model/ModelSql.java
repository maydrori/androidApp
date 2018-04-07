package com.example.may.myapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.may.myapplication.MyApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by May on 4/4/2018.
 */

public class ModelSql {

    /* Inner class that defines the table contents */
    public static class WorkshopEntry implements BaseColumns {
        public static final String TABLE_NAME = "workshops";
        public static final String COLUMN_TEACHER_ID = "teacherId";
        public static final String COLUMN_PLACE = "place";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_MAX_PARTICIPANTS = "maxParticipants";
        public static final String COLUMN_MEMBERS = "members";
    }

    private final int DB_VERSION = 1;
    private MyOpenHelper helper;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WorkshopEntry.TABLE_NAME + " (" +
                    WorkshopEntry._ID + " INTEGER PRIMARY KEY," +
                    WorkshopEntry.COLUMN_TEACHER_ID + " INTEGER," +
                    WorkshopEntry.COLUMN_PLACE + " TEXT," +
                    WorkshopEntry.COLUMN_DATE + " TEXT," +
                    WorkshopEntry.COLUMN_LEVEL + " TEXT," +
                    WorkshopEntry.COLUMN_MAX_PARTICIPANTS + " INTEGER," +
                    WorkshopEntry.COLUMN_MEMBERS + " INTEGER)"; // TODO

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WorkshopEntry.TABLE_NAME;

    private static final String SQL_INSERT =
            "DROP TABLE IF EXISTS " + WorkshopEntry.TABLE_NAME;

    public ModelSql () {
        helper = new MyOpenHelper(MyApp.context);
    }

//    public void addWorkshop(Workshop w) {
//
//        // Gets the data repository in write mode
//        SQLiteDatabase db = helper.getWritableDatabase();
//
//        // Create a new map of values, where column names are the keys
//        ContentValues values = new ContentValues();
//        values.put(WorkshopEntry.COLUMN_TEACHER_ID, w.getTeacherId());
//        values.put(WorkshopEntry.COLUMN_PLACE, w.getPlace());
//        values.put(WorkshopEntry.COLUMN_DATE, w.getDate().toString());
//        values.put(WorkshopEntry.COLUMN_LEVEL, w.getLevel());
//        values.put(WorkshopEntry.COLUMN_MAX_PARTICIPANTS, w.getMaxParticipants());
//        values.put(WorkshopEntry.COLUMN_MEMBERS, w.getMaxParticipants()); // TODO
//
//        // Insert the new row, returning the primary key value of the new row
//        long newRowId = db.insert(WorkshopEntry.TABLE_NAME, WorkshopEntry.COLUMN_TEACHER_ID, values);
//    }

//    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm", Locale.getDefault());

//    public List<Workshop> getAllWorkshops() throws ParseException {
//
//        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.query(WorkshopEntry.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null);
//
//        List workshops = new ArrayList<Workshop>();
//        while(cursor.moveToNext()) {
//
//            String id = cursor.getLong(cursor.getColumnIndex(WorkshopEntry._ID));
//            long teacherId = cursor.getLong(cursor.getColumnIndex(WorkshopEntry.COLUMN_TEACHER_ID));
//            String place = cursor.getString(cursor.getColumnIndex(WorkshopEntry.COLUMN_PLACE));
//            Date date = dateFormat.parse(cursor.getString(cursor.getColumnIndex(WorkshopEntry.COLUMN_DATE)));
//            String level = cursor.getString(cursor.getColumnIndex(WorkshopEntry.COLUMN_LEVEL));
//            int maxParticipants = cursor.getInt(cursor.getColumnIndex(WorkshopEntry.COLUMN_MAX_PARTICIPANTS));
//
//            workshops.add(new Workshop(id, date, teacherId, place, level, maxParticipants));
//        }
//        cursor.close();
//
//        return workshops;
//    }

    class MyOpenHelper extends SQLiteOpenHelper {

        public MyOpenHelper(Context context) {
            super(context, "database.db", null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
            onCreate(sqLiteDatabase);
        }
    }
}
