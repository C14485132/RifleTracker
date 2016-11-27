package com.example.phili.rifletracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class RecentEvents extends AppCompatActivity {

    Database db;
    ArrayList<String> spinnerArray;
    Spinner spinner;
    Button buttonRecentEventsView;
    Button buttonRecentEventsEdit;
    Button buttonRecentEventsRename;
    Button buttonRecentEventsDelete;
    Bundle bundle;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_events);
        setTitle("Recent events");

        db = new Database(this);
        spinnerArray = new ArrayList<String>();
        buttonRecentEventsView = (Button)findViewById(R.id.buttonRecentEventsView);
        buttonRecentEventsEdit = (Button)findViewById(R.id.buttonRecentEventsEdit);
        buttonRecentEventsRename = (Button)findViewById(R.id.buttonRecentEventsRename);
        buttonRecentEventsDelete = (Button)findViewById(R.id.buttonRecentEventsDelete);
        bundle = new Bundle();


        spinnerArray = db.listOfEvents();

        adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        //Add new event programming
        buttonRecentEventsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("eventName", spinner.getSelectedItem().toString());

                //Launch RecentEventView
                Intent i = new Intent(getApplicationContext(),RecentEventView.class);

                i.putExtras(bundle);
                startActivity(i);
            }
        });

        //Add new event programming
        buttonRecentEventsEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("eventName", spinner.getSelectedItem().toString());

                //Launch RecentEventView
                Intent i = new Intent(getApplicationContext(),RecentEventEdit.class);

                i.putExtras(bundle);
                startActivity(i);
            }
        });

        //Add new event programming
        buttonRecentEventsRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renameEvent(spinner.getSelectedItem().toString());

            }
        });

        //Add new event programming
        buttonRecentEventsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(spinner.getSelectedItem().toString());
            }
        });
    }

    public void renameEvent (final String eventName) {
        final AlertDialog.Builder alertDialogText = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialogText.setView(input);

        alertDialogText.setTitle("Renaming " + eventName);

        // Setting Negative "NO" Button
        alertDialogText.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Setting Positive "Yes" Button
        alertDialogText.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                db.renameEvent(eventName, input.getText().toString());

                spinnerArray.set(adapter.getPosition(eventName), input.getText().toString());
                adapter = null;
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinner_item, spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner = null;
                spinner = (Spinner) findViewById(R.id.spinner);
                spinner.setAdapter(adapter);
                spinner.setSelection(adapter.getPosition(input.getText().toString()));
            }
        });

        // Showing Alert Message
        alertDialogText.show();
    }

    public void deleteEvent (final String eventName) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Delete");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to delete the event \"" + eventName + "\"?");

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                db.deleteEvent(eventName);
                Toast.makeText(getApplicationContext(), "Deleted successfully!",
                        Toast.LENGTH_LONG).show();

                adapter.remove(eventName);
                spinner.setAdapter(adapter);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
