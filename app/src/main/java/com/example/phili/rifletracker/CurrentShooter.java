package com.example.phili.rifletracker;

import android.content.DialogInterface;
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
    int currentScore;
    int currentPosition;
    int currentShooterPosition;
    public Vibrator vib;
    int currentRound;
    int[][] scorecard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_shooter);

        //Initialization
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        arrayOfShooters = new ArrayList<>();
        textScoreArray  = new TextView[10];
        textScoreTotal  = (TextView)findViewById(R.id.textScoreTotal);
        buttonKill      = (Button)findViewById(R.id.buttonKill);
        buttonMiss      = (Button)findViewById(R.id.buttonMiss);
        buttonUndo      = (Button)findViewById(R.id.buttonUndo);
        buttonSkip      = (Button)findViewById(R.id.buttonSkip);
        currentScore    = 0;
        currentPosition = 0;
        currentRound    = 1;
        currentShooterPosition = 0;
        scorecard = new int[3][arrayOfShooters.size()];

        //Populating textScoreArray with the textScore textviews so they can be easily changed later
        for(int i=0;i<textScoreArray.length;i++) {
            String name = "textScore" + Integer.toString(i);
            textScoreArray[i] = (TextView)findViewById
                    (getResources().getIdentifier(name, "id", getPackageName()));
        }


        //Getting the variables from the last activity
        Bundle bundle = getIntent().getExtras();
        eventName = bundle.getString("eventName");
        arrayOfShooters = bundle.getStringArrayList("arrayOfShooters");

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
                    long[] two = {0,300,100,150};
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
                    long[] two = {0,150,100,150};
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
                //TODO: Undo
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
        currentShooterPosition++;

        //If all shooters are done:
        if (currentShooterPosition == arrayOfShooters.size()) {
            String send = "Round " + currentRound + " finished.\n" +
                    arrayOfShooters.get(currentShooterPosition - 1) + "'s score: " + currentScore;

            //Checking to see if all rounds are over
            if (currentRound == 3) {
                //Done shooting
                scoreDialog(send);

                //Next activity
            } else {
                //next round, keep shooting
                currentShooterPosition = 0;
                send += "\nNext up: " + arrayOfShooters.get(currentShooterPosition);
                scoreDialog(send);

                currentRound++;
                currentScore = 0;
                currentPosition = 0;
                setTitle("Current shooter: " + arrayOfShooters.get(currentShooterPosition));

                for (int i=0;i<textScoreArray.length;i++) {
                    textScoreArray[i].setText(getString(R.string.null_score));
                }
                textScoreTotal.setText(Integer.toString(currentScore));

            }

        } else {
            scoreDialog(arrayOfShooters.get(currentShooterPosition - 1) + "'s score: " +
                    currentScore + "\nNext up: " + arrayOfShooters.get(currentShooterPosition));

            currentScore = 0;
            currentPosition = 0;
            setTitle("Current shooter: " + arrayOfShooters.get(currentShooterPosition));

            for (int i=0;i<textScoreArray.length;i++) {
                textScoreArray[i].setText(getString(R.string.null_score));
            }
            textScoreTotal.setText(Integer.toString(currentScore));
        }
    }

    private void scoreDialog (String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Score");

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
        alertDialog.setPositiveButton("YES",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                //Skipping...
                //TODO: Skip stuff

                //Toast to confirm
                Toast.makeText(getApplicationContext(), arrayOfShooters.get(currentShooterPosition)
                        + " skipped", Toast.LENGTH_LONG).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }
}
