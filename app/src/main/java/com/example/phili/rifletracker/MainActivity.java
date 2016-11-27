package com.example.phili.rifletracker;

/* MinActivity *?
* - Starts the app, and launches all the activities from buttons
* - Creates a databse if none exists
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.ThemeOverlay_Material);
        setContentView(R.layout.activity_main);

        //Create database
        final Database db = new Database(this);

        Button btnRecentEvents = (Button)findViewById(R.id.buttonRecentEvents);
        Button btnAddNewEvent = (Button)findViewById(R.id.buttonAddNewEvent);
        Button btnExportEvent =  (Button)findViewById(R.id.buttonExportEvent);
        Button btnSettings =  (Button)findViewById(R.id.buttonSettings);

        //Recent events programming
        btnRecentEvents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Launch RecentEvents
                if (!db.dataExists()) {
                    db.close();
                    Toast.makeText(getApplicationContext(), "Error: No events exist. Add a new " +
                            "event before selecting this.",
                            Toast.LENGTH_LONG).show();
                } else {
                    db.close();
                    Intent i = new Intent(getApplicationContext(), RecentEvents.class);
                    startActivity(i);
                }
            }
        });

        //Add new event programming
        btnAddNewEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.close();
                //Launch AddNewEvent
                Intent i = new Intent(getApplicationContext(),AddNewEvent.class);
                startActivity(i);
            }
        });

        
        btnExportEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!db.dataExists()) {
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
                            String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4)};
                            csvWrite.writeNext(arrStr);
                        }
                        csvWrite.close();
                        curCSV.close();

                        Toast.makeText(getApplicationContext(), "Export directory: " + Environment.getExternalStorageDirectory(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception sqlEx) {
                        Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error: No events exist. Add a new " +
                                    "event before selecting this.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        //Settings programming
        btnSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.close();
                Intent i = new Intent(getApplicationContext(),Settings.class);
                startActivity(i);
            }
        });



    }



}
