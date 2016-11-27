package com.example.phili.rifletracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class RecentEventEdit extends AppCompatActivity {

    Database db;
    ArrayList<String> shooterList;
    String eventName;
    Spinner spinner;
    ArrayAdapter<String> adapter;
    Button buttonEventEditRename;
    Button buttonEventEditScore;
    Button buttonEventEditDelete;
    ArrayList oneToTen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_event_edit);
        setTitle("Recent events");

        Bundle bundle = getIntent().getExtras();
        eventName = bundle.getString("eventName");

        db = new Database(this);
        shooterList = new ArrayList<String>();
        shooterList = db.listOfNamesInEvent(eventName);
        spinner = (Spinner)findViewById(R.id.spinner);
        buttonEventEditRename = (Button)findViewById(R.id.buttonEventEditRename);
        buttonEventEditScore = (Button)findViewById(R.id.buttonEventEditScore);
        buttonEventEditDelete = (Button)findViewById(R.id.buttonEventEditDelete);

        adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, shooterList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        //Add new event programming
        buttonEventEditRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renameShooter(spinner.getSelectedItem().toString());
            }
        });

        //Add new event programming
        buttonEventEditScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editScore(spinner.getSelectedItem().toString());
            }
        });

        //Add new event programming
        buttonEventEditDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteShooter(spinner.getSelectedItem().toString());
            }
        });
    }

    public void renameShooter (final String shooterName) {
        final AlertDialog.Builder alertDialogText = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialogText.setView(input);

        alertDialogText.setTitle("Renaming " + shooterName);

        // Setting Negative "NO" Button
        alertDialogText.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Setting Positive "Yes" Button
        alertDialogText.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                db.renameShooter(shooterName, input.getText().toString());

                shooterList.set(adapter.getPosition(shooterName), input.getText().toString());
                adapter = null;
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.spinner_item, shooterList);
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

    public void editScore (final String shooter) {
        final AlertDialog.Builder alertDialogSpinner1 = new AlertDialog.Builder(this);
        final AlertDialog.Builder alertDialogSpinner2 = new AlertDialog.Builder(this);
        final AlertDialog.Builder alertDialogSpinner3 = new AlertDialog.Builder(this);
        final Spinner input1 = new Spinner(this);
        final Spinner input2 = new Spinner(this);
        final Spinner input3 = new Spinner(this);

        oneToTen = new ArrayList<Integer>();
        for (int i = 0;i<11;i++) {
            oneToTen.add(i);
        }
        ArrayAdapter<Integer> inputAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, oneToTen);
        inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final int rndScores[] = new int[3];

        input1.setAdapter(inputAdapter);
        input2.setAdapter(inputAdapter);
        input3.setAdapter(inputAdapter);
        alertDialogSpinner1.setView(input1);
        alertDialogSpinner2.setView(input2);
        alertDialogSpinner3.setView(input3);

        alertDialogSpinner1.setTitle("Editing Round 1");
        alertDialogSpinner2.setTitle("Editing Round 2");
        alertDialogSpinner3.setTitle("Editing Round 3");

        // Setting Negative "NO" Button
        alertDialogSpinner1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Setting Positive "Yes" Button
        alertDialogSpinner1.setPositiveButton("Set Round 1 score", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                rndScores[0] = (Integer) input1.getSelectedItem();
                dialog.cancel();

                alertDialogSpinner1.setView(input2);

                // Setting Negative "NO" Button
                alertDialogSpinner2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Setting Positive "Yes" Button
                alertDialogSpinner2.setPositiveButton("Set Round 2 score", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        rndScores[1] = (Integer) input2.getSelectedItem();
                        dialog.cancel();

                        alertDialogSpinner2.setView(input2);

                        // Setting Negative "NO" Button
                        alertDialogSpinner3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        // Setting Positive "Yes" Button
                        alertDialogSpinner3.setPositiveButton("Set Round 3 score", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                rndScores[2] = (Integer) input3.getSelectedItem();
                                dialog.cancel();

                                alertDialogSpinner3.setView(input3);

                                //edit DB
                                db.editScores(rndScores, shooter);
                            }
                        });

                        alertDialogSpinner3.show();

                    }
                });

                alertDialogSpinner2.show();

            }
        });

        alertDialogSpinner1.show();
    }

    public void deleteShooter (final String shooterName) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Delete");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to delete the shooter \"" + shooterName + "\"?");

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                db.deleteShooter(shooterName);
                Toast.makeText(getApplicationContext(), "Deleted successfully!",
                        Toast.LENGTH_LONG).show();

                adapter.remove(shooterName);
                spinner.setAdapter(adapter);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
