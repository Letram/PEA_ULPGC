package com.carlosmartel.practica1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int[] scores;
    int accumulatedPoints, currentPlayer, goal;

    TextView[] scoresTV;
    TextView turnPoints, currentPlayerTV, startPlayer;

    ImageView diceImg;

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

        //Images
        diceImg = findViewById(R.id.diceImg);
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
                if(diceImg.getVisibility() != View.VISIBLE)
                    diceImg.setVisibility(View.VISIBLE);
                switch (diceValue){
                    case 1:
                        diceImg.setImageResource(Dice.ONE.getDiceValue());
                        break;
                    case 2:
                        diceImg.setImageResource(Dice.TWO.getDiceValue());
                        break;
                    case 3:
                        diceImg.setImageResource(Dice.THREE.getDiceValue());
                        break;
                    case 4:
                        diceImg.setImageResource(Dice.FOUR.getDiceValue());
                        break;
                    case 5:
                        diceImg.setImageResource(Dice.FIVE.getDiceValue());
                        break;
                    case 6:
                        diceImg.setImageResource(Dice.SIX.getDiceValue());
                        break;
                }
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getString(R.string.alertTitle))
                .setMessage(getResources().getString(R.string.alertQuestion))
                .setPositiveButton(getResources().getString(R.string.alertYes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //super.onBackPressed();
                        //Or used finish();
                        finish();
                    }

                })
                .setNegativeButton(getResources().getString(R.string.alertNo), null)
                .show();
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
