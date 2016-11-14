package com.example.phili.rifletracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnRecentEvents = (Button)findViewById(R.id.buttonRecentEvents);
    Button btnAddNewEvent;
    Button btnExportEvents;
    Button btnSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRecentEvents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),RecentEvents.class);
                startActivity(i);
            }
        });
    }


}
