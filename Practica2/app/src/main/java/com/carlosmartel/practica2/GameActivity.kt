package com.carlosmartel.practica2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.carlosmartel.practica2.Fragments.DiceImageFragment
import com.carlosmartel.practica2.Fragments.PlayerScoreFragment
import com.carlosmartel.practica2.Fragments.PlayerTurnInfoFragment
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.image_fragment.*

class GameActivity : AppCompatActivity(),
    PlayerScoreFragment.PlayerScoreInterface,
    PlayerTurnInfoFragment.PlayerTurnInfoInterface{
    override var currentPlayer: Int = -1

    private var diceImageFragment : DiceImageFragment? = null
    private var playerScoreFragment : PlayerScoreFragment? = null
    private var playerTurnFragment : PlayerTurnInfoFragment? = null

    override fun roll() {
        val diceValue = diceImageFragment!!.getNumber()
        supportFragmentManager.beginTransaction().show(diceImageFragment!!).commit()
        playerTurnFragment?.setAccValue(diceValue)
    }

    override fun collect() {
        currentPlayer = if (currentPlayer == 1) 2 else 1
        playerTurnFragment?.resetAccScore()
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

        currentPlayer = resources.getInteger(R.integer.currentPlayer)

        diceImageFragment = supportFragmentManager.findFragmentById(R.id.imageFragment) as DiceImageFragment
        supportFragmentManager.beginTransaction().hide(diceImageFragment!!).commit()

        playerScoreFragment = supportFragmentManager.findFragmentById(R.id.playerScoreFragment) as PlayerScoreFragment

        playerTurnFragment = supportFragmentManager.findFragmentById(R.id.turnInfoFragment) as PlayerTurnInfoFragment

    }
}
