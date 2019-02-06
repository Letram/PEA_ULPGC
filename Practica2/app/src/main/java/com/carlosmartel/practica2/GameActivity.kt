package com.carlosmartel.practica2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val extras = intent.extras ?:return
        println(extras.getString("EXTRA_GAMEMODE"))
    }
}
