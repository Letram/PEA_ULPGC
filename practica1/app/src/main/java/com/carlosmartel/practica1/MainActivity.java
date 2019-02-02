package com.carlosmartel.practica1;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int[] scores;
    int accumulatedPoints, currentPlayer, goal;

    TextView[] scoresTV;
    TextView turnPoints, currentPlayerTV, startPlayer;

    Button rollBtn, collectBtn;

    ProgressBar progressBar1, progressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //int values
        currentPlayer = getResources().getInteger(R.integer.currentPlayer);
        accumulatedPoints = getResources().getInteger(R.integer.initialPoints);
        scoresTV = new TextView[2];
        scores = new int[]{getResources().getInteger(R.integer.initialPoints), getResources().getInteger(R.integer.initialPoints)};
        goal = getResources().getInteger(R.integer.goal);

        //Texts
        scoresTV[0] = findViewById(R.id.score1);
        scoresTV[1] = findViewById(R.id.score2);
        currentPlayerTV = findViewById(R.id.currentPlayer);
        turnPoints = findViewById(R.id.turnScoreText);
        startPlayer = findViewById(R.id.StartPlayer);

        //Buttons
        rollBtn = findViewById(R.id.rollBtn);
        collectBtn = findViewById(R.id.collectBtn);

        //Progress bars
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);

        progressBar1.setMax(goal);
        progressBar2.setMax(goal);
        showTexts();
        setListeners();
    }

    private void setListeners() {
        startPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                findViewById(R.id.rollBtn).setVisibility(View.VISIBLE);
            }
        });
        rollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int diceValue = (int) Math.floor(Math.random()*getResources().getInteger(R.integer.maxDiceValue)+getResources().getInteger(R.integer.minDiceValue));
                accumulatedPoints = diceValue != 1 ? accumulatedPoints + diceValue : 0;
                if(accumulatedPoints == 0)swapPlayers();
                else{
                    if(collectBtn.getVisibility() != View.VISIBLE)
                        collectBtn.setVisibility(View.VISIBLE);
                }
                showTexts();
            }
        });
        collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapPlayers();
                showTexts();
            }
        });
    }

    private void swapPlayers(){
        //Reset counters and update components
        scores[currentPlayer-1] = accumulatedPoints + scores[currentPlayer-1] > goal ? goal : accumulatedPoints + scores[currentPlayer-1];
        accumulatedPoints = getResources().getInteger(R.integer.initialPoints);
        currentPlayer = currentPlayer == getResources().getInteger(R.integer.PlayerOne) ? getResources().getInteger(R.integer.PlayerTwo) : getResources().getInteger(R.integer.PlayerOne);
        startPlayer.setText(currentPlayer == getResources().getInteger(R.integer.PlayerOne)  ? getResources().getString(R.string.start1) : getResources().getString(R.string.start2));
        progressBar1.setProgress(scores[getResources().getInteger(R.integer.PlayerOne)-1]);
        progressBar2.setProgress(scores[getResources().getInteger(R.integer.PlayerTwo)-1]);

        //Reset button views
        rollBtn.setVisibility(View.GONE);
        collectBtn.setVisibility(View.GONE);
        startPlayer.setVisibility(View.VISIBLE);
    }

    private void showTexts(){
        for (int i = 0; i < scoresTV.length; i++) {
            scoresTV[i].setText(String.valueOf(scores[i]));
        }
        currentPlayerTV.setText(currentPlayer == getResources().getInteger(R.integer.PlayerOne) ? getResources().getString(R.string.player1) : getResources().getString(R.string.player2));
        turnPoints.setText(String.format("%s%d", getResources().getString(R.string.turnScore), accumulatedPoints));
    }
}
