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

    //Creates the table and all columns
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

    //U[grades the table
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop, then recreate
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    //Inserts a row into the table
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

    //Check to see if there's a row where
    public boolean eventExists (String event) {
        Cursor alreadyHasEvent = this.getWritableDatabase().rawQuery("SELECT * FROM " +
                TABLE_NAME + " WHERE "  + EVENT_NAME + "=\"" + event + "\"", null);

        return (alreadyHasEvent.getCount() != 0);
    }


    //Gets all rows with a certain event, and returns them
    public Cursor selectEvent (String eventName) {
        Cursor event = this.getWritableDatabase().rawQuery("SELECT " +SHOOTER_NAME+ ", " +RND_1 +
                ", " +RND_2 + ", " + RND_3 + " FROM " + TABLE_NAME + " WHERE "  + EVENT_NAME +
                "=\"" + eventName + "\"", null);

        return event;
    }

    //Check to see if there's anything in the table
    public boolean dataExists () {

        Cursor c = this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);

        return (c.getCount() !=0);
    }

    //Returns a list of all the events in the table
    public ArrayList<String> listOfEvents() {
        Cursor c = this.getWritableDatabase().rawQuery("SELECT distinct " + EVENT_NAME + " FROM " +
                TABLE_NAME, null);

        cString = new ArrayList<String>();

        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
            cString.add(c.getString(c.getColumnIndex(EVENT_NAME)));
        }

        return cString;
    }

    //Updates the table with a new event name
    public void renameEvent (String eventNameOld, String eventNameNew) {
        this.getWritableDatabase().execSQL("UPDATE " + TABLE_NAME+ " SET " + EVENT_NAME + " = \"" +
                eventNameNew + "\" WHERE " + EVENT_NAME + " = \"" + eventNameOld + "\"");
    }

    //Deletes all events with a specific name
    public void deleteEvent (String event) {
        this.getWritableDatabase().execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + EVENT_NAME +
                " = \"" + event + "\"");
    }

    //Gets all the names that are in a specific event
    public ArrayList<String> listOfNamesInEvent(String event) {
        Cursor c = this.getWritableDatabase().rawQuery("SELECT distinct " + SHOOTER_NAME + " FROM "
                + TABLE_NAME + " WHERE " + EVENT_NAME + " = \"" + event + "\"", null);

        cString = new ArrayList<String>();

        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
            cString.add(c.getString(c.getColumnIndex(SHOOTER_NAME)));
        }

        return cString;
    }


    //Renames all instances of shooterNameOld with shooterNameNew in a specific event
    public void renameShooter (String shooterNameOld, String shooterNameNew, String eventName) {
        this.getWritableDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + SHOOTER_NAME + " = \""
                + shooterNameNew + "\" WHERE " + SHOOTER_NAME + " = \"" + shooterNameOld + "\" AND "
                + EVENT_NAME + " = \"" + eventName);
    }

    //Deletes a shooter from the db
    public void deleteShooter (String shooter, String eventName) {
        this.getWritableDatabase().execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + SHOOTER_NAME +
                " = \"" + shooter + "\" AND " +
                EVENT_NAME + " = \"" + eventName);
    }

    //Edit scores of shooter
    public void editScores (int[] scores, String shooterName, String eventName) {
        this.getWritableDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + RND_1 + " = " +
                scores[0] + " WHERE " + SHOOTER_NAME + " = \"" + shooterName + "\" AND " +
                EVENT_NAME + " = \"" + eventName);
        this.getWritableDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + RND_2 + " = " +
                scores[1] + " WHERE " + SHOOTER_NAME + " = \"" + shooterName + "\" AND " +
                EVENT_NAME + " = \"" + eventName);
        this.getWritableDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + RND_3 + " = " +
                scores[2] + " WHERE " + SHOOTER_NAME + " = \"" + shooterName + "\" AND " +
                EVENT_NAME + " = \"" + eventName);
    }

    //Drop and recreate table
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
