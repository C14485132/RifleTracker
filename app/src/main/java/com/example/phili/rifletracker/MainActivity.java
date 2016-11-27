package com.example.phili.rifletracker;

/* MinActivity
* - Starts the app, and launches all the activities from buttons
* - Creates a database if none exists by using Database's onCreate
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    Button buttonDeleteAll;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.ThemeOverlay_Material_Light);
        setContentView(R.layout.activity_main);

        //Create database
        db = new Database(this);
        final boolean dataExists = db.dataExists();
        db.close();

        Button btnRecentEvents = (Button)findViewById(R.id.buttonRecentEvents);
        Button btnAddNewEvent = (Button)findViewById(R.id.buttonAddNewEvent);
        Button btnExportEvent =  (Button)findViewById(R.id.buttonExportEvent);
        buttonDeleteAll = (Button)findViewById(R.id.buttonDeleteAll);

        //Recent events programming
        btnRecentEvents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Launch RecentEvents
                if (!dataExists) {
                    Toast.makeText(getApplicationContext(), "Error: No events exist. Add a new " +
                            "event before selecting this.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(getApplicationContext(), RecentEvents.class);
                    startActivity(i);
                }
            }
        });

        //Add new event programming
        btnAddNewEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Launch AddNewEvent
                Intent i = new Intent(getApplicationContext(),AddNewEvent.class);
                startActivity(i);
            }
        });


        btnExportEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataExists) {
                    //Referenced from: http://stackoverflow.com/questions/31367270/
                    File dbFile = getDatabasePath("Shooting.db");
                    File exportDir = new File(Environment.getExternalStorageDirectory(), "");
                    if (!exportDir.exists()) {
                        exportDir.mkdirs();
                    }

                    File file = new File(exportDir, "scores.csv");
                    try {
                        file.createNewFile();
                        CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                        SQLiteDatabase db2 = db.getReadableDatabase();
                        Cursor curCSV = db2.rawQuery("SELECT * FROM SCORES", null);
                        csvWrite.writeNext(curCSV.getColumnNames());
                        while (curCSV.moveToNext()) {
                            //Which column you want to exprort
                            String arrStr[] = {curCSV.getString(0), curCSV.getString(1),
                                    curCSV.getString(2), curCSV.getString(3), curCSV.getString(4)};
                            csvWrite.writeNext(arrStr);
                        }
                        csvWrite.close();
                        curCSV.close();

                        Toast.makeText(getApplicationContext(), "Export directory: " +
                                Environment.getExternalStorageDirectory(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception sqlEx) {
                        Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
                    }//End reference
                } else {
                    Toast.makeText(getApplicationContext(), "Error: No events exist. Add a new " +
                                    "event before selecting this.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });

    }

    public void confirmDelete() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Delete");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to delete all events?");

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                db = new Database(getApplicationContext());
                db.deleteAllVals();
                Toast.makeText(getApplicationContext(), "Deleted successfully!",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
