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

    boolean win;
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

        //bool
        win = false;

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
                if(accumulatedPoints + scores[currentPlayer-1] >= goal){
                    playerWin();
                }else{
                    if(accumulatedPoints == 0)swapPlayers();
                    else{
                        if(collectBtn.getVisibility() != View.VISIBLE)
                            collectBtn.setVisibility(View.VISIBLE);
                    }
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

    private void playerWin() {
        scores[currentPlayer-1] = accumulatedPoints + scores[currentPlayer-1] > goal ? goal : accumulatedPoints + scores[currentPlayer-1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getString(R.string.wintTitle))
                .setMessage(getString(R.string.winDescription, getString(R.string.current_player, currentPlayer)))
                .setPositiveButton(getResources().getString(R.string.winNewGame), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showTexts();
                        scores[0] = scores[1] = getResources().getInteger(R.integer.initialPoints);
                        accumulatedPoints = getResources().getInteger(R.integer.initialPoints);
                        swapPlayers();
                    }

                })
                .setNegativeButton(getResources().getString(R.string.winExit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
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
        currentPlayer = currentPlayer == 1 ? 2 : 1;
        startPlayer.setText(getString(R.string.start, getString(R.string.current_player, currentPlayer)));

        //Reset button views
        rollBtn.setVisibility(View.GONE);
        collectBtn.setVisibility(View.GONE);
        startPlayer.setVisibility(View.VISIBLE);

        showTexts();
    }

    private void showTexts(){
        for (int i = 0; i < scoresTV.length; i++) {
            scoresTV[i].setText(String.valueOf(scores[i]));
        }
        currentPlayerTV.setText(currentPlayer == getResources().getInteger(R.integer.PlayerOne) ? getResources().getString(R.string.player1) : getResources().getString(R.string.player2));
        turnPoints.setText(getString(R.string.turnScore, accumulatedPoints));
        progressBar1.setProgress(scores[0]);
        progressBar2.setProgress(scores[1]);
    }
}
