package com.example.phili.rifletracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Philip on 25/11/2016.
 */

public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME      = "Shooting.db";
    public static final String TABLE_NAME   = "Scores";
    public static final String SHOOTER_NAME = "Shooter_Name";
    public static final String EVENT_NAME   = "Event_Name";
    public static final String RND_1        = "Round1";
    public static final String RND_2        = "Round2";
    public static final String RND_3        = "Round3";

    public Database(Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                    SHOOTER_NAME + " TEXT," +
                    EVENT_NAME + " TEXT,  " +
                    RND_1 + " INT, " + RND_2 + " INT, " + RND_3 + " INT," +
                    "PRIMARY KEY (" + SHOOTER_NAME + ", " + EVENT_NAME + "))");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop, then recreate
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean insert (String name, String event, int r1Score, int r2Score, int r3Score) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(SHOOTER_NAME, name);
            cv.put(EVENT_NAME, event);
            cv.put(RND_1, r1Score);
            cv.put(RND_2, r2Score);
            cv.put(RND_3, r3Score);
            long res = db.insert(TABLE_NAME, null, cv);

            if (res == -1) {
                return false;
            }

            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean eventExists (String event) {
        Cursor alreadyHasEvent = this.getWritableDatabase().rawQuery("SELECT * FROM " +
                TABLE_NAME + " WHERE "  + EVENT_NAME + "=\"" + event + "\"", null);

        return (alreadyHasEvent.getCount() != 0);
    }



    public Cursor selectEvent (String eventName) {
        Cursor event = this.getWritableDatabase().rawQuery("SELECT " +SHOOTER_NAME+ ", " +RND_1 +
                ", " +RND_2 + ", " + RND_3 + " FROM " + TABLE_NAME + " WHERE "  + EVENT_NAME +
                "=\"" + eventName + "\"", null);

        System.out.println("Size of output: " + event.getCount());

        return event;
    }
}
