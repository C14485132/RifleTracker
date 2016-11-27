package com.example.phili.rifletracker;

/* RecentEventEdit
*
* - Select a shooter from the spinner
* - The user can either edit, rename or delete the shooter.
* - Renaming it will prompt the user to reenter a new name, and confirm or deny
* - Deleting it will delete all records from the db, and remove it from the spinner
* - Editing it will pull up 3 boxes, each corresponding to a round score that needs to be edited.
*
*/

import android.content.DialogInterface;
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
    ArrayList zeroToTen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_event_edit);
        setTitle("Recent events");

        //Getting stuff from last activity
        Bundle bundle = getIntent().getExtras();
        eventName = bundle.getString("eventName");

        //Initializing
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

        //Is there anything in the spinner?
        //If there's nothing, don't continue
        try {
            System.out.println(spinner.getSelectedItem().toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No shooters to edit in this event!",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        //Add new event programming
        buttonEventEditRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //try to rename the shooter as long as there's something in the spinner
                try {
                    renameShooter(spinner.getSelectedItem().toString());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No shooters to rename! Event has " +
                            "been removed because there are no shooters in the event.",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        //Add new event programming
        buttonEventEditScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    editScore(spinner.getSelectedItem().toString());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No shooters to edit! Event has been " +
                            "removed because there are no shooters in the event.",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        //Add new event programming
        buttonEventEditDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteShooter(spinner.getSelectedItem().toString());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No shooters to delete! Event has " +
                            "been removed because there are no shooters in the event.",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    //Pops up dialog asking to rename shooter.
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
                db.renameShooter(shooterName, input.getText().toString(), eventName);

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

    //Three popup boxes, each one has a spinner and will save the value
    public void editScore (final String shooter) {

        //Initialisation
        //Spinners and dialogs
        final AlertDialog.Builder alertDialogSpinner1 = new AlertDialog.Builder(this);
        final AlertDialog.Builder alertDialogSpinner2 = new AlertDialog.Builder(this);
        final AlertDialog.Builder alertDialogSpinner3 = new AlertDialog.Builder(this);
        final Spinner input1 = new Spinner(this);
        final Spinner input2 = new Spinner(this);
        final Spinner input3 = new Spinner(this);

        //Creating and populating zeroToTen
        zeroToTen = new ArrayList<Integer>();
        for (int i = 0;i<11;i++) {
            zeroToTen.add(i);
        }
        ArrayAdapter<Integer> inputAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, zeroToTen);
        inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Storingthe three new scores
        final int rndScores[] = new int[3];

        //Adding to the views
        input1.setAdapter(inputAdapter);
        input2.setAdapter(inputAdapter);
        input3.setAdapter(inputAdapter);
        alertDialogSpinner1.setView(input1);
        alertDialogSpinner2.setView(input2);
        alertDialogSpinner3.setView(input3);

        //Titles for dialogs
        alertDialogSpinner1.setTitle("Editing Round 1");
        alertDialogSpinner2.setTitle("Editing Round 2");
        alertDialogSpinner3.setTitle("Editing Round 3");

        // Setting Negative "NO" Button
        alertDialogSpinner1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        /*
        FIRST POPUP BEGIN
         */
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

                /*
                SECOND POPUP BEGIN
                 */
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

                        /*
                        THIRD POPUP BEGIN
                         */
                        alertDialogSpinner3.setPositiveButton("Set Round " +
                                "3 score", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                rndScores[2] = (Integer) input3.getSelectedItem();
                                dialog.cancel();

                                alertDialogSpinner3.setView(input3);

                                //Finally, edit the DB
                                db.editScores(rndScores, shooter, eventName);
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

    //Dialog to delete shooter, option to cancel
    public void deleteShooter (final String shooterName) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Delete");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to delete the shooter \"" + shooterName +
                "\"?");

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                db.deleteShooter(shooterName, eventName);
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
