package com.example.phili.rifletracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Vibrator;
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
        currentShooterPosition = 0;

        //Populating textScoreArray with the textScore textviews
        for(int i=0;i<textScoreArray.length;i++) {
            String name = "textScore" + Integer.toString(i);
            textScoreArray[i] = (TextView)findViewById
                    (getResources().getIdentifier(name, "id", getPackageName()));
        }


        //Getting the variables from the last activity
        Bundle bundle = getIntent().getExtras();
        eventName = bundle.getString("eventName");
        arrayOfShooters = bundle.getStringArrayList("arrayOfShooters");

        setTitle("Current shooter: " + arrayOfShooters.get(this.currentShooterPosition));

        //Button event programming

        //Kill: on press, increment total counter, mark as hit, and vibrate
        buttonKill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textScoreArray[currentPosition].setText(getString(R.string.kill_score));
                currentScore++;
                currentPosition++;
                textScoreTotal.setText(Integer.toString(currentScore));
                vib.vibrate(300);

                if (currentPosition == 8) {
                    //Vibrate twice to let them know last two are up
                    long[] two = {0,150,100,150};
                    vib.vibrate(two, -1);
                }

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
                textScoreArray[currentPosition].setText(getString(R.string.miss_score));
                currentPosition++;
                vib.vibrate(150);

                if (currentPosition == 8) {
                    //Vibrate twice to let them know last two are up
                    long[] two = {0,150,100,150};
                    vib.vibrate(two, -1);
                }

                System.out.println(currentPosition);
                if (currentPosition == 10) {
                    //Reset the current scores
                    nextShooter();
                }

            }
        });

        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //Current shooter is finished, onto the next one.
    public void nextShooter() {
        currentShooterPosition++;

        //If all shooters are done:
        if (currentShooterPosition == arrayOfShooters.size()) {
            //TODO: Once all the shooters are done
        } else {
            //TODO: Do stuff to display the score of that shooter, and list the next shooter
            currentScore = 0;
            currentPosition = 0;
            setTitle("Current shooter: " + arrayOfShooters.get(currentShooterPosition));

            for (int i=0;i<textScoreArray.length;i++) {
                textScoreArray[i].setText(getString(R.string.null_score));
            }
            textScoreTotal.setText(Integer.toString(currentScore));
        }
    }
}
