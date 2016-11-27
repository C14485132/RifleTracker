package com.example.phili.rifletracker;

/* RecentEventView
*
* - Just receives an event name from a bundle, and fetches corresponding data from the database
*   and adds it to various TextViews
* - A button can return you to the main menu.
*
*/

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.database.DatabaseUtils.dumpCursorToString;

public class RecentEventView extends AppCompatActivity {

    String eventName;
    Database db;
    Cursor eventOutputCursor;
    TextView textScoreArray[][];
    TextView textNameArray[];
    TextView textTotalArray[];
    Button btnMainMenu;
    int noOfRowsReturned;
    int iRnd[];
    int iName;
    int total;
    int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_event_view);

        Bundle bundle = getIntent().getExtras();
        eventName = bundle.getString("eventName");


        //Initialisation
        db                  = new Database(this);
        eventOutputCursor   = db.selectEvent(eventName);
        System.out.println(dumpCursorToString(eventOutputCursor));
        noOfRowsReturned    = 0;
        iRnd = new int[3];
        currentIndex = 0;

        iName = eventOutputCursor.getColumnIndex("Shooter_Name");
        iRnd[0] = eventOutputCursor.getColumnIndex("Round1");
        iRnd[1] = eventOutputCursor.getColumnIndex("Round2");
        iRnd[2] = eventOutputCursor.getColumnIndex("Round3");

        noOfRowsReturned = eventOutputCursor.getCount();
        System.out.println(noOfRowsReturned);


        textScoreArray  = new TextView[noOfRowsReturned][3];
        textNameArray   = new TextView[noOfRowsReturned];
        textTotalArray  = new TextView[noOfRowsReturned];

        //Populating the textView array with the scores and names
        for (int i = 0; i < noOfRowsReturned; i++) {
            textNameArray[i] = (TextView) findViewById
                    (getResources().getIdentifier("textNameS" + Integer.toString(i), "id",
                            getPackageName()));
            textTotalArray[i] = (TextView) findViewById
                    (getResources().getIdentifier("textTotalS" + Integer.toString(i), "id",
                            getPackageName()));

            for (int j = 0; j < 3; j++) {
                textScoreArray[i][j] = (TextView) findViewById
                        (getResources().getIdentifier("textRnd" + Integer.toString(j + 1) + "S" +
                                Integer.toString(i), "id", getPackageName()));
            }
        }

        //Filling the TextViews with data
        for(eventOutputCursor.moveToFirst(); !eventOutputCursor.isAfterLast();eventOutputCursor.moveToNext()) {
            textNameArray[currentIndex].setText(eventOutputCursor.getString(iName));
            textScoreArray[currentIndex][0].setText(Integer.toString(eventOutputCursor.getInt(iRnd[0])));
            textScoreArray[currentIndex][1].setText(Integer.toString(eventOutputCursor.getInt(iRnd[1])));
            textScoreArray[currentIndex][2].setText(Integer.toString(eventOutputCursor.getInt(iRnd[2])));
            total = eventOutputCursor.getInt(iRnd[0]) + eventOutputCursor.getInt(iRnd[1]) + eventOutputCursor.getInt(iRnd[2]);
            textTotalArray[currentIndex].setText(Integer.toString(total));
            currentIndex++;
        }

        setTitle("Event: " + eventName);

        //Go back to the main menu
        btnMainMenu = (Button)findViewById(R.id.buttonMainMenu);

        btnMainMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
