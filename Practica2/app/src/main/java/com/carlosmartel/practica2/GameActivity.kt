package com.carlosmartel.practica2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.carlosmartel.practica2.Fragments.DiceImageFragment
import com.carlosmartel.practica2.Fragments.PlayerScoreFragment
import com.carlosmartel.practica2.Fragments.PlayerTurnInfoFragment
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity(),
    PlayerScoreFragment.PlayerScoreInterface,
    PlayerTurnInfoFragment.PlayerTurnInfoInterface{

    private var diceImageFragment : DiceImageFragment? = null
    private var playerScoreFragment : PlayerScoreFragment? = null
    private var playerTurnFragment : PlayerTurnInfoFragment? = null

    override fun roll() {
        print(diceImageFragment?.getNumber())
    }

    override fun collect() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAccScore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setScores() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        diceImageFragment = supportFragmentManager.findFragmentById(R.id.imageFragment) as DiceImageFragment
        playerScoreFragment = supportFragmentManager.findFragmentById(R.id.playerScoreFragment) as PlayerScoreFragment
        playerTurnFragment = supportFragmentManager.findFragmentById(R.id.turnInfoFragment) as PlayerTurnInfoFragment

    }
}
