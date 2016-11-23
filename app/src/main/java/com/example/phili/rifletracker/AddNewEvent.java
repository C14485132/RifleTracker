package com.example.phili.rifletracker;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class AddNewEvent extends AppCompatActivity {

    TextView fieldEventName;
    TextView fieldShooterName;
    Button btnAddShooter;
    ListView listOfShooters;
    Button btnRemove;
    Button btnBegin;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayOfShooters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        //Initialising the onscreen elements
        fieldEventName   = (TextView) findViewById(R.id.tbEventName);
        fieldShooterName = (TextView) findViewById(R.id.tbShooterName);
        btnAddShooter    = (Button) findViewById(R.id.buttonAddShooter);
        listOfShooters   = (ListView) findViewById(R.id.listOfShooters);
        btnRemove        = (Button) findViewById(R.id.buttonRemove);
        btnBegin         = (Button) findViewById(R.id.buttonBegin);
        arrayOfShooters  = new ArrayList<String>();


        //Button event programming
        btnAddShooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldShooterName.getText().toString().length() != 0) {
                    arrayOfShooters.add(fieldShooterName.getText().toString());
                }

                adapter = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.simple_list_item_1, arrayOfShooters);
                listOfShooters.setAdapter(adapter);
                fieldShooterName.setText("");
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check to see if everything is good to go
                if (fieldEventName.getText().toString().length() < 0 && adapter.getCount() < 0 ) {

                }
            }
        });

        /*
        listOfShooters.setOnItemClickListener(new View.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position) {
                ItemClicked item = adapter.getItemAtPosition(position);

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        */
    }
}
