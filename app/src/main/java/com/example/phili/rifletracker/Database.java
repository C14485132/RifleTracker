package com.example.phili.rifletracker;

/*Database
* - Manages the database
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME      = "Shooting.db";
    public static final String TABLE_NAME   = "Scores";
    public static final String SHOOTER_NAME = "Shooter_Name";
    public static final String EVENT_NAME   = "Event_Name";
    public static final String RND_1        = "Round1";
    public static final String RND_2        = "Round2";
    public static final String RND_3        = "Round3";
    ArrayList<String> cString;

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

    public void insert (String name, String event, int r1Score, int r2Score, int r3Score) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(SHOOTER_NAME, name);
            cv.put(EVENT_NAME, event);
            cv.put(RND_1, r1Score);
            cv.put(RND_2, r2Score);
            cv.put(RND_3, r3Score);
            db.insert(TABLE_NAME, null, cv);
        } catch (Exception e) {
            System.out.println(e);
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

        return event;
    }

    public boolean dataExists () {

        Cursor c = this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);

        return (c.getCount() !=0);
    }

    public ArrayList<String> listOfEvents() {
        Cursor c = this.getWritableDatabase().rawQuery("SELECT distinct " + EVENT_NAME + " FROM " + TABLE_NAME, null);

        cString = new ArrayList<String>();

        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
            cString.add(c.getString(c.getColumnIndex(EVENT_NAME)));
        }

        return cString;
    }

    public void renameEvent (String eventNameOld, String eventNameNew) {
        this.getWritableDatabase().execSQL("UPDATE " + TABLE_NAME+ " SET " + EVENT_NAME + " = \"" + eventNameNew + "\" WHERE " + EVENT_NAME + " = \"" + eventNameOld + "\"");
    }

    public void deleteEvent (String event) {
        this.getWritableDatabase().execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + EVENT_NAME + " = \"" + event + "\"");
    }

    public ArrayList<String> listOfNamesInEvent(String event) {
        Cursor c = this.getWritableDatabase().rawQuery("SELECT distinct " + SHOOTER_NAME + " FROM " + TABLE_NAME + " WHERE " + EVENT_NAME + " = \"" + event + "\"", null);

        cString = new ArrayList<String>();

        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
            cString.add(c.getString(c.getColumnIndex(SHOOTER_NAME)));
        }

        return cString;
    }


    public void renameShooter (String shooterNameOld, String shooterNameNew) {
        this.getWritableDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + SHOOTER_NAME + " = \"" + shooterNameNew + "\" WHERE " + SHOOTER_NAME + " = \"" + shooterNameOld + "\"");
    }

    public void deleteShooter (String shooter) {
        this.getWritableDatabase().execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + SHOOTER_NAME + " = \"" + shooter + "\"");
    }

    public void editScores (int[] scores, String shooterName) {
        this.getWritableDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + RND_1 + " = " + scores[0] + " WHERE " + SHOOTER_NAME + " = \"" + shooterName + "\"");
        this.getWritableDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + RND_2 + " = " + scores[1] + " WHERE " + SHOOTER_NAME + " = \"" + shooterName + "\"");
        this.getWritableDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + RND_3 + " = " + scores[2] + " WHERE " + SHOOTER_NAME + " = \"" + shooterName + "\"");
    }

    public void deleteAllVals () {
        //Drop and recreate
        this.getWritableDatabase().execSQL("DROP TABLE " + TABLE_NAME);
        this.getWritableDatabase().execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                SHOOTER_NAME + " TEXT," +
                EVENT_NAME + " TEXT,  " +
                RND_1 + " INT, " + RND_2 + " INT, " + RND_3 + " INT," +
                "PRIMARY KEY (" + SHOOTER_NAME + ", " + EVENT_NAME + "))");

    }


}
