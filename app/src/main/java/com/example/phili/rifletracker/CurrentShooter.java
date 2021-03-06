package com.example.phili.rifletracker;

/* CurrentShooter
*
* - CurrentShooter is how scores are entered in for the first time
* - The shooters are sorted by the order they were entered in when in AddNewEvent
* - The user can skip a shooter at any time
* - Tally live updates, as well as score
* - Pressing Kill or Miss will make it vibrate, long for kill, short for miss
* - The phone will vibrate twice to indicate last 2 shots
* - After all shots are done from that shooter for that round, their score and the next shooter's
*   name are displayed.
* - If it's the last shooter of the round, the round's results are displayed.
* - After all rounds are done, the popup lets the user know, and them moves onto RecentEventView
* - The data is added to the database, and the event name is passed to RecentEventView as a param
*
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Vibrator;
import android.widget.Toast;

import java.util.ArrayList;

public class CurrentShooter extends AppCompatActivity {

    String eventName;
    ArrayList<String> arrayOfShooters;
    Button buttonKill;
    Button buttonMiss;
    Button buttonUndo;
    Button buttonSkip;
    TextView[] textScoreArray;
    TextView textScoreTotal;
    Database db;
    Bundle sendData;
    int currentScore;
    int currentPosition;
    int currentShooterPosition;
    public Vibrator vib;
    int currentRound;
    int[][] scorecard;
    int numOfShooters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_shooter);

        //Initialization
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        textScoreArray = new TextView[10];
        textScoreTotal = (TextView) findViewById(R.id.textScoreTotal);
        buttonKill = (Button) findViewById(R.id.buttonKill);
        buttonMiss = (Button) findViewById(R.id.buttonMiss);
        buttonUndo = (Button) findViewById(R.id.buttonUndo);
        buttonSkip = (Button) findViewById(R.id.buttonSkip);
        currentScore = 0;
        currentPosition = 0;
        currentRound = 1;
        currentShooterPosition = 0;
        numOfShooters = 0;
        db = new Database(this);

        //Populating textScoreArray with the textScore textviews so they can be easily changed later
        for (int i = 0; i < textScoreArray.length; i++) {
            String name = "textScore" + Integer.toString(i);
            textScoreArray[i] = (TextView) findViewById
                    (getResources().getIdentifier(name, "id", getPackageName()));
        }

        //Getting the variables from the last activity
        Bundle bundle = getIntent().getExtras();
        eventName = bundle.getString("eventName");
        arrayOfShooters = bundle.getStringArrayList("arrayOfShooters");

        scorecard = new int[3][arrayOfShooters.size()];

        setTitle("Current shooter: " + arrayOfShooters.get(currentShooterPosition));

        //Button event programming

        //Kill: on press, increment total counter, mark as hit, and long vibrate
        buttonKill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change the current tally to kill
                textScoreArray[currentPosition].setText(getString(R.string.kill_score));
                currentScore++;
                currentPosition++;
                textScoreTotal.setText(Integer.toString(currentScore));
                vib.vibrate(300);

                //Last 2 shots warning = 2 vibrates, one long, one short
                if (currentPosition == 8) {
                    //Vibrate twice to let them know last two are up
                    long[] two = {0, 300, 100, 150};
                    vib.vibrate(two, -1);
                }

                //Turn's over, next up
                if (currentPosition == 10) {
                    //Reset the current scores
                    nextShooter();
                }
            }
        });

        //Miss: on press, do the same as kill, but different
        buttonMiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change tally so thow miss
                textScoreArray[currentPosition].setText(getString(R.string.miss_score));
                currentPosition++;
                vib.vibrate(150);

                //Last 2 shots warning = 2 vibrates, both short
                if (currentPosition == 8) {
                    //Vibrate twice to let them know last two are up
                    long[] two = {0, 150, 100, 150};
                    vib.vibrate(two, -1);
                }

                //Turn's over, next up
                if (currentPosition == 10) {
                    //Reset the current scores
                    nextShooter();
                }

            }
        });

        //Remove the last shot and the score
        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition != 0) {
                    confirmUndo();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to undo; nothing to undo",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //Skip the current user
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSkip();
            }
        });
    }

    //Current shooter is finished, onto the next one.
    public void nextShooter() {
        scorecard[currentRound - 1][currentShooterPosition] = currentScore;
        currentShooterPosition++;

        //If all shooters are done:
        if (currentShooterPosition == arrayOfShooters.size()) {
            String send = "Round " + currentRound + " finished.\n\n";

            //Show all scores for that round
            for (int i = 0; i < arrayOfShooters.size(); i++) {
                send += arrayOfShooters.get(i) + "'s score for this round: "
                        + scorecard[currentRound - 1][i] + "\n";
            }

            //Checking to see if all rounds are over
            if (currentRound == 3) {

                send += "\nShooting's over!";
                //Done shooting
                sendData = new Bundle();
                sendData.putString("eventName", eventName);

                displayDialog(send);
            } else {
                //next round, keep shooting
                currentShooterPosition = 0;
                send += "\nNext up: " + arrayOfShooters.get(currentShooterPosition);
                displayDialog("Score", send);

                currentRound++;
                currentScore = 0;
                currentPosition = 0;
                setTitle("Current shooter: " + arrayOfShooters.get(currentShooterPosition));

                for (int i = 0; i < textScoreArray.length; i++) {
                    textScoreArray[i].setText(getString(R.string.null_score));
                }
                textScoreTotal.setText(Integer.toString(currentScore));

            }

        } else {
            //Continue to next shooter in the round
            displayDialog("Score", arrayOfShooters.get(currentShooterPosition - 1) + "'s score: " +
                    currentScore + "\nNext up: " + arrayOfShooters.get(currentShooterPosition));

            currentScore = 0;
            currentPosition = 0;
            setTitle("Current shooter: " + arrayOfShooters.get(currentShooterPosition));

            //Resetting the tally and score
            for (int i = 0; i < textScoreArray.length; i++) {
                textScoreArray[i].setText(getString(R.string.null_score));
            }
            textScoreTotal.setText(Integer.toString(currentScore));
        }
    }

    //Just a dismissable popup box with scores.
    private void displayDialog(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting dismiss button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void displayDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Score");

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting dismiss button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Add current stuff to the database
                for (int i = 0; i < arrayOfShooters.size(); i++) {
                    db.insert(arrayOfShooters.get(i), eventName, scorecard[0][i],
                            scorecard[1][i], scorecard[2][i]);
                }

                //Next activity
                //Bundle and send
                Intent i = new Intent(getApplicationContext(), RecentEventView.class);
                i.putExtras(sendData);
                startActivity(i);
                finish();
            }
        });

        alertDialog.show();
    }

    //Seeing id the user really wants to skip
    private void confirmSkip() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Skip");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to skip " +
                arrayOfShooters.get(currentShooterPosition) + "'s turn?");

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Skipping...
                scorecard[currentRound][currentShooterPosition] = 0;

                //Toast to confirm
                Toast.makeText(getApplicationContext(), arrayOfShooters.get(currentShooterPosition)
                        + " skipped", Toast.LENGTH_LONG).show();

                nextShooter();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    //Seeing id the user really wants to skip
    private void confirmUndo() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Undo");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to undo the last shot?");

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Undoing...
                if (currentPosition == 0) {
                    Toast.makeText(getApplicationContext(), "Error: No more shots to undo.",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (textScoreArray[currentPosition - 1].getText().toString().charAt(0) == 'X') {
                        currentScore--;
                        textScoreTotal.setText(Integer.toString(currentScore));
                    }
                    currentPosition--;
                    textScoreArray[currentPosition].setText(R.string.null_score);
                    Toast.makeText(getApplicationContext(), "Successfully undone.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }
}
