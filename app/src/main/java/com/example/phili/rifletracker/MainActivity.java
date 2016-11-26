package com.example.phili.rifletracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.ThemeOverlay_Material);
        setContentView(R.layout.activity_main);

        //Create database
        Database db = new Database(this);
        db.close();

        Button btnRecentEvents = (Button)findViewById(R.id.buttonRecentEvents);
        Button btnAddNewEvent = (Button)findViewById(R.id.buttonAddNewEvent);
        Button btnExportEvent =  (Button)findViewById(R.id.buttonExportEvent);
        Button btnSettings =  (Button)findViewById(R.id.buttonSettings);

        //Recent events programming
        btnRecentEvents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Launch RecentEvents
                if (/*the database is empty*/ true) {
                    Toast.makeText(getApplicationContext(), "Error: No events exist. Add a new" +
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

        // TODO: Export screens
        /*
        btnExportEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RecentEventView.class);
                startActivity(i);
            }
        });
        */


        //Settings programming
        btnSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Settings.class);
                startActivity(i);
            }
        });



    }



}
