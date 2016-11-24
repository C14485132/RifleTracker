package com.example.phili.rifletracker;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.content.DialogInterface;
import android.widget.Toast;

import java.util.ArrayList;

public class AddNewEvent extends AppCompatActivity {

    public TextView fieldEventName;
    public TextView fieldShooterName;
    public Button btnAddShooter;
    public ListView listOfShooters;
    public Button btnBegin;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> arrayOfShooters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
        setTitle("Add new event");

        //Initialising the onscreen elements
        fieldEventName   = (TextView) findViewById(R.id.tbEventName);
        fieldShooterName = (TextView) findViewById(R.id.tbShooterName);
        btnAddShooter    = (Button) findViewById(R.id.buttonAddShooter);
        listOfShooters   = (ListView) findViewById(R.id.listOfShooters);
        btnBegin         = (Button) findViewById(R.id.buttonBegin);
        arrayOfShooters  = new ArrayList<>();


        //Button event programming
        btnAddShooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //As long as there's something in the Shooter field, add it
                if (fieldShooterName.getText().toString().length() != 0) {
                    arrayOfShooters.add(fieldShooterName.getText().toString());
                }

                adapter = new ArrayAdapter<>
                        (getApplicationContext(), R.layout.simple_list_item_1, arrayOfShooters);
                listOfShooters.setAdapter(adapter);
                fieldShooterName.setText("");
            }
        });

        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check to see if everything is good to go
                if (fieldEventName.getText().toString().length() == 0 || adapter.getCount() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Can't begin; make sure you set an event name and at least one shooter.",
                            Toast.LENGTH_LONG).show();

                } else {
                    //Bundle the variables and send them to the next activity
                    Bundle bundle = new Bundle();
                    Intent i = new Intent(getApplicationContext(),CurrentShooter.class);

                    bundle.putString("eventName", fieldEventName.getText().toString());
                    bundle.putStringArrayList("arrayOfShooters", arrayOfShooters);
                    i.putExtras(bundle);

                    startActivity(i);
                }
            }
        });

        listOfShooters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                //Calling confirmDelete method
                confirmDelete(adapter.getItem(position));
            }
        });
    }

    //Referenced from: http://stackoverflow.com/questions/23254750/
    //Takes in a position from the ListView, and then removes it if the user wants it gone
    private void confirmDelete(final String remove) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Remove");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want remove \"" + remove + "\" from the list?");

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                adapter.remove(remove);
                Toast.makeText(getApplicationContext(), "\"" + remove + "\" removed",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }
}
