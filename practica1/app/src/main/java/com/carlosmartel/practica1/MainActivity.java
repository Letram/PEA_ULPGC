package com.carlosmartel.practica1;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int initialPoints = getResources().getInteger(R.integer.initialPoints);

        TextView[] scores = new TextView[2];
        scores[0] = findViewById(R.id.score1);
        scores[1] = findViewById(R.id.score2);
        for (TextView tv :
                scores) {
            tv.setText(String.valueOf(initialPoints));
        }
        int currentPlayer = getResources().getInteger(R.integer.currentPlayer);
        TextView currentPlayerTV = findViewById(R.id.currentPlayer);

        //TODO try to removethis warnings
        currentPlayerTV.setText(String.format("%s%d", getResources().getString(R.string.current_player), currentPlayer));
        TextView turnPoints = findViewById(R.id.turnScoreText);
        turnPoints.setText(String.format("%s%d", getResources().getString(R.string.turnScore), initialPoints));
        int goal = R.integer.goal;

        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.StartPlayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                findViewById(R.id.rollBtn).setVisibility(View.VISIBLE);
                System.out.println("HOLI");
            }
        });
        findViewById(R.id.rollBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO day 1 ended here
                int diceValue = (int) Math.floor(Math.random()*getResources().getInteger(R.integer.maxDiceValue)+getResources().getInteger(R.integer.minDiceValue));
                System.out.println(diceValue);
            }
        });
    }
}
