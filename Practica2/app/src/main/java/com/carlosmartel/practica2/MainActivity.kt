package com.carlosmartel.practica2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.carlosmartel.practica2.Fragments.SelectPlayerFragment

class MainActivity : AppCompatActivity(), SelectPlayerFragment.SelectPlayerInterface {
    override fun startGame(mode: String) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(CustomData.EXTRA_GAMEMODE, mode)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
