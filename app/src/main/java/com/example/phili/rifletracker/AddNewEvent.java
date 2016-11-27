package com.example.phili.rifletracker;

/*Add New Event
*
* - Screen that allows the user to set up a new event.
* - They enter the Event Name and the shooters
* - The shooters are then checked to see if they're unique, and then added to the ListView if they are
* - The user can tap a name to remove it
* - Once Begin is selected, it checks to see if both Event Name and listView have something in them
* - It checks to see if the event name is already in use and prompts the user to make a new name
*   if it is in use
* - The data is bundled up and send to CurrentShooter is everything is good
*
* */


import android.content.Intent;
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
import java.util.Collections;

public class AddNewEvent extends AppCompatActivity {

    public TextView fieldEventName;
    public TextView fieldShooterName;
    public Button btnAddShooter;
    public ListView listOfShooters;
    public Button btnBegin;
    public ArrayAdapter<String> adapter = null;
    public ArrayList<String> arrayOfShooters;
    Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
        setTitle("Add new event");

        //Initialising the elements
        fieldEventName   = (TextView) findViewById(R.id.tbEventName);
        fieldShooterName = (TextView) findViewById(R.id.tbShooterName);
        btnAddShooter    = (Button) findViewById(R.id.buttonAddShooter);
        listOfShooters   = (ListView) findViewById(R.id.listOfShooters);
        btnBegin         = (Button) findViewById(R.id.buttonBegin);
        arrayOfShooters  = new ArrayList<>();
        db               = new Database(this);


        //Button event programming
        btnAddShooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //As long as there's something in the Shooter field, add it to the arraylist
                if (arrayOfShooters.size() >= 10) {
                    Toast.makeText(getApplicationContext(),
                            "Error: The shooter limit has been reached",
                            Toast.LENGTH_LONG).show();
                } else if (fieldShooterName.getText().toString().length() != 0) {
                    //Is it already in the list?
                    if (arrayOfShooters.contains(fieldShooterName.getText().toString())) {
                        //Display error
                        Toast.makeText(getApplicationContext(),
                                "Error: That name has already been added",
                                Toast.LENGTH_LONG).show();
                    } else {
                        //Then add the string to the ListView
                        //Reversed twice to reverse it, add it to the end, then re-reverse it so
                        //it's added to the top
                        Collections.reverse(arrayOfShooters);
                        arrayOfShooters.add(fieldShooterName.getText().toString());
                        Collections.reverse(arrayOfShooters);
                        adapter = new ArrayAdapter<>
                            (getApplicationContext(), R.layout.simple_list_item_1, arrayOfShooters);
                        listOfShooters.setAdapter(adapter);
                        fieldShooterName.setText("");
                    }
                }
            }
        });

        //Begin button takes the arrayview and name are populated, checks if the eventname already
        //exists in the database, and then moves onto CurrentShooter
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check to see if everything is good to go
                if (fieldEventName.getText().toString().length() == 0 || arrayOfShooters.size() == 0) {

                    //Let them know there needs to be at least one shooter
                    Toast.makeText(getApplicationContext(),
                            "Can't begin; make sure you set an event name and at least one shooter",
                            Toast.LENGTH_LONG).show();

                } else if (db.eventExists(fieldEventName.getText().toString())) {
                    //Check to see if the database has events with the same name first
                    Toast.makeText(getApplicationContext(),
                        "Can't begin; that event name is already in use. Please pick another name.",
                        Toast.LENGTH_LONG).show();
                } else {
                    //Bundle the variables and send them to the next activity
                    Bundle bundle = new Bundle();
                    Intent i = new Intent(getApplicationContext(),CurrentShooter.class);

                    //Before the shooters are sent. put them back in order of added, oldest firstNam
                    Collections.reverse(arrayOfShooters);

                    bundle.putString("eventName", fieldEventName.getText().toString());
                    bundle.putStringArrayList("arrayOfShooters", arrayOfShooters);
                    i.putExtras(bundle);

                    startActivity(i);
                    finish();
                }
            }
        });

        //When an element is clicked in listView, delete the clicked elemeent
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
