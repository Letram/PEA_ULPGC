package com.carlosmartel.practica2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.carlosmartel.practica2.Fragments.DiceImageFragment
import com.carlosmartel.practica2.Fragments.PlayerScoreFragment
import com.carlosmartel.practica2.Fragments.PlayerTurnInfoFragment

class GameActivity : AppCompatActivity(),
    PlayerTurnInfoFragment.PlayerTurnInfoInterface{
    override var currentPlayer: Int = -1

    private var diceImageFragment : DiceImageFragment? = null
    private var playerScoreFragment : PlayerScoreFragment? = null
    private var playerTurnFragment : PlayerTurnInfoFragment? = null

    private var accPlayerValueInTurn : Int = 0

    override fun roll() {
        val diceValue = diceImageFragment!!.getNumber()
        if(diceImageFragment!!.isHidden)
            supportFragmentManager.beginTransaction().show(diceImageFragment!!).commit()
        accPlayerValueInTurn = playerTurnFragment!!.setAccValue(diceValue)
        if(accPlayerValueInTurn == 0)
            collect()
    }

    override fun collect() {
        playerScoreFragment?.setScore(currentPlayer, accPlayerValueInTurn)
        currentPlayer = if (currentPlayer == 1) 2 else 1
        playerTurnFragment?.resetAccScore()
        accPlayerValueInTurn = 0
    }

    override fun setAccScore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        currentPlayer = resources.getInteger(R.integer.currentPlayer)

        diceImageFragment = supportFragmentManager.findFragmentById(R.id.imageFragment) as DiceImageFragment
        supportFragmentManager.beginTransaction().hide(diceImageFragment!!).commit()

        playerScoreFragment = supportFragmentManager.findFragmentById(R.id.playerScoreFragment) as PlayerScoreFragment
        playerScoreFragment?.setup(intent?.extras?.getString(CustomData.EXTRA_GAMEMODE))
        playerTurnFragment = supportFragmentManager.findFragmentById(R.id.turnInfoFragment) as PlayerTurnInfoFragment

    }
}
